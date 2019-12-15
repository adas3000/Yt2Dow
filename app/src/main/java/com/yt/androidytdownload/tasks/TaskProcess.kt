package com.yt.androidytdownload.tasks

import com.yt.androidytdownload.Model.VideoDetails
import com.yt.androidytdownload.enum.Kind
import com.yt.androidytdownload.factory.TaskFactory
import com.yt.androidytdownload.util.ContextKeeper
import com.yt.androidytdownload.util.PortKeeper

class TaskProcess {

    val taskFactory: TaskFactory

    constructor(taskFactory: TaskFactory) {
        this.taskFactory = taskFactory
    }

    fun doAction(url: String, kindstr: Kind, type: String,videoDetails: VideoDetails=VideoDetails("","","") ,
                 convertToMp3: Boolean = false,port:Int = PortKeeper.getNextPort()) {

        val task: AbstractTask = taskFactory.createTask(url, kindstr, type,videoDetails ,convertToMp3,port)
        ContextKeeper.downloadQueueEmpty = false

        task.doExecute()
    }

}