package com.yt.androidytdownload

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import com.yt.androidytdownload.Model.VideoDetails
import org.junit.Assert
import org.junit.Test

class StringToClassTest {

    @Test
    fun checkObjectisCorrect(){

        val str:String = "{\"title\": \"Android Tutorial (Kotlin) - 16 - Fragment\", \"file_size\": 40684349}"


        val objMapper: ObjectMapper = ObjectMapper()
        val title = Gson().fromJson(str,VideoDetails::class.java)

        Assert.assertEquals("Android Tutorial (Kotlin) - 16 - Fragment",title.getTitle())

    }

}