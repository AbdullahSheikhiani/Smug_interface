package com.rls.smug_interface.deviceAndAction

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.Switch
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.rls.smug_interface.R
import kotlinx.android.synthetic.main.fragment_main_ui_device.view.*


class DeviceFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

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
                    listBtn.setColorFilter(Color.rgb(0, 0, 0))
                    val x1 = adjustView(R.drawable.bulb, "BULB", false, 100)
                    layout.addView(x1)
                    val x2 = adjustView(R.drawable.strip, "STrip", true, 30)
                    layout.addView(x2)

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
        return v
    }
}
