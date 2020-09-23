package com.example.chatingapp.notifications

import android.os.Message
import android.util.Log
import android.widget.Toast
import androidx.core.content.res.TypedArrayUtils.getText
import com.example.chatingapp.models.User
import com.example.chatingapp.presenters.LatestMessagesPresenter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.iid.FirebaseInstanceId
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SendNotifications {
    private var apiService = Client.getClient("https://fcm.googleapis.com/").create(ApiService::class.java)
    private var currentUser :String? = null
    fun getchatpartnerId(){

    }


    fun getToUserToken(text: String,  user:User){
        UpdateToken()
        fetchCurrentUser()
        FirebaseDatabase.getInstance().getReference().child("Tokens").child(user.uid).child("token")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    dataSnapshot
                    var usertoken = dataSnapshot.getValue().toString()
                    sendNotification(usertoken, currentUser!!, text)
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })

    }



private fun UpdateToken(){
    var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    var refreshToken:String= FirebaseInstanceId.getInstance().getToken().toString()
    var token:Token=Token(refreshToken)
    FirebaseDatabase.getInstance().getReference("Tokens").child(FirebaseAuth.getInstance().getCurrentUser()!!.getUid()).setValue(token)
}

private fun sendNotification(usertoken:String,title: String,message: String){
    var data=Data(title,message)
    var sender= NotificationSender(data,usertoken)
    apiService.sendNotification(sender)!!.enqueue(object : Callback<MyResponse?> {

        override fun onResponse(call: Call<MyResponse?>, response: Response<MyResponse?>) {
            if (response.code() === 200) {
                Log.d("notif", response.code().toString())

                if (response.body()!!.success !== 1) {
//                    Toast.makeText(this, "Failed ", Toast.LENGTH_LONG).show()
                }
            }
        }

        override fun onFailure(call: Call<MyResponse?>, t: Throwable?) {

        }
    })
}
    fun fetchCurrentUser() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val currentUserRef = p0.getValue(User::class.java)
                currentUser = currentUserRef?.username
//                Log.d("latestmessages","Current User: ${LatestMessagesPresenter.currentUser?.username}")
            }
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}