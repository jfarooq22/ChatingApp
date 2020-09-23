package com.example.chatingapp.notifications

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @Headers(
        "Content-Type: application/json",
        "Authorization:key=AAAASHJljW0:APA91bFI7PnWPUdT627DgPdtjTW3DtLuL0B0MtfMFioyoZbmt9TXpfhVxG0cuI1kE6tmVtNzCwhlzDuSMSnGJmQLACjN9PhaoxXnj4Ph8XWf1O7MGyT0XxuOgv3vaof--ZWMPrlxhAhw"
    )@POST("fcm/send")
    open fun sendNotification(@Body body: NotificationSender?): Call<MyResponse?>?
}