package com.example.chatingapp.contracts

interface LoginContract {

    interface loginView{
        fun onLoginSuccess()
        fun onLoginFailed()
    }

    interface loginPresenter{
        fun login(email:String,password:String)
    }

    interface onLoginListenner{
        fun onLoginSuccess()
        fun onLoginFailed()
    }


}