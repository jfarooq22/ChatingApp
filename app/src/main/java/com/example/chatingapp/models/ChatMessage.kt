package com.example.chatingapp.models

import android.text.Editable

class ChatMessage(val id: String,val text: String,val fromId:String,val toId:String,val timestamp:Long) {
    constructor():this("","","","",-1)
}