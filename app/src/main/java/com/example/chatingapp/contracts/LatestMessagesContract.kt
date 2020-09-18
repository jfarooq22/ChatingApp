package com.example.chatingapp.contracts

import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder

interface LatestMessagesContract {

    interface LatestMessagesView {
        fun onMessagesReady(adapter:GroupAdapter<ViewHolder>)
        fun onMessagesFailed()
    }

    interface LatestMessagesPresenter{
        fun listenForLatestMessages()
    }

    interface LatestMessagesListenner {
        fun onLatestMessagesReady(adapter:GroupAdapter<ViewHolder>)
        fun onLatestMessagesFailed()
    }


}