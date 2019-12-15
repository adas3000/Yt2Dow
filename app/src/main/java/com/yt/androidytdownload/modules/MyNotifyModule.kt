package com.yt.androidytdownload.modules

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
    }



}