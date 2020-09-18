package com.example.chatingapp.presenters

import android.text.Editable
import android.util.Log
import com.example.chatingapp.contracts.ChatLogView
import com.example.chatingapp.models.ChatMessage
import com.example.chatingapp.items.ChatToItem
import com.example.chatingapp.items.ChatfromItem
import com.example.chatingapp.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder

class ChatLogPresenter(chatLogView: ChatLogView) {
    var chatLogView : ChatLogView? = null
    init{
        this.chatLogView = chatLogView
    }

    val adapter = GroupAdapter<ViewHolder>()
    var toUser: User? = null

    fun listenForMessages(toUser:User){
        val fromId = FirebaseAuth.getInstance().uid
        val toId = toUser.uid
        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")

        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java)
                if(chatMessage!=null){
                    Log.d("chatlog",chatMessage.text)
                    if(chatMessage.fromId== FirebaseAuth.getInstance().uid)
                    {
                        val currentUser = LatestMessagesPresenter.currentUser

                        adapter.add(ChatfromItem(chatMessage.text,currentUser!!))
                    }else{
                        adapter.add(ChatToItem(chatMessage.text, toUser))
                    }
                }
                chatLogView?.onChatLogReady(adapter)
            }

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }
        })
    }

    @Exclude
    fun performSendMessage(text: Editable, user:User){

        val fromId = FirebaseAuth.getInstance().uid

        val toId = user.uid

        if (fromId== null)return

        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()

        val toref = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()

        val chatMessage = ChatMessage(ref.key!!, text.toString(), fromId, toId,System.currentTimeMillis()/1000)
        ref.setValue(chatMessage)
            .addOnSuccessListener {
                Log.d("chatlog","Saved message to firebase ${ref.key}")
                chatLogView?.onMessageSend()
            }

        toref.setValue(chatMessage)

        val latestMessageRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toId")
        latestMessageRef.setValue(chatMessage)

        val latestMessageToRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/$fromId")
        latestMessageToRef.setValue(chatMessage)


    }
}