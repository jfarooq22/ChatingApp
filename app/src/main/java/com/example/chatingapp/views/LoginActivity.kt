package com.example.chatingapp.views

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.chatingapp.contracts.LoginContract
import com.example.chatingapp.R
import com.example.chatingapp.presenters.LoginPresenter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), LoginContract.loginView {

    var loginPresenter:LoginPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        var user = FirebaseAuth.getInstance().currentUser
        if(user!=null){
            onLoginSuccess()
        }
        else{
            Log.d("Login","User not logged in already")
        }


        loginPresenter= LoginPresenter(this)
        login_button_login.setOnClickListener {

            val email = email_edittext_login.text.toString()
            val password = password_edittext_login.text.toString()
            progressBar.visibility=View.VISIBLE
//            sp.edit().putBoolean("logged",true)

            loginPresenter?.login(email,password)
        }

        signup_textview.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onLoginSuccess() {
            progressBar.visibility = View.INVISIBLE
            Log.d("Login","Login successful")
        Toast.makeText(this,"Login Successful", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LatestMessagesActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
    }

    override fun onLoginFailed() {
            progressBar.visibility = View.INVISIBLE
            Toast.makeText(this,"Email or password is wrong", Toast.LENGTH_LONG).show()
    }


}