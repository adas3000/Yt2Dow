package com.yt.androidytdownload.util

class PortKeeper {

    companion object{
        val PortFrom:Int = 5005
        var currentPort:Int = PortFrom

        fun getNextPort():Int{
            return currentPort++
        }

    }


}