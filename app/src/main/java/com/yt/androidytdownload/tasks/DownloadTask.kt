package com.yt.androidytdownload.tasks

import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.Toast
import com.yt.androidytdownload.Model.VideoDetails
import com.yt.androidytdownload.enum.SocketResult
import com.yt.androidytdownload.util.*
import java.io.File
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class DownloadTask : AsyncTask<String, String, SocketResult> {


    var videoDetails:VideoDetails = VideoDetails("","","")
    val notification:MyNotification
    val downloadButton:Button

    constructor(notification: MyNotification,downloadButton:Button) {
        this.notification = notification
        this.downloadButton = downloadButton
    }



    override fun onPreExecute() {
        notification.setTitle(videoDetails.title)
        notification.setContentText("Size(MB):"+videoDetails.file_size)
        notification.builder.setProgress(100,0,false)
        notification.makeNotification()
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
                received.contains("100%") -> {publishProgress(received);running = false}
                received.matches(".*\\d.*".toRegex()) -> publishProgress(received) //check whether has some decimals
            }


        }
        socket.close()

        return SocketResult.SUCCESS
    }

    override fun onProgressUpdate(vararg values: String?) {
        if (values[0] != null) {
            Log.d("Received:", values[0].toString())
            notification.builder.setProgress(100,GetDecFromStr(values[0].toString()),false)
            notification.makeNotification()
        }
    }


    override fun onPostExecute(result: SocketResult?) {

        notification.builder.setContentText("")
            .setProgress(100, 100, true)


      //  val convertTask:Mp4ToMp3Task = Mp4ToMp3Task(videoDetails,notification,1000)



        startConvertion("-i",videoDetails.file_path,videoDetails.file_path.replace(".mp4",".mp3"))



       // convertTask.execute("-i",videoDetails.file_path,saveIn)

/*
        val file:File = File(videoDetails.file_path)
        val map:MimeTypeMap = MimeTypeMap.getSingleton()
        val ext:String = MimeTypeMap.getFileExtensionFromUrl(file.name)
        var type:String? = map.getMimeTypeFromExtension(ext)

        if(type==null) type =/* "*/*"

        val intent:Intent = Intent(Intent.ACTION_VIEW)
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        val uri:Uri = Uri.fromFile(file)

        intent.setDataAndType(uri,type)
        val pendingIntent:PendingIntent = PendingIntent.getActivity(ContextKeeper.context,0,intent,0)
        notification.builder.setContentIntent(pendingIntent)
        notification.makeNotification()
*/

        if (result != null && result!=SocketResult.SUCCESS)
                Toast.makeText(ContextKeeper.context, "Error occurred check whether url is valid.", Toast.LENGTH_LONG).show()

        downloadButton.isClickable = true
    }
}