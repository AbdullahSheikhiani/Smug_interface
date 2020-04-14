package com.rls.smug_interface.user

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.rls.smug_interface.R
import kotlinx.android.synthetic.main.fragment_main_ui.view.*

class UserFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_main_ui, container, false)
        val listBtn = root.imageViewList
        val removeBtn = root.imageViewDel
        val addBtn = root.imageViewAdd

        val fList = listBtn.colorFilter
        val fRemove = removeBtn.colorFilter
        val fAdd = addBtn.colorFilter

        listBtn.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    listBtn.setColorFilter(fAdd)
                }
                MotionEvent.ACTION_DOWN -> {
                    listBtn.setColorFilter(Color.rgb(0, 0, 0))
                    //todo my stuff

                }

            }
            true
        }
        removeBtn.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    removeBtn.setColorFilter(fAdd)
                }
                MotionEvent.ACTION_DOWN -> {
                    removeBtn.setColorFilter(Color.rgb(0, 0, 0))
                    //todo my stuff

                }

            }
            true
        }
        addBtn.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    addBtn.setColorFilter(fAdd)
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
}
