package com.yt.androidytdownload.tasks

import com.yt.androidytdownload.enum.Kind
import com.yt.androidytdownload.factory.TaskFactory

class TaskProcess {

    val taskFactory: TaskFactory

    constructor(taskFactory: TaskFactory) {
        this.taskFactory = taskFactory
    }

    fun doAction(url: String, kindstr: Kind, type: String, convertToMp3: Boolean = false) {

        val task: AbstractTask = taskFactory.createTask(url, kindstr, type, convertToMp3)

        task.doExecute()
    }

}