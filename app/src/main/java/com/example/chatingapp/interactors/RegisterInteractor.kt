package com.example.chatingapp.interactors

import android.net.Uri
import android.util.Log
import com.example.chatingapp.contracts.RegisterContract
import com.example.chatingapp.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class RegisterInteractor(registerListenner: RegisterContract.onRegisterListenner)
{
    var registerListenner : RegisterContract.onRegisterListenner? = null
    init {
        this.registerListenner = registerListenner
    }

    fun createUser(email:String, password:String, imageUri:Uri, username:String){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener{
                if(!it.isSuccessful) {
                    registerListenner?.onRegisterFailed()
                    return@addOnCompleteListener
                }
                registerListenner?.onRegisterSuccessful()
                Log.d("Register","Added user with userid: ${it.result?.user?.uid}")
                uploadImageToFirebaseStorage(imageUri,username)
            }

    }

    private fun uploadImageToFirebaseStorage(selectedPhotoUri:Uri, username:String){

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(selectedPhotoUri)
            .addOnSuccessListener {
                Log.d("Register","Uploaded image to ${it.metadata?.path}")
                ref.downloadUrl.addOnSuccessListener {
                    Log.d("Register","path: $it")
                    saveUserToFirebase(it.toString(),username)
                }
            }
            .addOnFailureListener{
                Log.d("Register","Failed to upload image to storage: ${it.message}")
            }
    }

    private fun saveUserToFirebase(profileImageUrl:String,username:String){
        val uid = FirebaseAuth.getInstance().uid ?:""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid,username,profileImageUrl)

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("Register","finally we saved user to database")
                registerListenner?.onUserCreated()

            }
            .addOnFailureListener{
                Log.d("Register","failed to save user to database")
                registerListenner?.onUserCreatedFailed()

            }
    }







}