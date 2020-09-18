package com.example.chatingapp.views

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.chatingapp.R
import com.example.chatingapp.contracts.RegisterContract
import com.example.chatingapp.presenters.RegisterPresenter
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.progressBar

class RegisterActivity : AppCompatActivity(), RegisterContract.registerView {

    var registerPresenter:RegisterPresenter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        register_button_register.setOnClickListener {
            val username = username_edittext_register.text.toString()
            val email = email_edittext_register.text.toString()
            val password = password_edittext_register.text.toString()
            registerPresenter = RegisterPresenter(this)

            if(email!=null && password!=null && username!=null && selectedPhotoUri!= null ){
                registerPresenter?.performRegister(email,password,selectedPhotoUri!!,username)
                progressBar.visibility= View.VISIBLE
            }
            else{
                onNoValuesEntered()
            }


        }

        already_have_an_account_textview.setOnClickListener {
            finish()
        }

        select_image_button.setOnClickListener {

            val intent = Intent(Intent.ACTION_PICK)
            intent.type="image/*"
            startActivityForResult(intent,0)
        }

    }

    var selectedPhotoUri : Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==0 && resultCode== Activity.RESULT_OK && data!=null){
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,selectedPhotoUri)

            selectphoto_imageview_register.setImageBitmap(bitmap)
            select_image_button.alpha =0f

        }
    }


    private fun onNoValuesEntered() {
        Toast.makeText(this,"No field should be left empty", Toast.LENGTH_LONG).show()
        return
    }

    override fun onRegisterSuccessful() {
        Toast.makeText(this,"User registered successfully", Toast.LENGTH_LONG).show()
    }


    override fun onRegisterFailed() {
        Toast.makeText(this,"Registration failed. Please try again", Toast.LENGTH_LONG).show()
    }

    override fun onSavedUserSuccessful(){
        progressBar.visibility= View.INVISIBLE
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onSavedUserFailed() {
        progressBar.visibility=View.INVISIBLE
        Log.d("RegisterView","Failed to save user")
    }
}