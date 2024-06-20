package com.amtech.vendorservices.V.Notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.amtech.vendorservices.R
import com.amtech.vendorservices.V.sharedpreferences.SessionManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val tag = "FirebaseMessagingService"
    lateinit var sessionManager: SessionManager
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        println("$tag token --> $token")
        sessionManager= SessionManager( this@MyFirebaseMessagingService)
        sessionManager.fcmToken= token
        Log.e("sessionManager.fcmToken",sessionManager.fcmToken.toString())
        //  sessionManager.fcmToken=token
        //    Toast.makeText(this,"token",Toast.LENGTH_LONG).show()
        Log.e("FCMToken", token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        try {
            if (remoteMessage.notification != null) {
//                val soundServiceIntent = Intent(this, SoundService::class.java)
//                startService(soundServiceIntent)
                showNotification(
                    remoteMessage.notification?.title,
                    remoteMessage.notification?.body
                )
            }
            //
            //            else {
//                showNotification(remoteMessage.data["title"], remoteMessage.data["message"])
//            }

        } catch (e: Exception) {
            println("$tag error -->${e.localizedMessage}")
            Log.e("NotifyError", e.localizedMessage)
        }
    }

    private fun showNotification(
        title: String?,
        body: String?
    ) {
        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        val pendingIntent = PendingIntent.getActivity(
//            this, 0, intent,
//            PendingIntent.FLAG_ONE_SHOT
//        )
        // val intent1 = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_MUTABLE)

        val pendingIntent: PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(
                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        val channelId = getString(R.string.channel_id)
        val channelName = getString(R.string.channel_name)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupNotificationChannels(channelId, channelName, notificationManager)
        }

        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.app_logo)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSilent(false)
            .setOngoing(false)
            // .addAction(R.drawable.logo, "Delete", pIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(soundUri)
            .setContentIntent(pendingIntent)
        notificationManager.notify(0, notificationBuilder.build())

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun setupNotificationChannels(
        channelId: String,
        channelName: String,
        notificationManager: NotificationManager
    ) {

        val channel =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
        channel.enableLights(true)
        channel.lightColor = Color.GREEN
        channel.enableVibration(true)
        notificationManager.createNotificationChannel(channel)
    }

}


/*    private val mediaPlayer: MediaPlayer? = null
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("okk", remoteMessage.data.toString())
        if (remoteMessage.data.size > 0) {
            val title = remoteMessage.data["title"]
            val body = remoteMessage.data["body"]
//            val i = Intent(this, Appointments::class.java)
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
//            i.putExtra("title", title)
//            i.putExtra("body", body)
//            startActivity(i)
            val soundServiceIntent = Intent(this, SoundService::class.java)
            startService(soundServiceIntent)

            val rideData = remoteMessage.data

            for ((key, value) in rideData) {
                // Use key and value as needed
                // For example, you can log them or update your app UI
                Log.d("FirebaseMessage", "Key: $key Value: $value")
            }
            val id = rideData["id"]
            val rideNo = rideData["ride_no"]


          //   Handle the notification data here and pass it to FireBaseMessageActivity
//            val intent = Intent(this, Appointments::class.java)
//            intent.putExtra("title", title)
//            intent.putExtra("body", body)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Required to start activity from a service
//            startActivity(intent)
           // showNotification(this, title, body)
            showNotificationNew(this, title!!, body!!)
        }
    }

    override fun onNewToken(token: String) {
        Log.d("TAG", "Refreshed token: $token")

        // If you need to send this token to your server, do it here.
    }

    private fun showNotification(
        context: Context,
        title: String?,
        message: String?,
     ) {
        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        intent.putExtra("title", title)
//        intent.putExtra("body", message)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = 1
        val channelId = this.getString(R.string.channel_id)
        val channelName = "EHCF-channel"
        val important = NotificationManager.IMPORTANCE_HIGH
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(
                channelId, channelName, important
            )
            notificationManager.createNotificationChannel(mChannel)
        }
        //
//
        val intent1 = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_MUTABLE)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val actionIntent = Intent(this, Reciver::class.java)
        actionIntent.setAction("action_id")
        //
//
        var flag = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) flag = PendingIntent.FLAG_IMMUTABLE
        val pIntent = PendingIntent.getBroadcast(this, 0, actionIntent, flag)


//         Remove the custom sound URI and use the default sound
        val mBuilder = NotificationCompat.Builder(
            context,
            this.getString(R.string.channel_id)
        )
            .setSmallIcon(R.drawable.logonew)
            .setContentTitle(title)
            .setContentText(message)
            .setSound(defaultSoundUri)
            .setSilent(false)
            .setContentIntent(intent1)
            .setOngoing(false)
            .addAction(R.drawable.logonew, "Join the meeting", pIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        val mNotification = mBuilder.build()


        // Display notification
        notificationManager.notify(1, mNotification)
    }*/



