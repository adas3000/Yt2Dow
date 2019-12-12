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
        var notificationManager:NotificationManager? = null
    }


    val channelId: String
    var builder: NotificationCompat.Builder
    var currentId : Int

    constructor(channelId: String, channelName: String, context: Context) {
        this.channelId = channelId
        this.builder = NotificationCompat.Builder(context, this.channelId)
        this.currentId = count++

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val notificationChannel: NotificationChannel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)

            notificationManager?.createNotificationChannel(notificationChannel)
            builder.setChannelId(channelId)
        }

    }


    fun setNotificationBuilder(title: String, content: String, priority: Int, icon: Int): MyNotification {
        builder.setContentTitle(title).setContentText(content).setPriority(priority).setSmallIcon(icon)
        return this
    }


    fun makeNotification() {
        notificationManager?.notify(currentId,builder.build())
    }

    fun setTitle(str:String):MyNotification{
        builder.setContentTitle(str)
        return this
    }

    fun setContentText(str:String):MyNotification{
        builder.setContentText(str)
        return this
    }

    fun setPriority(p:Int):MyNotification{
        builder.setPriority(p)
        return this
    }

    fun setIcon(icon:Int):MyNotification{
        builder.setSmallIcon(icon)
        return this
    }

}