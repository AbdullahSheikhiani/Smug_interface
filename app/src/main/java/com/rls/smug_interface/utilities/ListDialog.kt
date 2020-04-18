package com.rls.smug_interface.utilities

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.rls.smug_interface.R
import kotlinx.android.synthetic.main.fragment_list.view.*
import java.net.Inet4Address
import java.net.Socket
import kotlin.concurrent.thread

class ListDialog : DialogFragment() {
    private lateinit var listener: ListDialog.ListDialogListener

    interface ListDialogListener {
        fun onListItem(item: String)
        //private fun adjustView(imgID: Int, text: String, status: Boolean, brightness: Int): View {
    }

    fun ip(): String {

        val host = "pspspspi"
        lateinit var ipas: String
        return try {
            //print("INSIDE SHOW IP ADDRESS")
            val t = thread {
                ipas = Inet4Address.getByName(host).hostAddress
            }
            t.join()
            //ipas

            //work around
            return if (ipas == "127.0.1.1")
                "192.168.1.126"
            else
                ipas
        } catch (ex: Exception) {
            println(ex.message)
            "192.168.1.126"
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = context as ListDialog.ListDialogListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException(
                (context.toString() +
                        "must implement NoticeDialogListener")
            )
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { it ->
            // Build the dialog and set up the button click handlers
            val builder = AlertDialog.Builder(it)
            val myInflater = requireActivity().layoutInflater
            val v: View = myInflater.inflate(R.layout.fragment_list, null)
            val list = v.listView

            thread {
                val a = ArrayList<String>()
                var connection = Socket(ip(), 5050)
                print(connection)
                val writer = connection.getOutputStream()
                writer.write("9".toByteArray())
                println("sent 9")
                connection.close()
                Thread.sleep(1)
                connection = Socket(ip(), 5050)
                println(connection)
                val reader = connection.getInputStream()
                val b = reader.bufferedReader()
                var x = b.readLine()
                while (x != "stp") {
                    a.add(x)
                    x = b.readLine()
                }
                println("got device list array ")
                val adapter = ArrayAdapter(
                    context, // Context
                    android.R.layout.simple_expandable_list_item_1, // Layout
                    a // Array
                )
                list.adapter = adapter
            }.join()
            list.setOnItemClickListener { parent, view, position, id ->
                listener.onListItem(list.adapter.getItem(position).toString())
                dismiss()
            }
            builder.setView(v)
            builder.create()

        } ?: throw IllegalStateException("Activity cannot be null")
    }


}