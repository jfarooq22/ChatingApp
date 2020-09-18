package com.example.chatingapp.presenters

import com.example.chatingapp.contracts.LoginContract
import com.example.chatingapp.interactors.LoginInteractor

class LoginPresenter(loginView: LoginContract.loginView): LoginContract.loginPresenter,LoginContract.onLoginListenner {
    var loginView: LoginContract.loginView? = null
    var loginInteractor: LoginInteractor? = null
    init{
        this.loginView = loginView
        loginInteractor = LoginInteractor(this)
    }

    override fun login(email:String,password:String){
        loginInteractor?.performLogin(email,password)
    }

    override fun onLoginSuccess() {
       loginView?.onLoginSuccess()
    }

    override fun onLoginFailed() {
        loginView?.onLoginFailed()
    }
}