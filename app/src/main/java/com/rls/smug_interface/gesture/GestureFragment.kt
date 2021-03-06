package com.rls.smug_interface.gesture

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
import kotlinx.android.synthetic.main.activity_add_gesture.view.*
import kotlinx.android.synthetic.main.fragment_list.view.*
import kotlinx.android.synthetic.main.fragment_main_ui.view.*


class GestureFragment : Fragment() {

    lateinit var viewModel: GestureViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(GestureViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_main_ui, container, false)

        val layout = root.deviceLayout

        val listBtn = root.imageViewList
        val removeBtn = root.imageViewDel
        val addBtn = root.imageViewAdd

        val fList = listBtn.colorFilter
        val fRemove = removeBtn.colorFilter
        val fAdd = addBtn.colorFilter

        listBtn.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    addBtn.colorFilter = fAdd
                    removeBtn.colorFilter = fRemove
                }
                MotionEvent.ACTION_DOWN -> {
                    listBtn.setColorFilter(Color.rgb(0, 0, 0))
                    //move to listGestures
                    //val mInflater = requireActivity().layoutInflater
                    //val view = mInflater.inflate(R.layout.fragment_list, null)
                    val view = View.inflate(context, R.layout.fragment_list, null)
                    val list = view.listView
                    //val a = ArrayList<String>()
                    viewModel.getGestureList()
                    viewModel.gestureList.observe(viewLifecycleOwner, Observer {
                        val adapter = ArrayAdapter(
                            context!!, // Context
                            android.R.layout.simple_expandable_list_item_1, // Layout
                            it // Array
                        )
                        list.adapter = adapter
                        layout.removeAllViews()
                        layout.addView(view)
                    })
                }
            }
            true
        }


        removeBtn.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    addBtn.colorFilter = fAdd
                    listBtn.colorFilter = fList
                }
                MotionEvent.ACTION_DOWN -> {
                    removeBtn.setColorFilter(Color.rgb(0, 0, 0))
                    //val mInflater = requireActivity().layoutInflater
                    //val view = mInflater.inflate(R.layout.fragment_list, null)
                    val view = View.inflate(context, R.layout.fragment_list, null)
                    val list = view.findViewById<ListView>(R.id.listView)

                    viewModel.getGestureList()
                    viewModel.gestureList.observe(viewLifecycleOwner, Observer {
                        val adapter = ArrayAdapter(
                            context!!, // Context
                            android.R.layout.simple_expandable_list_item_1, // Layout
                            it // Array
                        )

                        list.adapter = adapter
                        layout.removeAllViews()
                        layout.addView(view)
                        list.setOnItemClickListener { parent, view, position, id ->
                            viewModel.removeGesture(adapter.getItem(position)!!.toString())
                            layout.removeAllViewsInLayout()
                            val txt = TextView(context)
                            //todo ack
                            txt.text = "removed ${adapter.getItem(position)} successfully"
                            layout.addView(txt)
                        }
                    })

                }

            }
            true
        }
        addBtn.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    removeBtn.colorFilter = fAdd
                    listBtn.colorFilter = fList
                }
                MotionEvent.ACTION_DOWN -> {

                    layout.removeAllViewsInLayout()
                    //val mInflater = requireActivity().layoutInflater
                    // val view = mInflater.inflate(R.layout.activity_add_gesture, null)
                    val view = View.inflate(context, R.layout.activity_add_gesture, null)

                    val gestureName = view.gestureNameTxt
                    val addGstbtn = view.btnAdd
                    val timesLeft = view.timesLeftText
                    val progress = view.progressBar
                    timesLeft.text = ""
                    println(savedInstanceState)
                    addBtn.setColorFilter(Color.rgb(0, 0, 0))
                    addGstbtn.setOnClickListener {
                        gestureName.isEnabled = false
                        println(gestureName.text.toString())
                        viewModel.addGesture(gestureName.text.toString())
                        viewModel.getRemainingTimes()
                    }
                    viewModel.remainingTimes.observe(viewLifecycleOwner, Observer {
                        timesLeft.text = "recordings left ${it}"

                        if (it != 0 && it != -1)
                            viewModel.getRemainingTimes()
                        else if (it == 0) {
                            progress.visibility = View.VISIBLE
                            timesLeft.text = "Adding Gesture to system, will take a moment"
                            viewModel.getRemainingTimes()
                        } else {
                            progress.visibility = View.INVISIBLE
                            timesLeft.text = ""
                            val text = "Gesture added successfully"
                            val duration = Toast.LENGTH_LONG
                            val toast = Toast.makeText(context, text, duration)
                            toast.show()
                            gestureName.setText("")
                            gestureName.isEnabled = true
                        }
                    })
                    layout.removeAllViewsInLayout()
                    layout.addView(view)

                }

            }
            true
        }


        return root
    }
}
