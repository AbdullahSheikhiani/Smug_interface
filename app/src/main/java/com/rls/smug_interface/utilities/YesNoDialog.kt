package com.rls.smug_interface.utilities

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.rls.smug_interface.R


class YesNoDialog : DialogFragment() {
    // Use this instance of the interface to deliver action events
    private lateinit var listener: NoticeDialogListener

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    interface NoticeDialogListener {
        fun onDialogPositiveClick(dialog: Dialog?)
        fun onDialogNegativeClick(dialog: Dialog?)
    }

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = context as NoticeDialogListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException(
                (context.toString() +
                        " must implement NoticeDialogListener")
            )
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Build the dialog and set up the button click handlers
            val builder = AlertDialog.Builder(it)

            builder.setMessage(R.string.on_or_off)

            builder.apply {
                setPositiveButton(R.string.On,
                    DialogInterface.OnClickListener { dialog, id ->
                        // User clicked OK button
                        listener.onDialogPositiveClick(getDialog())
                        ///println("ON")

                    })
                setNegativeButton(R.string.Off,
                    DialogInterface.OnClickListener { dialog, id ->
                        listener.onDialogNegativeClick(getDialog())

                        ////println("OFF")
                    })
            }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}