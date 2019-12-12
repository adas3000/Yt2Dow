package com.yt.androidytdownload

import com.yt.androidytdownload.util.GetDecFromStr
import com.yt.androidytdownload.util.deleteWhen
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


    }

}

