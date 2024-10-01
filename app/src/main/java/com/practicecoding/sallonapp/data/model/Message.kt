package com.practicecoding.sallonapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
data class Message(val status: Boolean, val message: String, val time: String)

@Serializable
@Parcelize
data class LastMessage(val status: Boolean, val message: String, val time: String,
                       val seenbybarber:Boolean, var seenbyuser:Boolean) : Parcelable

@Parcelize
data class ChatModel(val name: String,val message:LastMessage,val image:String,val uid:String,val phoneNumber:String) :
    Parcelable
