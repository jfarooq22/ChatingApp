package com.example.chatingapp.contracts

import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder

interface ChatLogView {

    fun onChatLogReady(adapter:GroupAdapter<ViewHolder>)

    fun onChatLogFailed()

    fun onMessageSend()

    fun onMessgeSendFailed()
}