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
import com.rls.smug_interface.R
import kotlinx.android.synthetic.main.fragment_bar.view.*
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

        var layout = root.sLinear

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
                    //TODO smart plug needs different handling
                    //get address and test listener
                    viewModel.getDeviceList()
                    /*
                    val intent = Intent(context, ActionHandler::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context?.startActivity(intent)
                     */
                }
            }
            true
        }
        viewModel.deviceList.observe(viewLifecycleOwner, Observer {
            println(it)
            layout = createDeviceList(it, layout)
        })
        removeBtn.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    listBtn.colorFilter = fList
                    addBtn.colorFilter = fAdd
                    actionBtn.colorFilter = fAction
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
                    actionBtn.colorFilter = fAction

                }
                MotionEvent.ACTION_DOWN -> {
                    addBtn.setColorFilter(Color.rgb(0, 0, 0))
                    //todo my stuff

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
                    //todo my stuff
                    //get unlinked gesture list
                    //go to new activity to set up
                    val mInflater = requireActivity().layoutInflater
                    val view = mInflater.inflate(R.layout.fragment_list, null)
                    val list = view.findViewById<ListView>(R.id.listView)
                    val a = ArrayList<String>()
                    viewModel.getListOfUnLinkedGestures()
                    viewModel.gestureList.observe(viewLifecycleOwner, Observer {
                        val adapter = ArrayAdapter(
                            context, // Context
                            android.R.layout.simple_expandable_list_item_1, // Layout
                            it // Array
                        )
                        list.adapter = adapter
                        list.setOnItemClickListener { parent, view, position, id ->
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

    private fun adjustView(imgID: Int, text: String, status: Boolean, brightness: Int): View {
        //todo add listeners
        val myInflater = requireActivity().layoutInflater
        val v: View = myInflater.inflate(R.layout.fragment_bar, null)
        val img = v.deviceIcon
        val txt = v.deviceName
        val s = v.onOffSwitch
        val bright = v.brightnessBar
        img.setImageResource(imgID)
        txt.text = text
        s.isChecked = status
        bright.max = 255
        bright.progress = brightness
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
        bright.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
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

    private fun createDeviceList(devices: ArrayList<String>, layout: LinearLayout): LinearLayout {
        layout.removeAllViewsInLayout()
        for (i in devices) {
            when {
                i.toLowerCase(Locale.ROOT).contains("go") -> {
                    layout.addView(adjustView(R.drawable.go, i, false, 125))
                    val space = Space(context)
                    space.minimumHeight = 15
                    layout.addView(space)
                }
                i.toLowerCase(Locale.ROOT).contains("strip") -> {
                    layout.addView(adjustView(R.drawable.strip, i, true, 125))
                    val space = Space(context)
                    space.minimumHeight = 15
                    layout.addView(space)
                }
                i.toLowerCase(Locale.ROOT).contains("e26") -> {
                    layout.addView(adjustView(R.drawable.bulb, i, false, 0))
                    val space = Space(context)
                    space.minimumHeight = 15
                    layout.addView(space)
                }
            }
        }
        return layout
    }
}
