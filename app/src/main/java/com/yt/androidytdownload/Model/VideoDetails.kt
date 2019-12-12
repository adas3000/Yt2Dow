package com.yt.androidytdownload.Model

import com.fasterxml.jackson.annotation.JsonProperty

class VideoDetails {

    @JsonProperty(value = "title")
    var title: String


    @JsonProperty(value = "file_size")
    var file_size: String


    constructor(title: String, file_size: String) {
        this.title = title
        this.file_size = file_size
    }


}