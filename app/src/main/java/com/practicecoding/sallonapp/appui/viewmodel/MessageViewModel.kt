package com.practicecoding.sallonapp.appui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicecoding.sallonapp.data.FireStoreDbRepository
import com.practicecoding.sallonapp.data.model.ChatModel
import com.practicecoding.sallonapp.data.model.LastMessage
import com.practicecoding.sallonapp.data.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val repo: FireStoreDbRepository
) : ViewModel() {
    //    private var _userChat = mutableStateListOf<LastChatModel>()
//    var userChat :SnapshotStateList<LastChatModel> =_userChat
    private var _userChat = MutableStateFlow(emptyList<ChatModel>().toMutableList())
    var userChat: StateFlow<MutableList<ChatModel>> = _userChat.asStateFlow()

    //mutableStateOf<List<LastChatModel>>(emptyList())
//    private var _messageList = mutableStateListOf<Message>()
//    var messageList: SnapshotStateList<Message> = _messageList
    private var _messageList = MutableStateFlow(emptyList<Message>().toMutableList())
    var messageList: StateFlow<MutableList<Message>> = _messageList.asStateFlow()

    //    private
    private var _newChat = MutableStateFlow(false)
    var newChat: StateFlow<Boolean> = _newChat.asStateFlow()

    //    var count : SnapshotMutableState<Int> = _count
    init {
        viewModelScope.launch { getChatUser() }
    }

    private suspend fun addChat(message: LastMessage, barberuid: String, status: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.addChat(message, barberuid, status)
        }
    }

    suspend fun onEvent(event: MessageEvent) {
        when (event) {
            is MessageEvent.AddChat -> addChat(event.message, event.barberuid, event.status)
            is MessageEvent.GetChatUser -> getChatUser()
            is MessageEvent.MessageList -> getmessageList(event.barberuid)
        }
    }

    private suspend fun getChatUser() {
        viewModelScope.launch {
            _userChat.update { it.apply { clear() } }
            repo.getChatUser().collect { chat ->
                Log.d("uses", chat.toString())
                _userChat.emit(chat.sortedBy { it.message.time }.toMutableList())
                _newChat.value = false
                for (i in _userChat.value) {
                    if (!i.message.seenbyuser) {
                        _newChat.emit(true)
                        break
                    }
                }
            }
        }
        Log.d("uses", userChat.toString())
    }

    private suspend fun getmessageList(barberuid: String) {
        viewModelScope.launch {
            _messageList.update { it.apply { it.clear() } }
            repo.messageList(barberuid).collect { message ->
                _messageList.emit(message)
            }
        }
    }
}

sealed class MessageEvent {
    data class AddChat(
        val message: LastMessage,
        val barberuid: String,
        val status: Boolean = true
    ) : MessageEvent()

    data object GetChatUser : MessageEvent()
    data class MessageList(val barberuid: String) : MessageEvent()
}