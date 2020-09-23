package com.example.chatingapp.presenters

import android.util.Log
import com.example.chatingapp.contracts.LatestMessagesContract
import com.example.chatingapp.interactors.LatestMessagesInteractor
import com.example.chatingapp.models.ChatMessage
import com.example.chatingapp.items.LatestMessageRow
import com.example.chatingapp.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder

class LatestMessagesPresenter(latestMessagesView: LatestMessagesContract.LatestMessagesView):LatestMessagesContract.LatestMessagesPresenter,
    LatestMessagesContract.LatestMessagesListenner {

    companion object{
        var currentUser: User?=null

    }

    val adapter = GroupAdapter<ViewHolder>()
    val latestMessagesMap = HashMap<String, ChatMessage>()

    var latestMessagesView: LatestMessagesContract.LatestMessagesView? =null
    var latestMessagesInteractor: LatestMessagesInteractor? =null
    init{
        this.latestMessagesView = latestMessagesView
        latestMessagesInteractor = LatestMessagesInteractor(this)
    }

    override fun listenForLatestMessages(){
        latestMessagesInteractor?.getLatestMessages()
    }

    override fun listenforNotification(chatMessage: ChatMessage) {
      latestMessagesView?.onNotificationReady(chatMessage)
    }

    fun fetchCurrentUser() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                currentUser = p0.getValue(User::class.java)
                Log.d("latestmessages","Current User: ${currentUser?.username}")
            }
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun onLatestMessagesReady(adapter: GroupAdapter<ViewHolder>) {
        latestMessagesView?.onMessagesReady(adapter)
    }

    override fun onLatestMessagesFailed() {
        latestMessagesView?.onMessagesFailed()
    }
}