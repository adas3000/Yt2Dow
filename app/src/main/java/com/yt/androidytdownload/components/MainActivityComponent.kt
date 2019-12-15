package com.yt.androidytdownload.components

import android.content.Context
import com.yt.androidytdownload.MainActivity
import com.yt.androidytdownload.modules.MainActivityModule
import dagger.Component

@Component(modules = [MainActivityModule::class])
interface MainActivityComponent {

    fun context(): Context

    fun inject(mainActivity:MainActivity)

}