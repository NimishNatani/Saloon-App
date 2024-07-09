package com.practicecoding.sallonapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Message(val status: Boolean, val message: String, val time: String)

data class ChatModel(val name: String,val message:Message,val image:String,val uid:String)
