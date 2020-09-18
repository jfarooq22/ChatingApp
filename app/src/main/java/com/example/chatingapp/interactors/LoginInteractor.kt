package com.example.chatingapp.interactors

import android.util.Log
import com.example.chatingapp.contracts.LoginContract
import com.google.firebase.auth.FirebaseAuth

class LoginInteractor(loginListenner: LoginContract.onLoginListenner) {

    var loginListenner : LoginContract.onLoginListenner? = null
    init {
    this.loginListenner = loginListenner
    }

    fun performLogin(email:String, password:String){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
            .addOnCompleteListener{
                if(!it.isSuccessful){
                    loginListenner?.onLoginFailed()
                    return@addOnCompleteListener
                }
                Log.d("Login","Login successful")
                loginListenner?.onLoginSuccess()

            }
            .addOnFailureListener{
                Log.d("Login","Login unsuccessful")
                return@addOnFailureListener
            }
    }

}