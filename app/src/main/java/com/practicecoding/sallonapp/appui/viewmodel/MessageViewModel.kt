package com.practicecoding.sallonapp.appui.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicecoding.sallonapp.data.FireStoreDbRepository
import com.practicecoding.sallonapp.data.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.practicecoding.sallonapp.data.model.LastChatModel
import com.practicecoding.sallonapp.data.model.LastMessage
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(    private val repo: FireStoreDbRepository
): ViewModel() {
    var userChat = mutableStateOf<List<LastChatModel>>(emptyList())
    var _messageList = mutableStateListOf<Message>()
    var messageList: SnapshotStateList<Message> = _messageList
    var _count = mutableIntStateOf(0)
    init {
        viewModelScope.launch { getChatUser() }
    }
    private suspend fun addChat(message: LastMessage, barberuid:String, status:Boolean){
        viewModelScope.launch {
            repo.addChat(message,barberuid,status)
        }
    }
    suspend fun onEvent(event: MessageEvent) {
        when(event){
            is MessageEvent.AddChat -> addChat(event.message,event.barberuid,event.status)
            is MessageEvent.GetChatUser -> getChatUser()
            is MessageEvent.MessageList -> getmessageList(event.barberuid)
        }
    }
    private suspend fun getChatUser(){
        viewModelScope.launch {
            repo.getChatUser().collect { chat ->
                userChat.value = chat
                _count.value=0
                for (i in userChat.value) {
                    if (!i.message.seenbybarber) {
                        _count.value++
                    }
                    if (_count.value > 1) {
                        break
                    }
                }
            }
        }
        Log.d("uses",userChat.toString())
    }
    private suspend fun getmessageList(barberuid:String){
        viewModelScope.launch {
             repo.messageList(barberuid).collect{
                 message->
                 _messageList.clear()
                 _messageList.addAll(message)
             }
        }
    }
}

sealed class MessageEvent {
    data class AddChat(val message: LastMessage,val barberuid:String,val status:Boolean=true):MessageEvent()
    data object GetChatUser:MessageEvent()
    data class MessageList(val barberuid:String):MessageEvent()
}