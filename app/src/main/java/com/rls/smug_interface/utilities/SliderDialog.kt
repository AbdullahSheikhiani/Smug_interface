package com.rls.smug_interface.utilities

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.rls.smug_interface.R
import kotlinx.android.synthetic.main.fragment_scrollbar.view.*

class SliderDialog : DialogFragment() {

    private lateinit var listener: OnSaveListener

    interface OnSaveListener {
        fun onSave(value: String)
        fun onCancel()

    }
    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */


    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = context as OnSaveListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException(
                (context.toString() +
                        "must implement NoticeDialogListener")
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Build the dialog and set up the button click handlers
            val builder = AlertDialog.Builder(it)
            builder.setMessage("AAAAAAAAAAAAAAAA")
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.fragment_scrollbar, null)
            builder.setView(view)
            val slider = view.fluidSlider
            slider.endText = "255"
            slider.position = 0.5F
            slider.bubbleText = "127"
            slider.positionListener = { pos -> slider.bubbleText = "${(255 * pos).toInt()}" }
            //slider.endTrackingListener = { println(slider.bubbleText) }
            //slider.positionListener = { pos -> slider.bubbleText = "${(255 * pos).toInt()}" }
            //todo REGISTER BUTTONS HERE
            builder.apply {
                //println(slider.bubbleText)

                setPositiveButton(
                    R.string.save
                ) { _, _ ->
                    // User clicked OK button
                    println("USER CHOSE SAVE")
                    listener.onSave(slider.bubbleText.toString())
                    dialog?.dismiss()


                    ///println("ON")

                }
                setNegativeButton(R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        listener.onCancel()
                        dialog.dismiss()

                        ////println("OFF")
                    })
            }
            builder.create()

        } ?: throw IllegalStateException("Activity cannot be null")
    }

}