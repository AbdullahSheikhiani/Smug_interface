package com.rls.smug_interface.utilities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rls.smug_interface.R
import com.skydoves.colorpickerview.listeners.ColorListener
import kotlinx.android.synthetic.main.activity_color_handler.*


class ColorHandler : AppCompatActivity() {
    private var colour = 0
    private val returnIntent = Intent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_handler)
        colorPickerView.setColorListener(ColorListener { color, _ ->
            println(String.format("#%06X", 0xFFFFFF and colour))
            colour = color
        })
        returnColorBtn.setOnClickListener {
            returnIntent.putExtra(
                "color",
                String.format("#%06X", 0xFFFFFF and colour)
            )
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val hexColor =
            returnIntent.putExtra(
                "color",
                String.format("#%06X", 0xFFFFFF and colour)
            )
        setResult(Activity.RESULT_OK, returnIntent)
        finish()

    }
}
