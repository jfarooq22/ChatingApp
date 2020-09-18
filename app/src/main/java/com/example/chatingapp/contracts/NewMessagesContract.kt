package com.example.chatingapp.contracts

import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder

interface NewMessagesContract {

    interface NewMessagesView{
        fun onNewMessagesReady(adapter:GroupAdapter<ViewHolder>)

        fun onNewMessagesFailed()
    }

    interface NewMessagesPresenter{
        fun fetchUsers()
    }

    interface NewMessagesListenner{
        fun onfetchUsersSuccessful(adapter: GroupAdapter<ViewHolder>)
        fun onfetchUsersFailed()
    }


}