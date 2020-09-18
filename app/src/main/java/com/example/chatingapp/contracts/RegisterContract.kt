package com.example.chatingapp.contracts

import android.net.Uri

interface RegisterContract {

    interface  registerView{
        fun onRegisterSuccessful()
        fun onRegisterFailed()
        fun onSavedUserSuccessful()
        fun onSavedUserFailed()
    }
    interface registerPresenter{
        fun performRegister(email:String, password:String, imageUri: Uri, username:String)
    }
    interface onRegisterListenner{
        fun onRegisterSuccessful()
        fun onRegisterFailed()
        fun onUserCreated()
        fun onUserCreatedFailed()
    }

}