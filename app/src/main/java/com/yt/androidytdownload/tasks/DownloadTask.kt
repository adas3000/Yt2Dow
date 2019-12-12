package com.yt.androidytdownload.tasks

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import com.yt.androidytdownload.enum.SocketResult
import com.yt.androidytdownload.util.GetDecFromStr
import com.yt.androidytdownload.util.SocketPort
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class DownloadTask : AsyncTask<String, String, SocketResult> {


    private val context: Context
    private val progressBar: ProgressBar
    private val downloadButton:Button

    constructor(context: Context, progressBar: ProgressBar,downloadButton:Button) {
        this.context = context
        this.progressBar = progressBar
        this.downloadButton = downloadButton
    }



    override fun onPreExecute() {
        progressBar.visibility = View.VISIBLE
    }

    override fun doInBackground(vararg p0: String?): SocketResult? {

        val socket: DatagramSocket = DatagramSocket(SocketPort.Port.port)
        var running: Boolean = true
        var buffer = ByteArray(256)
        var packet: DatagramPacket = DatagramPacket(buffer, buffer.size)


        while (running) {

            socket.receive(packet)

            val addr: InetAddress = packet.address
            val port = packet.port

            packet = DatagramPacket(buffer, buffer.size, addr, port)
            val received: String = String(packet.data, 0, packet.length)


            when {
                received.contains("100%") -> running = false
                received.matches(".*\\d.*".toRegex()) -> publishProgress(received) //check whether has some decimals
            }


        }
        socket.close()

        return SocketResult.SUCCESS
    }

    override fun onProgressUpdate(vararg values: String?) {
        if (values[0] != null) {
            Log.d("Received:", values[0].toString())
            progressBar.progress = GetDecFromStr(values[0].toString())
        }
    }


    override fun onPostExecute(result: SocketResult?) {

        if (result != null) {
            if (result==SocketResult.SUCCESS)
                Toast.makeText(context, "Downloaded!", Toast.LENGTH_LONG).show()
             else
                Toast.makeText(context, "Error occurred check whether url is valid.", Toast.LENGTH_LONG).show()
        }
        progressBar.visibility = View.INVISIBLE
        progressBar.progress = 0
        downloadButton.isClickable = true
    }
}