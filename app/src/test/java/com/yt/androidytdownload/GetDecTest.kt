package com.yt.androidytdownload

import com.google.gson.Gson
import com.yt.androidytdownload.Model.VideoDetails
import com.yt.androidytdownload.util.*
import org.junit.Assert
import org.junit.Test
import org.junit.Assert.*

class GetDecTest {

    @Test
    fun getDec_isCorrect() {


        assertEquals(123, GetDecFromStr("123"))
        assertEquals(100, GetDecFromStr("Downloaded 100%"))
        assertEquals(100, GetDecFromStr("Downloaded100%"))
        assertEquals(1, GetDecFromStr("Downloaded1%"))
        assertEquals(11, GetDecFromStr("Downloaded11%"))

    }


    @Test
    fun deleteWhenTest() {
        val string =
            "{\"title\": \"Konrad \\ud83c\\udd9a Kurian \\ud83c\\udfa4 WBW 2019 Fina\\u0142 (freestyle rap battle)\", \"file_size\": 31.760437}���������������������������������������������������������������������������������������������������������������������������������������������"

        assertEquals(
            "{\"title\": \"Konrad \\ud83c\\udd9a Kurian \\ud83c\\udfa4 WBW 2019 Fina\\u0142 (freestyle rap battle)\", \"file_size\": 31.760437}",
            deleteWhen(string, '}')
        )

        val title = Gson().fromJson(deleteWhen(string, '}'), VideoDetails::class.java)
    }


    @Test
    fun check() {
        strToJson(
            "{\"title\": \"Konrad \\ud83c\\udd9a Kurian \\ud83c\\udfa4 WBW 2019 Fina\\u0142 (freestyle rap battle)\", \"file_size\": 31.760437}",
            VideoDetails::class.java
        )
    }

    @Test
    fun ifEqualsAndNoExceptionThenOk() {

        val str1 = " Progress:size=    2456kB time=00:02:37.15 bitrate= 128.0kbits/s speed=1.48x"
        val str2 = "Progress:size=    2323kB time=00:02:28.63 bitrate= 128.0kbits/s speed=1.48x"
        val str3 = "Progress:size=      10kB time=00:00:00.60 bitrate= 137.9kbits/s speed=1.16x"

        assertEquals(2456, parseFFMpegOnProgressStr(str1))
        assertEquals(2323, parseFFMpegOnProgressStr(str2))
        assertEquals(10, parseFFMpegOnProgressStr(str3))

    }

    @Test
    fun ifEqualsThenOk(){
        assertEquals("Lucky Luke  LYD Like You Do  Future House", cutChars("Lucky Luke - LYD (Like You Do) || Future House"))
        assertEquals("moj", cutChars("moją"))
    }


}

