package com.example.chatingapp.interactors

import android.app.NotificationManager
import android.os.Build
import android.provider.Settings.Global.getString
import com.example.chatingapp.R
import com.example.chatingapp.contracts.LatestMessagesContract
import com.example.chatingapp.items.LatestMessageRow
import com.example.chatingapp.models.ChatMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder

class LatestMessagesInteractor(latestMessagesListenner: LatestMessagesContract.LatestMessagesListenner) {

    var latestMessagesListenner:LatestMessagesContract.LatestMessagesListenner? = null
    init {
        this.latestMessagesListenner = latestMessagesListenner
    }

    val adapter = GroupAdapter<ViewHolder>()
    val latestMessagesMap = HashMap<String, ChatMessage>()

    fun getLatestMessages(){
        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId")
        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java) ?: return
                latestMessagesMap[p0.key!!] = chatMessage
                refreshRecyclerViewMessages()
//                sendNotification(chatMessage)
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java) ?: return
                latestMessagesMap[p0.key!!] = chatMessage
                refreshRecyclerViewMessages()
//                sendNotification(chatMessage)
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(p0: DatabaseError) {
                latestMessagesListenner?.onLatestMessagesFailed()
            }

        })
    }

    private fun refreshRecyclerViewMessages() {
        adapter.clear()
        latestMessagesMap.values.forEach{
            adapter.add(LatestMessageRow(it))
        }
        latestMessagesListenner?.onLatestMessagesReady(adapter)
    }


    private fun sendNotification(chatMessage: ChatMessage){
        latestMessagesListenner?.listenforNotification(chatMessage)

    }
}