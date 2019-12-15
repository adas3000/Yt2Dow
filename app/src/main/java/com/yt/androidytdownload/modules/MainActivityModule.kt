package com.yt.androidytdownload.modules

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class MainActivityModule {

    val context:Context

    constructor(context: Context) {
        this.context = context
    }

    @Provides
    fun context():Context{
        return context
    }


}