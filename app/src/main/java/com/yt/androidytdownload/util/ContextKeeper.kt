package com.yt.androidytdownload.util

import android.content.Context
import com.yt.androidytdownload.tasks.TaskProcess

class ContextKeeper {

    companion object{
        lateinit var context : Context
        var downloadQueueEmpty = true
        lateinit var taskProcess:TaskProcess
    }



}