package com.yt.androidytdownload.tasks

import android.os.AsyncTask
import android.util.JsonReader
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.yt.androidytdownload.Model.VideoDetails
import com.yt.androidytdownload.util.SocketPort
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class VideoDataTask:AsyncTask<Void,Void,Void> {


    private var videoDetails:VideoDetails

    constructor(){
        this.videoDetails = VideoDetails("","")
    }

    public fun getVideoDetails():VideoDetails{
        return this.videoDetails
    }

    override fun doInBackground(vararg p0: Void?): Void {



        val socket = DatagramSocket(SocketPort.Port.port)
        var running = true
        var buffer = ByteArray(256)
        var packet = DatagramPacket(buffer, buffer.size)


        socket.receive(packet)

        val addr: InetAddress = packet.address
        val port = packet.port

        packet = DatagramPacket(buffer, buffer.size, addr, port)

        val received: String = String(packet.data, 0, packet.length)
        println("RECEIVED"+received)
        this.videoDetails = GsonBuilder().setLenient().create().fromJson(received,VideoDetails::class.java)


        socket.close()

        return Void.TYPE.newInstance()
    }
}