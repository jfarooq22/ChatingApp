package com.example.chatingapp.interactors

import android.util.Log
import com.example.chatingapp.contracts.NewMessagesContract
import com.example.chatingapp.items.UserItem
import com.example.chatingapp.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder

class NewMessageInteractor(newMessagesListenner: NewMessagesContract.NewMessagesListenner) {

    var newMessagesListenner: NewMessagesContract.NewMessagesListenner? = null
    init {
        this.newMessagesListenner = newMessagesListenner
    }

    fun fetchUsersFromFirebase() {
        val currentUserUid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users")

        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                p0.children.forEach{
                    Log.d("NewMessage",it.toString())
                    val user = it.getValue(User::class.java)
                    if(user!=null){
                        if(user.uid==currentUserUid){
                            Log.d("newmessage","current user not added")
                        }
                        else{
                            adapter.add(UserItem(user))
                        }
                    }
                }
               newMessagesListenner?.onfetchUsersSuccessful(adapter)
            }
            override fun onCancelled(p0: DatabaseError) {

            }

        })
    }

}