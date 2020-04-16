package com.rls.smug_interface.deviceAndAction

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.rls.smug_interface.R
import kotlinx.android.synthetic.main.activity_add_actions.*
import androidx.lifecycle.Observer
import java.util.*
import kotlin.collections.ArrayList

class AddActions : AppCompatActivity() {
    lateinit var layout: LinearLayout
    lateinit var viewModel: DeviceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_actions)
        //actionBar.title = "add شسمه"
        supportActionBar!!.title = "add شسمه"
        viewModel = ViewModelProvider(this).get(DeviceViewModel::class.java)

        val gestureName = intent.getStringExtra("GESTURE_NAME")
        gestureNameTxt.text = gestureName
        layout = insideLinearLayout
        val addAction = imgAddAction
        val saveBtn = saveAndApllyBtn
        val del = delActionBtn
        viewModel.getDeviceList()
        var deviceList = ArrayList<String>()
        viewModel.deviceList.observe(this, Observer {
            deviceList = it
            //todo add listner
            //pop a dialog?
            createDeviceList(it)
            createDeviceList(it)
            createDeviceList(it)
        })
    }


    private fun adjustView(imgID: Int, text: String, status: Boolean, brightness: Int): View {
        //todo add listeners
        val myInflater = layoutInflater
        val v: View = myInflater.inflate(R.layout.fragment_bar, null)
        val img = v.findViewById<ImageView>(R.id.deviceIcon)
        val txt = v.findViewById<TextView>(R.id.deviceName)
        val s = v.findViewById<Switch>(R.id.onOffSwitch)
        val bright = v.findViewById<SeekBar>(R.id.brightnessBar)
        img.setImageResource(imgID)
        txt.text = text
        s.isChecked = status
        bright.max = 255
        bright.progress = brightness
        v.setBackgroundColor(
            Color.argb(127, 222, 222, 222)
        )
        //todo pop color handler dialog or start activity for result
        txt.setOnClickListener {
            println("${txt.text} was clicked")
        }
        img.setOnClickListener {
            println("image of ${txt.text} was clicked")
        }

        s.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                //we can use text to get current device name
                println(text)
                println("checked")
                //buttonView.isChecked = false
            } else {
                println("not checked")
            }
        }
        bright.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                println("seekbar Value ${seekBar.progress}")
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onProgressChanged(
                seekBar: SeekBar,
                progress: Int,
                fromUser: Boolean
            ) {

            }
        })
        return v
    }

    private fun createDeviceList(devices: ArrayList<String>) {
        for (i in devices) {
            when {
                i.toLowerCase(Locale.ROOT).contains("go") -> {
                    layout.addView(adjustView(R.drawable.go, i, false, 0))
                    val space = Space(applicationContext)
                    space.minimumHeight = 15
                    layout.addView(space)
                }
                i.toLowerCase(Locale.ROOT).contains("strip") -> {
                    layout.addView(adjustView(R.drawable.strip, i, false, 0))
                    val space = Space(applicationContext)
                    space.minimumHeight = 15
                    layout.addView(space)
                }
                i.toLowerCase(Locale.ROOT).contains("e26") -> {
                    layout.addView(adjustView(R.drawable.bulb, i, false, 0))
                    val space = Space(applicationContext)
                    space.minimumHeight = 15
                    layout.addView(space)
                }
            }
        }
    }
}