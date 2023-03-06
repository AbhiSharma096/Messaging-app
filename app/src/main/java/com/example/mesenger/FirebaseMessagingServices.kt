package com.example.mesenger

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Vibrator
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*


class FirebaseMessagingServices : FirebaseMessagingService() {
    var mNotificationManager: NotificationManager? = null
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)


// playing audio and vibration when user se reques
        val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val r = RingtoneManager.getRingtone(applicationContext, notification)
        r.play()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            r.isLooping = false
        }




        // vibration
//        val v = getSystemService(VIBRATOR_SERVICE) as Vibrator
//        val pattern = longArrayOf(100, 300, 300, 300)
//        v.vibrate(pattern, -1)
        val resourceImage = resources.getIdentifier(
            remoteMessage.notification!!.icon, "drawable", packageName
        )

        val intent = Intent( this, ChatActivity::class.java)
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

              val pendingIntent = PendingIntent.getActivity(
              applicationContext, 0 /* Request code */, intent,
              PendingIntent.FLAG_IMMUTABLE
              )


        val builder = NotificationCompat.Builder(this, "CHANNEL_ID")
            .setOnlyAlertOnce(true)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setContentTitle(remoteMessage!!.notification!!.title!!)
            .setContentText(remoteMessage!!.notification!!.body!!)
            .setSmallIcon(resourceImage)

        mNotificationManager =
            this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "Your_channel_id"
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_HIGH
            )
            mNotificationManager!!.createNotificationChannel(channel)
            builder.setChannelId(channelId)
        }


    // notificationId is a unique int for each notification that you must define
        mNotificationManager!!.notify(100, builder.build())
    }
}


