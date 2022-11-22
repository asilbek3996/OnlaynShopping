package com.example.onlaynshop.service

import android.app.Notification.DEFAULT_VIBRATE
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION_CODES
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.multidex.BuildConfig.APPLICATION_ID
import com.example.onlaynshop.R
import com.example.onlaynshop.utils.PrefUtils
import com.example.onlineshop.screen.MainActivity
import com.google.firebase.messaging.BuildConfig
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class AppFirebaseMessagingService:FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.d("tag-debug : ", token)
        PrefUtils.setFCMToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        try {
            Log.d("tag-debug : body ",remoteMessage?.notification?.body.toString())
            Log.d("tag-debug : title ",remoteMessage?.notification?.title.toString())
            val title = remoteMessage.notification?.title
            val body = remoteMessage.notification?.body
            showmessage(
                title ?: "",
                body ?: ""
            )
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    fun showmessage(title:String,body:String,id:Long = System.currentTimeMillis()){
        val defaultSoundUri:Uri? =  RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        var intent = Intent(this,MainActivity::class.java)

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT
        )
        val channelId = BuildConfig.VERSION_NAME
        val builder =
            NotificationCompat.Builder(this,channelId)
                .setDefaults(DEFAULT_VIBRATE)
                .setStyle(NotificationCompat.BigTextStyle().bigText(body))
                .setSmallIcon(R.drawable.menus)
                .setLargeIcon(BitmapFactory.decodeResource(applicationContext.resources,R.drawable.menus))
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setColor(Color.parseColor("#FFFFFF"))
                .setSound(defaultSoundUri)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(longArrayOf(100,200,300,400,500,400,300,200,400))
                .setContentIntent(pendingIntent)

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BASE){
            val channel = NotificationChannel(
                channelId,
                "${BuildConfig.VERSION_NAME} channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            manager.createNotificationChannel(channel)
        }
        manager.notify(id.toInt(),builder.build())

    }
}