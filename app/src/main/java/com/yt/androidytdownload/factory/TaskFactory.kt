package com.yt.androidytdownload.factory

import android.widget.Button
import android.widget.ProgressBar
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.yt.androidytdownload.tasks.AbstractTask
import com.yt.androidytdownload.tasks.ValidTask
import java.lang.IllegalArgumentException

class TaskFactory {

    val downloadButton: Button
    val progressBar: ProgressBar
    val python: Python
    val pyObj: PyObject

    constructor(downloadButton: Button, progressBar: ProgressBar, python: Python= Python.getInstance(), mainName:String="main"){
        this.downloadButton = downloadButton
        this.progressBar = progressBar
        this.python = python
        this.pyObj = python.getModule(mainName)
    }


    fun createTask(type:String,convertToMp3:Boolean = false):AbstractTask{

        val type_Str = type.toLowerCase()

        val task:AbstractTask

        if(type_Str.equals("validtask")){

            task = ValidTask()

        }
        else if(type_Str.equals("downloadtask")){

        }
        else throw IllegalArgumentException("No such task")


        return task
    }


}