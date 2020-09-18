package com.example.chatingapp.presenters

import android.util.Log
import com.example.chatingapp.contracts.NewMessagesContract
import com.example.chatingapp.interactors.NewMessageInteractor
import com.example.chatingapp.items.UserItem
import com.example.chatingapp.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder

class NewMessagePresenter(newMessagesView: NewMessagesContract.NewMessagesView):NewMessagesContract.NewMessagesPresenter, NewMessagesContract.NewMessagesListenner {
    var newMessagesView: NewMessagesContract.NewMessagesView? = null
    var newMessagesInteractor : NewMessageInteractor? = null
    init{
        this.newMessagesView = newMessagesView
        newMessagesInteractor = NewMessageInteractor(this)
    }

    override fun fetchUsers() {
        newMessagesInteractor?.fetchUsersFromFirebase()
    }

    override fun onfetchUsersSuccessful(adapter: GroupAdapter<ViewHolder>) {
        newMessagesView?.onNewMessagesReady(adapter)
    }

    override fun onfetchUsersFailed() {
        newMessagesView?.onNewMessagesFailed()
    }
}