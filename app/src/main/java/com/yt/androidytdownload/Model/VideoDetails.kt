package com.yt.androidytdownload.Model

import com.fasterxml.jackson.annotation.JsonProperty

class VideoDetails {

    @JsonProperty(value = "title")
    var title: String


    @JsonProperty(value = "file_size")
    var file_size: String

    @JsonProperty(value = "file_path")
    var file_path: String

    constructor(title: String, file_size: String, file_path: String) {
        this.title = title
        this.file_size = file_size
        this.file_path = file_path
    }


}