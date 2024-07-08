package com.practicecoding.sallonapp.appui.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicecoding.sallonapp.data.FireStoreDbRepository
import com.practicecoding.sallonapp.data.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.core.content.ContentProviderCompat.requireContext
import com.practicecoding.sallonapp.data.model.ChatModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(    private val repo: FireStoreDbRepository
): ViewModel() {
 var userChat = mutableStateOf<List<ChatModel>>(emptyList())
    var _messageList = mutableStateListOf<Message>()
    var messageList: SnapshotStateList<Message> = _messageList

    suspend fun onEvent(event: MessageEvent) {
        when(event){
            is MessageEvent.AddChat -> addChat(event.message,event.barberuid)
            is MessageEvent.GetChatUser -> getChatUser()
            is MessageEvent.MessageList -> getmessageList(event.barberuid)
        }
    }

    private suspend fun addChat(message: Message, barberuid:String){
        viewModelScope.launch {
            repo.addChat(message,barberuid)
        }
    }
    private suspend fun getChatUser(){
        viewModelScope.launch {
          userChat.value=  repo.getChatUser()
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
    data class AddChat(val message: Message,val barberuid:String):MessageEvent()
    data object GetChatUser:MessageEvent()
    data class MessageList(val barberuid:String):MessageEvent()
}