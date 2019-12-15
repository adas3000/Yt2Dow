package com.yt.androidytdownload.modules

import androidx.core.app.NotificationCompat
import com.yt.androidytdownload.R
import com.yt.androidytdownload.util.ContextKeeper
import com.yt.androidytdownload.util.MyNotification
import dagger.Module
import dagger.Provides

@Module
object MyNotifyModule {


    @JvmStatic
    @Provides
    fun notification():MyNotification{

        val channelId = "com.yt.androidyt.download.channel"
        val channelName = "Yt2DowChannel"

        return MyNotification(channelId,channelName,ContextKeeper.context)
            .setIcon(R.mipmap.ic_yt_icon_foreground)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    }



}