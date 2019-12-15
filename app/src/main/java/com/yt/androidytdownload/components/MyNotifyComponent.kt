package com.yt.androidytdownload.components

import com.yt.androidytdownload.MainActivity
import dagger.Component

@Component
interface MyNotifyComponent {

    fun inject(mainActivity: MainActivity)

}