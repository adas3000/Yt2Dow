package com.yt.androidytdownload

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.yt.androidytdownload.enum.Kind
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var kind: Kind = Kind.MP4


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        radioGroup.check(radioButton_video.id)

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

        val list:List<PyObject> = pyObj.callAttr("new_list","Hello","Cito","Wygladasz","Jak","Molotow").asList()

        for(i in list)
            Log.d("Item:",i.toString())


        // pyObj.callAttr("doDownload","https://www.youtube.com/watch?v=pXdY1B-KVJg",true)
    }

}
