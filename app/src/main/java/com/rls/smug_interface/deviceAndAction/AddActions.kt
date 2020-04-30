package com.rls.smug_interface.deviceAndAction

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.lang.Long.parseLong
import androidx.lifecycle.ViewModelProvider
import com.rls.smug_interface.MainUI
import com.rls.smug_interface.R
import com.rls.smug_interface.utilities.ColorPickerDialog
import kotlinx.android.synthetic.main.activity_add_actions.*
import kotlinx.android.synthetic.main.fragment_bar.view.*
import kotlinx.android.synthetic.main.fragment_bar.view.deviceAddr
import java.util.*
import kotlin.collections.ArrayList


class AddActions : AppCompatActivity(), ColorPickerDialog.ColorListener,
    ListDialog.ListDialogListener {
    lateinit var layout: LinearLayout
    private lateinit var viewModel: DeviceViewModel

    private val listOFViews = ArrayList<View>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_actions)
        supportActionBar!!.title = "add actions to a gesture"
        viewModel = ViewModelProvider(this).get(DeviceViewModel::class.java)

        val gestureName = intent.getStringExtra("GESTURE_NAME")
        gestureNameTxt.text = gestureName
        layout = insideLinearLayout
        val addAction = imgAddAction
        val saveBtn = saveAndApplyBtn
        val del = delActionBtn
        addAction.setOnClickListener {
            listDialog()
            println("list dialog")
        }
        saveBtn.setOnClickListener {
            //viewModel.setGestureAssociation(gestureName, )
            val devicesAddr = ArrayList<String>()
            val attr = ArrayList<String>()
            val values = ArrayList<String>()
            for (i in 0 until listOFViews.size) {
                if (!listOFViews[i].deviceName.text.toString().contains("plug")) {
                    devicesAddr.add(listOFViews[i].deviceAddr.text.toString())
                    attr.add("state")
                    if (listOFViews[i].onOffSwitch.isChecked)
                        values.add("ON")
                    else
                        values.add("OFF")
                    devicesAddr.add(listOFViews[i].deviceAddr.text.toString())
                    attr.add("brightness")
                    values.add(listOFViews[i].brightnessBar.progress.toString())

                    if (!listOFViews[i].colorText.text.contains("-1")) {
                        devicesAddr.add(listOFViews[i].deviceAddr.text.toString())
                        attr.add("color")
                        values.add(listOFViews[i].colorText.text.toString())
                    }

                } else {
                    devicesAddr.add(listOFViews[i].deviceAddr.text.toString())
                    attr.add("state")
                    if (listOFViews[i].onOffSwitch.isChecked) {
                        values.add("ON")
                    } else {
                        values.add("OFF")
                    }
                }
            }

            viewModel.setGestureAssociation(gestureName, devicesAddr, attr, values)
            finish()
            //val intent = Intent(context, MainUI::class.java)
            //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            //startActivityForResult(intent, 1)
        }
    }


    private fun adjustView(
        imgID: Int,
        text: String,
        status: Boolean,
        brightness: Int,
        addr: String
    ): View {
        val myInflater = layoutInflater
        val v: View = myInflater.inflate(R.layout.fragment_bar, null)

        listOFViews.add(v)

        val img = v.deviceIcon
        val txt = v.deviceName
        val s = v.onOffSwitch
        val bright = v.brightnessBar
        val address = v.deviceAddr
        address.text = addr
        img.setImageResource(imgID)
        txt.text = text
        s.isChecked = status
        bright.max = 255
        bright.progress = brightness
        bright.min = 20
        val colorText = v.colorText
        colorText.text = "-1"
        /*
         colorText.height = 0
         colorText.width = 0
         layout.addView(colorText)

         */
        if (imgID == R.drawable.plug) {
            bright.visibility = View.GONE
        } else {
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
            txt.setOnClickListener {
                //val intentR = Intent(baseContext, ColorHandler::class.java)
                //startActivityForResult(intentR, shortHash)
                colorPicker(v.deviceAddr.text.toString())
                println("v.hascode() = ${v.hashCode()}")
                println("${txt.text} was clicked")
                //println(colorText.text)
            }
            img.setOnClickListener {
                //val intentR = Intent(baseContext, ColorHandler::class.java)
                //startActivityForResult(intentR, shortHash)
                //colorPicker(v.hashCode())
                colorPicker(v.deviceAddr.text.toString())

                println("v.hascode() = ${v.hashCode()}")
                println("image of ${txt.text} was clicked")
            }
        }

        v.setBackgroundColor(
            Color.argb(127, 222, 222, 222)
        )


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

        return v
    }


    private fun createDeviceList(device: String, addr: String) {
        val spaceHeight = 30
        when {
            device.toLowerCase(Locale.ROOT).contains("go") -> {
                layout.addView(adjustView(R.drawable.go, device, true, 10, addr))
                val space = Space(applicationContext)
                space.minimumHeight = spaceHeight
                layout.addView(space)
            }
            device.toLowerCase(Locale.ROOT).contains("strip") -> {
                layout.addView(adjustView(R.drawable.strip, device, true, 10, addr))
                val space = Space(applicationContext)
                space.minimumHeight = spaceHeight
                layout.addView(space)
            }
            device.toLowerCase(Locale.ROOT).contains("e26") -> {
                layout.addView(adjustView(R.drawable.bulb, device, true, 10, addr))
                val space = Space(applicationContext)
                space.minimumHeight = spaceHeight
                layout.addView(space)
            }
            device.toLowerCase(Locale.ROOT).contains("plug") -> {
                layout.addView(adjustView(R.drawable.plug, device, true, 10, addr))
                val space = Space(applicationContext)
                space.minimumHeight = spaceHeight
                layout.addView(space)
            }
        }
    }


    private fun colorPicker(hashCode: String) {
        val dialog = ColorPickerDialog.newInstance(hashCode)
        dialog.show(supportFragmentManager, "color")
    }

    override fun saveColor(color: String, code: String) {
        println("color = $color")
        println("hashCode = $code")
        for (i in 0 until listOFViews.size) {
            if (code.contains(listOFViews[i].deviceAddr.text)) {
                listOFViews[i].colorText.text = color
                val color = hexToRGB(color)
                println("color= $color")
                //listOFViews[i].brightnessBar.thumb.setTint(color)
                listOFViews[i].brightnessBar.progressDrawable.setTint(color)
            }
        }
    }

    private fun hexToRGB(hexColor: String): Int {
        val color = hexColor.toCharArray()
        println(color[0])

        val r = parseLong("${color[1]}${color[2]}", 16).toInt()
        val g = parseLong("${color[3]}${color[4]}", 16).toInt()
        val b = parseLong("${color[5]}${color[6]}", 16).toInt()
        return Color.rgb(r, g, b)
        //return Color.argb(alpha, r, g, b)
    }

    private fun listDialog() {
        val dialog = ListDialog()
        dialog.show(supportFragmentManager, "listDialog")
    }

    override fun onListItem(item: String, address: String) {
        println("item= $item")
        createDeviceList(item, address)
    }
}