package com.yt.androidytdownload.Model

class VideoDetails {

    private val title:String
    private val file_size:String

    constructor(title:String,file_size:String){
        this.title = title
        this.file_size = file_size
    }


    fun getTitle():String{
        return this.title
    }

    fun getfile_Size():String{
        return this.file_size
    }





}