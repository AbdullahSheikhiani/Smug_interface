package com.rls.smug_interface.deviceAndAction

import android.bluetooth.BluetoothClass
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rls.smug_interface.R
import kotlinx.android.synthetic.main.fragment_main_ui_device.view.*


class DeviceFragment : Fragment() {

    lateinit var viewModel: DeviceViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(DeviceViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_main_ui_device, container, false)

        val layout = root.sLinear


        val listBtn = root.imageViewList
        val removeBtn = root.imageViewDel
        val addBtn = root.imageViewAdd

        val fList = listBtn.colorFilter
        val fRemove = removeBtn.colorFilter
        val fAdd = addBtn.colorFilter

        listBtn.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    removeBtn.colorFilter = fRemove
                    addBtn.colorFilter = fAdd
                }
                MotionEvent.ACTION_DOWN -> {
                    //todo logic to get devices
                    //smart plug needs different handling
                    //get address and
                    viewModel.deviceList.observe(viewLifecycleOwner, Observer {
                        println(it)
                    })
                    viewModel.getDeviceList()

                    listBtn.setColorFilter(Color.rgb(0, 0, 0))
                    val x1 = adjustView(R.drawable.bulb, "BULB", false, 100)
                    layout.addView(x1)
                    val separator = Space(context)
                    separator.minimumWidth = 0
                    separator.minimumHeight = 15
                    layout.addView(separator)
                    val x2 = adjustView(R.drawable.strip, "STrip", true, 30)
                    layout.addView(x2)
                    //layout.addView(separator)

                    /*
                    val intent = Intent(context, ActionHandler::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context?.startActivity(intent)
                     */


                }

            }
            true
        }
        removeBtn.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    listBtn.colorFilter = fList
                    addBtn.colorFilter = fAdd
                }
                MotionEvent.ACTION_DOWN -> {
                    removeBtn.setColorFilter(Color.rgb(0, 0, 0))
                    //todo my stuff
                    layout.removeAllViewsInLayout()

                }

            }
            true
        }
        addBtn.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    listBtn.colorFilter = fList
                    removeBtn.colorFilter = fAdd
                }
                MotionEvent.ACTION_DOWN -> {
                    addBtn.setColorFilter(Color.rgb(0, 0, 0))
                    //todo my stuff

                }

            }
            true
        }

        return root
    }

    private fun adjustView(imgID: Int, text: String, status: Boolean, brightness: Int): View {
        //todo add listeners
        val myInflater = requireActivity().layoutInflater
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
        return v
    }
}
