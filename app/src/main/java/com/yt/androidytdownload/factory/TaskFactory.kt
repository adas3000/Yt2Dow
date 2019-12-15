package com.yt.androidytdownload.factory

import android.content.Context
import android.widget.Button
import android.widget.ProgressBar
import androidx.core.app.NotificationCompat
import com.chaquo.python.Python
import com.yt.androidytdownload.Model.VideoDetails
import com.yt.androidytdownload.R
import com.yt.androidytdownload.components.DaggerMyNotifyComponent
import com.yt.androidytdownload.components.MyNotifyComponent
import com.yt.androidytdownload.enums.Kind
import com.yt.androidytdownload.tasks.AbstractTask
import com.yt.androidytdownload.tasks.DownloadTask
import com.yt.androidytdownload.tasks.ValidTask
import com.yt.androidytdownload.util.ContextKeeper
import com.yt.androidytdownload.util.MyNotification
import java.lang.IllegalArgumentException
import javax.inject.Inject

class TaskFactory {

    val downloadButton: Button
    val progressBar: ProgressBar
    val python: Python
    val moduleName:String
    val notification:MyNotification

    constructor(downloadButton: Button, progressBar: ProgressBar,notification: MyNotification, python: Python= Python.getInstance(), mainName:String="main"){
        this.downloadButton = downloadButton
        this.progressBar = progressBar
        this.python = python
        this.moduleName =mainName
        this.notification = notification
    }


    fun createTask(url:String,kindstr:Kind,type:String,videoDetails: VideoDetails,convertToMp3:Boolean = false,port:Int):AbstractTask{

        val type_Str = type.toLowerCase()

        val task:AbstractTask

        if(type_Str.equals("validtask")){

            task = ValidTask(progressBar,python,moduleName,url,kindstr,port)

        }
        else if(type_Str.equals("downloadtask")){
            task = DownloadTask(notification, downloadButton,port,kindstr,videoDetails ,convertToMp3)
        }
        else throw IllegalArgumentException("No such task")


        return task
    }

}