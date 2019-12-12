package com.yt.androidytdownload.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import java.util.*

class MyNotification {

    companion object{
        var count = 0
    }


    val channelId: String
    var builder: NotificationCompat.Builder
    val notificationManager: NotificationManager


    constructor(channelId: String, channelName: String, context: Context, notificationManager: NotificationManager) {
        this.channelId = channelId
        this.builder = NotificationCompat.Builder(context, this.channelId)
        this.notificationManager = notificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val notificationChannel: NotificationChannel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)

            notificationManager.createNotificationChannel(notificationChannel)
            builder.setChannelId(channelId)
        }

    }


    fun setNotificationBuilder(title: String, content: String, priority: Int, icon: Int): MyNotification {
        builder.setContentTitle(title).setContentText(content).setPriority(priority).setSmallIcon(icon)
        return this
    }


    fun makeNotification() {
        notificationManager.notify(count++,builder.build())
    }


}