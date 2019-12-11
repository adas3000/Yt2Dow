package com.yt.androidytdownload

import com.yt.androidytdownload.util.GetDecFromStr
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

}

