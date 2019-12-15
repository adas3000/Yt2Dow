package com.yt.androidytdownload.components

import com.yt.androidytdownload.MainActivity
import com.yt.androidytdownload.modules.MyNotifyModule
import dagger.Component

@Component(modules = [MyNotifyModule::class])
interface MyNotifyComponent {

    fun inject(mainActivity: MainActivity)

}