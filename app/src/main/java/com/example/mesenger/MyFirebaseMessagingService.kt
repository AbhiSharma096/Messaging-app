package com.example.mesenger

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val chanelId = "MyNotification"
const val chanelName = "MyNotification"

class MyFirebaseMessagingService: FirebaseMessagingService(){





       override fun onMessageReceived(p0: RemoteMessage) {
              super.onMessageReceived(p0)
              generateNotification(p0.notification?.title.toString(), p0.notification?.body.toString())
       }

            // attach it with xml file
      fun getremoteview(title: String, message: String): RemoteViews {
            val remoteview = RemoteViews(packageName, R.layout.notification)
            remoteview.setTextViewText(R.id.Title, title)
            remoteview.setTextViewText(R.id.Description, message)
            remoteview.setImageViewResource(R.id.Logo, R.drawable.icon)
            return remoteview
      }

      // first generate notification
      fun generateNotification(title: String, message: String) {

            val intent = Intent(this, ContactActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)


            val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

            val notificationBuilder = NotificationCompat.Builder(this, chanelId)
                        .setSmallIcon(R.drawable.icon)
                        .setVibrate(longArrayOf(900,900, 900, 900, 900))
                        .setAutoCancel(true)
                        .setOnlyAlertOnce(true)
                        .setContentIntent(pendingIntent)
            notificationBuilder.setContent(getremoteview(title, message))

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                  val notificationChannel = NotificationChannel(chanelId, chanelName, NotificationManager.IMPORTANCE_HIGH)
                  notificationManager.createNotificationChannel(notificationChannel)
            }
                notificationManager.notify(0, notificationBuilder.build())
      }





}