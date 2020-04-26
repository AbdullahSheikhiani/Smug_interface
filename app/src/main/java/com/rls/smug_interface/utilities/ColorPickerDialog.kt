package com.rls.smug_interface.utilities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.rls.smug_interface.R
import kotlinx.android.synthetic.main.fragment_color.view.*
import com.skydoves.colorpickerview.listeners.ColorListener
import kotlinx.android.synthetic.main.fragment_color.view.colorPickerView

class ColorPickerDialog : DialogFragment() {
    private lateinit var listener: ColorListener
    private var code: Int = -1

    interface ColorListener {
        fun saveColor(color: String, code: Int)
    }


    companion object {

        fun newInstance(c: Int) = ColorPickerDialog().apply {
            arguments = Bundle().apply {
                putInt("viewHash", c)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            code = it.getInt("viewHash")

        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = context as ColorListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException(
                (context.toString() +
                        "must implement ColorListener")
            )
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Build the dialog and set up the button click handlers
            val builder = AlertDialog.Builder(it)
            //val myInflater = requireActivity().layoutInflater
            val v: View = View.inflate(context, R.layout.fragment_color, null)
            val save = v.returnColorBtn2
            val colorPickerView = v.colorPickerView
            var colour = 0
            //colorPickerView.setSelectorPoint(0, 1)
            colorPickerView.setColorListener(ColorListener { color, _ ->
                //println(String.format("#%06X", 0xFFFFFF and colour))
                colour = color
            })
            save.setOnClickListener {
                println("code = $code")
                listener.saveColor(
                    String.format("#%06X", 0xFFFFFF and colour),
                    code
                )

                dismiss()
            }
            builder.setView(v)

            builder.create()

        } ?: throw IllegalStateException("Activity cannot be null")
    }
}