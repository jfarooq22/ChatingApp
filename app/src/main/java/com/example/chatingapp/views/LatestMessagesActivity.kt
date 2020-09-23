package com.example.chatingapp.views

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.chatingapp.contracts.LatestMessagesContract
import com.example.chatingapp.R
import com.example.chatingapp.views.NewMessageActivity.Companion.USER_KEY
import com.example.chatingapp.items.LatestMessageRow
import com.example.chatingapp.models.ChatMessage
import com.example.chatingapp.models.User
import com.example.chatingapp.presenters.LatestMessagesPresenter
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_latest_messages.*
import kotlinx.android.synthetic.main.latest_message_row.view.*

class LatestMessagesActivity : AppCompatActivity(), LatestMessagesContract.LatestMessagesView {
    val context = this
    var latestMessagesPresenter: LatestMessagesPresenter? = null

    var notificationManager: NotificationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_messages)

        supportActionBar?.title="Inbox"

        createNotificationChannel()
        latestMessagesPresenter = LatestMessagesPresenter(this)

        latestMessagesPresenter?.listenForLatestMessages()
        latestMessagesPresenter?.fetchCurrentUser()


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.menu_new_message ->{
                val intent = Intent(this, NewMessageActivity::class.java)
                startActivity(intent)
            }
            R.id.menu_sign_out ->{
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, LoginActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                finish()
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onMessagesReady(adapter:GroupAdapter<ViewHolder>) {
        recyclerview_latest_messages.adapter =adapter
        recyclerview_latest_messages.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))
        adapter.setOnItemClickListener { item, view ->
            val intent = Intent(this@LatestMessagesActivity,ChatLogActivity::class.java)

            val row = item as LatestMessageRow
            intent.putExtra(USER_KEY,row.chatPartnerUser)
            startActivity(intent)
        }
    }

    override fun onMessagesFailed() {
        Toast.makeText(this,"Failed to retrieve latest messages",Toast.LENGTH_SHORT).show()
    }

    override fun onNotificationReady(chatMessage: ChatMessage) {

        var chatPartnerUser: User? = null
        val chatPartnerId : String

        if(chatMessage.fromId == FirebaseAuth.getInstance().uid) {
            chatPartnerId = chatMessage.toId
        } else{
            chatPartnerId = chatMessage.fromId
        }

        val ref = FirebaseDatabase.getInstance().getReference("/users/$chatPartnerId")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                chatPartnerUser = p0.getValue(User::class.java)
                showNotification(chatMessage,chatPartnerUser!!)
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }


    })

    }
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "ChatingApp"
            val descriptionText = "Message Notification"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("ChatingApp", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager!!.createNotificationChannel(channel)
        }

    }

    private fun showNotification(chatMessage: ChatMessage, chatPartnerUser: User){
        var builder = NotificationCompat.Builder(context, "ChatingApp")
            .setSmallIcon(R.drawable.messaginglogo_background)
            .setContentTitle(chatPartnerUser?.username)
            .setContentText(chatMessage.text)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        notificationManager?.notify(10,builder.build())
    }

}