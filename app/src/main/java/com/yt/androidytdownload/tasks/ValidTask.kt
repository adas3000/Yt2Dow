package com.yt.androidytdownload.tasks

import android.content.Context
import android.os.AsyncTask
import android.widget.Toast
import com.yt.androidytdownload.util.SocketPort
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class ValidTask : AsyncTask<Void, Void, Boolean> {


    private val context:Context
    private var valid:Boolean

    constructor(context: Context){
        this.context = context
        this.valid = false
    }


    override fun doInBackground(vararg p0: Void?): Boolean {


        val socket = DatagramSocket(SocketPort.Port.port)
        var buffer = ByteArray(256)
        var packet = DatagramPacket(buffer, buffer.size)

        socket.receive(packet)

        val addr: InetAddress = packet.address
        val port = packet.port

        packet = DatagramPacket(buffer, buffer.size, addr, port)
        val received: String = String(packet.data, 0, packet.length)

        if (received.contains("error")) {
            socket.close()
            return false
        }

        socket.close()

        return true
    }

    override fun onPostExecute(result: Boolean?) {

        if(result!=null){
            this.valid = result
        }
        else
            Toast.makeText(context,"Error-Result value is null",Toast.LENGTH_LONG).show()

    }

    fun getValid():Boolean{
        return true
    }

}