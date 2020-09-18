package com.example.chatingapp.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.chatingapp.contracts.LatestMessagesContract
import com.example.chatingapp.R
import com.example.chatingapp.views.NewMessageActivity.Companion.USER_KEY
import com.example.chatingapp.items.LatestMessageRow
import com.example.chatingapp.presenters.LatestMessagesPresenter
import com.google.firebase.auth.FirebaseAuth
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_latest_messages.*

class LatestMessagesActivity : AppCompatActivity(), LatestMessagesContract.LatestMessagesView {

    var latestMessagesPresenter: LatestMessagesPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_messages)

        supportActionBar?.title="Inbox"

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
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
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
}