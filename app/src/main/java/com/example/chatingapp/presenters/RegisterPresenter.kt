package com.example.chatingapp.presenters

import android.net.Uri
import com.example.chatingapp.interactors.RegisterInteractor
import com.example.chatingapp.contracts.RegisterContract

class RegisterPresenter(registerView: RegisterContract.registerView):RegisterContract.registerPresenter,RegisterContract.onRegisterListenner {
    var registerView: RegisterContract.registerView?=null
    var registerInteractor: RegisterInteractor? = null


    init{
        this.registerView = registerView
        registerInteractor = RegisterInteractor(this)


    }
    override fun performRegister(email:String, password:String, imageUri:Uri, username:String){
        registerInteractor?.createUser(email,password,imageUri,username)
    }

    override fun onUserCreated() {
       registerView?.onSavedUserSuccessful()
    }

    override fun onUserCreatedFailed() {
      registerView?.onSavedUserFailed()
    }


    override fun onRegisterSuccessful() {
        registerView?.onRegisterSuccessful()
    }

    override fun onRegisterFailed() {
        registerView?.onRegisterFailed()
    }

}