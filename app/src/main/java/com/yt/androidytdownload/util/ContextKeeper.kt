package com.yt.androidytdownload.util

import android.content.Context
import com.yt.androidytdownload.tasks.TaskProcess

class ContextKeeper {

    companion object{
        var context : Context? = null
        var downloadQueueEmpty = true
        var taskProcess:TaskProcess? = null
    }



}