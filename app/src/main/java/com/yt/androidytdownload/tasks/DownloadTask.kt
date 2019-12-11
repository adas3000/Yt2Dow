package com.yt.androidytdownload.tasks

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.yt.androidytdownload.util.GetDecFromStr
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class DownloadTask : AsyncTask<String, String, Void> {


    private val context: Context
    private val progressBar: ProgressBar

    constructor(context: Context, progressBar: ProgressBar) {
        this.context = context
        this.progressBar = progressBar
    }

    override fun onPreExecute() {
        super.onPreExecute()
        progressBar.visibility = View.VISIBLE
    }

    override fun doInBackground(vararg p0: String?): Void? {

        val socket: DatagramSocket = DatagramSocket(5005)
        var running: Boolean = true
        var buffer = ByteArray(256)
        var packet: DatagramPacket = DatagramPacket(buffer, buffer.size)

        while (running) {

            socket.receive(packet)

            val addr: InetAddress = packet.address
            val port = packet.port

            packet = DatagramPacket(buffer, buffer.size, addr, port)
            val received: String = String(packet.data, 0, packet.length)

            if (received.length<5)
                Log.d("Msg:",received)
            else
                publishProgress(received)

            if (received.contains("100%")) {
                running = false
            }
        }
        socket.close()

        return null
    }

    override fun onProgressUpdate(vararg values: String?) {
        if (values[0] != null) {
            Log.d("Recived:", values[0])
            progressBar.progress = GetDecFromStr(values[0].toString())
        }
    }


    override fun onPostExecute(result: Void?) {
        Toast.makeText(context, "Downloaded!", Toast.LENGTH_LONG).show()
        progressBar.visibility = View.INVISIBLE
        progressBar.progress=0
    }
}