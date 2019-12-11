package com.yt.androidytdownload.tasks

import android.os.AsyncTask
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


        socket.close()

        return Void.TYPE.newInstance()
    }
}