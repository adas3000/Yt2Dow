package com.yt.androidytdownload.factory

import android.app.Notification
import android.content.Context
import android.widget.Button
import android.widget.ProgressBar
import androidx.core.app.NotificationCompat
import com.chaquo.python.Python
import com.yt.androidytdownload.R
import com.yt.androidytdownload.enum.Kind
import com.yt.androidytdownload.tasks.AbstractTask
import com.yt.androidytdownload.tasks.DownloadTask
import com.yt.androidytdownload.tasks.ValidTask
import com.yt.androidytdownload.util.MyNotification
import com.yt.androidytdownload.util.PortKeeper
import java.lang.IllegalArgumentException

class TaskFactory {

    val downloadButton: Button
    val progressBar: ProgressBar
    val python: Python
    val moduleName:String
    val context:Context

    constructor(downloadButton: Button, progressBar: ProgressBar,context: Context, python: Python= Python.getInstance(), mainName:String="main"){
        this.downloadButton = downloadButton
        this.progressBar = progressBar
        this.python = python
        this.moduleName =mainName
        this.context = context
    }


    fun createTask(url:String,kindstr:Kind,type:String,convertToMp3:Boolean = false):AbstractTask{

        val type_Str = type.toLowerCase()

        val task:AbstractTask

        if(type_Str.equals("validtask")){

            task = ValidTask(progressBar,python,moduleName,url,kindstr,PortKeeper.getNextPort())

        }
        else if(type_Str.equals("downloadtask")){

            val notify = MyNotification("com.yt.androidyt.download.channel", "androidytdownloadsChannel",context)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setIcon(R.mipmap.ic_yt_icon_foreground)

            task = DownloadTask(notify, downloadButton,PortKeeper.getNextPort(),kindstr, convertToMp3)
        }
        else throw IllegalArgumentException("No such task")


        return task
    }

}