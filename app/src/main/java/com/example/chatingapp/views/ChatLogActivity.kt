package com.example.chatingapp.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.widget.Toast
import androidx.core.content.res.TypedArrayUtils.getText
import com.example.chatingapp.contracts.ChatLogView
import com.example.chatingapp.R
import com.example.chatingapp.models.User
import com.example.chatingapp.notifications.ApiService
import com.example.chatingapp.notifications.Client
import com.example.chatingapp.notifications.SendNotifications
import com.example.chatingapp.presenters.ChatLogPresenter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*

class ChatLogActivity : AppCompatActivity(), ChatLogView {

    var chatLogPresenter: ChatLogPresenter? = null
    var sendNotifications: SendNotifications? = null
    val adapter = GroupAdapter<ViewHolder>()
    lateinit var apiService: ApiService
    var toUser: User? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        toUser = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        supportActionBar?.title = toUser?.username

        chatLogPresenter = ChatLogPresenter(this)
        sendNotifications = SendNotifications()

        chatLogPresenter?.listenForMessages(toUser!!)

        val text = edittext_chat_log.text
        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)


        send_button_chat_log.setOnClickListener {
            Log.d("chatlog", "trying to send message")
            chatLogPresenter?.performSendMessage(text, user!!)
            sendNotifications?.getToUserToken(text.toString(), user!!)

        }
    }

        override fun onChatLogReady(adapter: GroupAdapter<ViewHolder>) {
            recyclerview_chat_log.adapter = adapter
            recyclerview_chat_log.scrollToPosition(adapter.itemCount - 1)
        }

        override fun onChatLogFailed() {
            Toast.makeText(this, "Could not update chatlog", Toast.LENGTH_LONG).show()
        }

        override fun onMessageSend() {
            edittext_chat_log.text.clear()
            recyclerview_chat_log.scrollToPosition(adapter.itemCount - 1)

        }

        override fun onMessgeSendFailed() {
            Toast.makeText(this, "Could not send message", Toast.LENGTH_LONG).show()
        }
 }
