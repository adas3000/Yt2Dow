package com.yt.androidytdownload.Model

import com.fasterxml.jackson.annotation.JsonProperty

class VideoDetails {

    @JsonProperty(value = "title")
    private var title:String

    @JsonProperty(value = "file_size")
    private var file_size:String

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

    fun setTitle(title:String){
        this.title = title
    }

    fun setfile_Size(file_size: String){
        this.file_size = file_size
    }




}