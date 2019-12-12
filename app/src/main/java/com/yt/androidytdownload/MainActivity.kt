package com.yt.androidytdownload

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.yt.androidytdownload.enum.Kind
import com.yt.androidytdownload.tasks.DownloadTask
import com.yt.androidytdownload.tasks.ValidTask
import com.yt.androidytdownload.util.MyNotification
import kotlinx.android.synthetic.main.activity_main.*
import android.os.StrictMode



class MainActivity : AppCompatActivity() {

    private var kind: Kind = Kind.MP4
    var hasPermissions = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        radioGroup.check(radioButton_video.id)
        progressBar.visibility = View.INVISIBLE
        progressBar_circle.visibility = View.INVISIBLE


        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "GRANTED", LENGTH_LONG).show()
            hasPermissions = true

        } else
            Toast.makeText(this, "NOT GRANTED", LENGTH_LONG).show()

        if (Build.VERSION.SDK_INT >= 24) {
            try {
                val m = StrictMode::class.java.getMethod("disableDeathOnFileUriExposure")
                m.invoke(null)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        MyNotification.notificationManager = notificationManager
    }

    fun onRadioButtonClick(view: View) {


        val radio: RadioButton = findViewById(radioGroup.checkedRadioButtonId)

        when (radio.id) {
            R.id.radioButton_audio -> {
                kind = Kind.MP3
            }
            R.id.radioButton_video -> {
                kind = Kind.MP4
            }
        }

    }


    fun onDownloadClick(view: View) {

        var notification:MyNotification=MyNotification("com.yt.androidyt.download.channel","androidytdownloads",this)
        notification.setIcon(R.mipmap.ic_launcher).setPriority(NotificationCompat.PRIORITY_DEFAULT)


        if (!hasPermissions) {
            Toast.makeText(this, "No permission to write in storage.Set permission in settins.", Toast.LENGTH_LONG)
                .show()
            return
        }

        val url: String = editText.text.toString()
        val kindstr = kind.toString().toLowerCase()

        val downloadTask: DownloadTask = DownloadTask(this, notification, button_download)

        val validTask: ValidTask =
            ValidTask(this, downloadTask, progressBar_circle, Python.getInstance(), "main", url, kindstr)
        validTask.execute()

    }


}
