package com.rls.smug_interface

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.skydoves.colorpickerview.listeners.ColorListener
import kotlinx.android.synthetic.main.activity_color_handler.*


class ColorHandler : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_handler)
        colorPickerView.setColorListener(ColorListener { color, fromUser ->
            println(color)
        })
    }
}
