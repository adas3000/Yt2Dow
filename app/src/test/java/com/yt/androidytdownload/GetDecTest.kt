package com.yt.androidytdownload

import com.google.gson.Gson
import com.yt.androidytdownload.Model.VideoDetails
import com.yt.androidytdownload.util.GetDecFromStr
import com.yt.androidytdownload.util.deleteWhen
import com.yt.androidytdownload.util.strToJson
import org.junit.Assert
import org.junit.Test
import org.junit.Assert.*

class GetDecTest {

    @Test
    fun getDec_isCorrect(){


        assertEquals(123, GetDecFromStr("123"))
        assertEquals(100, GetDecFromStr("Downloaded 100%"))
        assertEquals(100, GetDecFromStr("Downloaded100%"))
        assertEquals(1, GetDecFromStr("Downloaded1%"))
        assertEquals(11, GetDecFromStr("Downloaded11%"))

    }


    @Test
    fun deleteWhenTest(){
        val string = "{\"title\": \"Konrad \\ud83c\\udd9a Kurian \\ud83c\\udfa4 WBW 2019 Fina\\u0142 (freestyle rap battle)\", \"file_size\": 31.760437}���������������������������������������������������������������������������������������������������������������������������������������������"

        assertEquals("{\"title\": \"Konrad \\ud83c\\udd9a Kurian \\ud83c\\udfa4 WBW 2019 Fina\\u0142 (freestyle rap battle)\", \"file_size\": 31.760437}",
            deleteWhen(string,'}')
        )

        val title = Gson().fromJson(deleteWhen(string,'}'),VideoDetails::class.java)
    }


    @Test
    fun check(){
        strToJson("{\"title\": \"Konrad \\ud83c\\udd9a Kurian \\ud83c\\udfa4 WBW 2019 Fina\\u0142 (freestyle rap battle)\", \"file_size\": 31.760437}",VideoDetails::class.java)
    }

}

