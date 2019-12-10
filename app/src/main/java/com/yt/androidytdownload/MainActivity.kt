package com.yt.androidytdownload

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.RadioButton
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.yt.androidytdownload.enum.Kind
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {

    private var kind: Kind = Kind.MP4


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        radioGroup.check(radioButton_video.id)

        Log.d("fsystempath:",this.filesDir.path.toString())
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

        val python: Python = Python.getInstance()
        val pyObj: PyObject = python.getModule("main")


        val list: List<PyObject> = pyObj.callAttr("getList", "Hello", "Cito", "Wygladasz", "Jak", "Molotow").asList()



        for (i in list)
            Log.d("Item:", i.toString())

         val filePath:String = this.filesDir.path.toString()

        //val file : File = File(filePath+"/3 years of Computer Science in 8 minutes.mp4")

        Thread(Runnable {
            pyObj.callAttr("doDownload","https://youtu.be/ReVeUvwTGdU",filePath)
        }).start()

    }

}
