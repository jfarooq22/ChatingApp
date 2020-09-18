package com.example.chatingapp.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.chatingapp.contracts.NewMessagesContract
import com.example.chatingapp.R
import com.example.chatingapp.items.UserItem
import com.example.chatingapp.presenters.NewMessagePresenter
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_message.*

class NewMessageActivity : AppCompatActivity(), NewMessagesContract.NewMessagesView {

    var newMessagesPresenter: NewMessagePresenter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        supportActionBar?.title="Select User"

        newMessagesPresenter = NewMessagePresenter(this)
        newMessagesPresenter?.fetchUsers()
    }

    companion object{
        val USER_KEY = "USER_KEY"
    }


    override fun onNewMessagesReady(adapter:GroupAdapter<ViewHolder>) {
        recyclerview_newmessage.adapter=adapter
        adapter.setOnItemClickListener{item,view->

        val userItem = item as UserItem
        val intent = Intent(view.context,ChatLogActivity::class.java)
        intent.putExtra(USER_KEY,userItem.user)
        startActivity(intent)

        finish()
    }

    }

    override fun onNewMessagesFailed() {
        Toast.makeText(this,"Failed to retrieve users", Toast.LENGTH_SHORT).show()
    }
}