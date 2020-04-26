package com.rls.smug_interface.deviceAndAction

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rls.smug_interface.InitialSetup
import com.rls.smug_interface.R
import com.rls.smug_interface.utilities.ColorPickerDialog
import kotlinx.android.synthetic.main.fragment_bar.*
import kotlinx.android.synthetic.main.fragment_bar.view.*
import kotlinx.android.synthetic.main.fragment_bar.view.deviceName
import kotlinx.android.synthetic.main.fragment_main_ui_device.view.*
import java.util.*
import kotlin.collections.ArrayList


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
        val actionBtn = root.imageViewAction

        val fList = listBtn.colorFilter
        val fRemove = removeBtn.colorFilter
        val fAdd = addBtn.colorFilter
        val fAction = actionBtn.colorFilter

        listBtn.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    removeBtn.colorFilter = fRemove
                    addBtn.colorFilter = fAdd
                    actionBtn.colorFilter = fAction
                }
                MotionEvent.ACTION_DOWN -> {
                    listBtn.setColorFilter(Color.rgb(0, 0, 0))
                    //get address and test listener
                    layout.removeAllViewsInLayout()
                    viewModel.getDeviceList(1)
                    viewModel.deviceList.observe(viewLifecycleOwner, Observer { deviceName ->
                        println(deviceName)
                        viewModel.getDeviceAddrList(1)
                        viewModel.deviceAddrList.observe(
                            viewLifecycleOwner,
                            Observer { deviceAddrList ->
                                println(deviceAddrList)
                                viewModel.getDeviceStates(deviceAddrList, deviceName)
                                viewModel.deviceStates.observe(viewLifecycleOwner, Observer {
                                    println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAXXXXXXXXXXXXXXXXXXX\n")
                                    createDeviceList(deviceName, deviceAddrList, it, layout)
                                })
                            })
                    })
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
                    actionBtn.colorFilter = fAction
                }
                MotionEvent.ACTION_DOWN -> {
                    removeBtn.setColorFilter(Color.rgb(0, 0, 0))
                    layout.removeAllViewsInLayout()
                    viewModel.getDeviceList(2)
                    viewModel.deviceList2.observe(viewLifecycleOwner, Observer { nameList ->
                        println(nameList)
                        viewModel.getDeviceAddrList(2)
                        viewModel.deviceAddrList2.observe(
                            viewLifecycleOwner,
                            Observer { addresses ->
                                println(nameList)
                                layout.removeAllViewsInLayout()
                                for (i in 0 until nameList.size) {
                                    println("deviceListArray: $nameList")
                                    println("deviceAddr: $addresses")
                                    val vi = View.inflate(context, R.layout.fragment_bar, null)
                                    val img = vi.deviceIcon
                                    val deviceNameTxt = vi.deviceName
                                    val deviceAddr = vi.deviceAddr
                                    val s = vi.onOffSwitch
                                    val bright = vi.brightnessBar
                                    deviceNameTxt.text = nameList[i]
                                    print("xxxxxxxx")
                                    s.visibility = View.INVISIBLE
                                    bright.visibility = View.INVISIBLE
                                    deviceAddr.text = nameList[i]
                                    //todo listener
                                    deviceNameTxt.setOnClickListener {
                                        viewModel.removeDevice(deviceAddr.text.toString())

                                    }
                                    img.setOnClickListener {
                                        viewModel.removeDevice(deviceAddr.text.toString())
                                    }

                                    vi.setBackgroundColor(Color.rgb(222, 222, 222))
                                    when {
                                        nameList[i].toLowerCase(Locale.ROOT)
                                            .contains("e26") -> {
                                            img.setImageResource(R.drawable.bulb)
                                        }
                                        nameList[i].toLowerCase(Locale.ROOT)
                                            .contains("strip") -> {
                                            img.setImageResource(R.drawable.strip)
                                        }
                                        nameList[i].toLowerCase(Locale.ROOT)
                                            .contains("go") -> {
                                            img.setImageResource(R.drawable.go)
                                        }
                                        nameList[i].toLowerCase(Locale.ROOT)
                                            .contains("plug") -> {
                                            img.setImageResource(R.drawable.plug)
                                        }
                                    }
                                    val space = Space(context)
                                    space.minimumHeight = 30
                                    layout.addView(vi)
                                    layout.addView(space)
                                }
                                //create device list
                                //add to layout
                            }
                        )
                    })

                    //list devices
                    //viewModel.removeDevice()
                    //layout.removeAllViewsInLayout()
                }
            }
            true
        }
        addBtn.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    listBtn.colorFilter = fList
                    removeBtn.colorFilter = fAdd
                    actionBtn.colorFilter = fAction

                }
                MotionEvent.ACTION_DOWN -> {
                    addBtn.setColorFilter(Color.rgb(0, 0, 0))

                    viewModel.addDevice()
                    //beautify
                    //val intent = Intent(context, InitialSetup::class.java)
                    //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    //startActivityForResult(intent, 1)

                }

            }
            true
        }

        actionBtn.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    listBtn.colorFilter = fList
                    removeBtn.colorFilter = fAdd
                    addBtn.colorFilter = fAction

                }
                MotionEvent.ACTION_DOWN -> {
                    actionBtn.setColorFilter(Color.rgb(0, 0, 0))
                    //get unlinked gesture list
                    //go to new activity to set up
                    //val mInflater = requireActivity().layoutInflater
                    // val view = mInflater.inflate(R.layout.fragment_list, null)
                    val view = View.inflate(context, R.layout.fragment_list, null)
                    val list = view.findViewById<ListView>(R.id.listView)
                    //val a = ArrayList<String>()
                    viewModel.getListOfUnLinkedGestures()
                    viewModel.gestureList.observe(viewLifecycleOwner, Observer {
                        val adapter = ArrayAdapter(
                            context!!, // Context
                            android.R.layout.simple_expandable_list_item_1, // Layout
                            it // Array
                        )
                        list.adapter = adapter
                        list.setOnItemClickListener { _, _, position, _ ->
                            //start new activity
                            val item = adapter.getItem(position)
                            println(item)
                            val intentR = Intent(context, AddActions::class.java)
                            intentR.putExtra("GESTURE_NAME", adapter.getItem(position))
                            startActivityForResult(intentR, 1)
                        }
                        layout.removeAllViews()
                        layout.addView(view)
                    })
                }
            }
            true
        }
        return root
    }

    private fun adjustView(
        imgID: Int,
        text: String,
        status: Boolean,
        brightness: Int,
        deviceAddress: String,
        color: String
    ): View {
        //todo add listeners

        // val myInflater = requireActivity().layoutInflater
        // val v: View = myInflater.inflate(R.layout.fragment_bar, null)
        val v = View.inflate(context, R.layout.fragment_bar, null)
        val img = v.deviceIcon
        val deviceNameTxt = v.deviceName
        val deviceAddr = v.deviceAddr
        val s = v.onOffSwitch
        val bright = v.brightnessBar
        val txt = v.deviceName
        deviceNameTxt.text = text
        deviceAddr.text = deviceAddress
        s.isChecked = status
        bright.max = 255
        bright.progress = brightness
        v.setBackgroundColor(
            Color.argb(127, 222, 222, 222)
        )
        if (imgID == -1) {
            img.setImageResource(R.drawable.plug)
            bright.setOnTouchListener { _, _ ->
                true
            }
            bright.visibility = View.INVISIBLE
            //bright.setOnTouchListener()
        } else {

            img.setImageResource(imgID)
            img.setOnClickListener {
                println("device address $deviceAddress")
                colorPicker(v.hashCode())
                println("v.hascode() = ${v.hashCode()}")
                println("image of ${txt.text} was clicked")
            }
            txt.setOnClickListener {
                //val intentR = Intent(baseContext, ColorHandler::class.java)
                //startActivityForResult(intentR, shortHash)
                colorPicker(v.hashCode())
                println("v.hascode() = ${v.hashCode()}")
                println("${txt.text} was clicked")
                //println(colorText.text)
            }

            bright.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    println("seekbar Value ${seekBar.progress}")
                    viewModel.issueLiveCommand(
                        deviceAddress,
                        "brightness",
                        seekBar.progress.toString()
                    )
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
        }

        s.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.issueLiveCommand(deviceAddress, "state", "ON")
                //we can use text to get current device name
                println(text)
                println("checked")
                //buttonView.isChecked = false
            } else {
                println("not checked")
                viewModel.issueLiveCommand(deviceAddress, "state", "OFF")
            }
        }


        return v
    }

    private fun createDeviceList(
        devices: ArrayList<String>,
        deviceIeeeAdder: ArrayList<String>,
        states: ArrayList<String>,
        layout: LinearLayout
    ): LinearLayout {
        layout.removeAllViewsInLayout()
        val spaceHeight = 30

        for (i in 0 until devices.size) {
            val stateList = states[i].split(",")
            var status = false
            var brightness = 0
            var color = "0"
            if (stateList[0].contains("ON"))
                status = true
            if (stateList.size > 2) {
                brightness = stateList[1].toInt()
                color = stateList[2]
            }
            when {
                devices[i].toLowerCase(Locale.ROOT).contains("go") -> {
                    layout.addView(
                        adjustView(
                            R.drawable.go, devices[i], status, brightness,
                            deviceIeeeAdder[i], color
                        )
                    )
                    val space = Space(context)
                    space.minimumHeight = spaceHeight
                    layout.addView(space)
                }
                devices[i].toLowerCase(Locale.ROOT).contains("strip") -> {
                    layout.addView(
                        adjustView(
                            R.drawable.strip, devices[i], status, brightness,
                            deviceIeeeAdder[i], color
                        )
                    )
                    val space = Space(context)
                    space.minimumHeight = spaceHeight
                    layout.addView(space)
                }
                devices[i].toLowerCase(Locale.ROOT).contains("e26") -> {
                    layout.addView(
                        adjustView(
                            R.drawable.bulb, devices[i], status, brightness,
                            deviceIeeeAdder[i], color
                        )
                    )
                    val space = Space(context)
                    space.minimumHeight = spaceHeight
                    layout.addView(space)
                }
                devices[i].toLowerCase(Locale.ROOT).contains("smart") -> {

                    layout.addView(
                        adjustView(
                            -1, devices[i], status, 0,
                            deviceIeeeAdder[i], "0"
                        )
                    )
                    val space = Space(context)
                    space.minimumHeight = spaceHeight
                    layout.addView(space)
                }
            }
        }
        return layout
    }

    private fun colorPicker(hashCode: Int) {
        val dialog = ColorPickerDialog.newInstance(hashCode)
        dialog.setTargetFragment(targetFragment, 0)
        dialog.show(childFragmentManager, "color0")
    }

}
