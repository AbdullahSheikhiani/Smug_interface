package com.rls.smug_interface.gesture

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.rls.smug_interface.R
import kotlinx.android.synthetic.main.activity_remove_gesture.*
import kotlinx.android.synthetic.main.fragment_main_ui.view.*
import java.net.Inet4Address
import java.net.Socket
import kotlin.concurrent.thread

class GestureFragment : Fragment() {

    fun ip(): String {

        val host = "pspspspi"
        lateinit var ipas: String
        return try {
            //print("INSIDE SHOW IP ADDRESS")
            val t = thread {
                ipas = Inet4Address.getByName(host).hostAddress
            }
            t.join()
            ipas
        } catch (ex: Exception) {
            println(ex.message)
            "192.168.1.126"
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_main_ui, container, false)

        val layout = root.vLayout

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
                    val mInflater = requireActivity().layoutInflater
                    val view = mInflater.inflate(R.layout.fragment_list_gestures, null)
                    val list = view.findViewById<ListView>(R.id.listGstView)
                    val a = ArrayList<String>()
                    val t = thread {
                        println("list gestures THREAD")
                        print("IP= ")
                        println(ip())
                        var connection = Socket(ip(), 5051)
                        //val reader = connection.getInputStream()
                        val writer = connection.getOutputStream()
                        writer.write("1".toByteArray())
                        connection.close()
                        connection = Socket(ip(), 5050)
                        val reader = connection.getInputStream()
                        val b = reader.bufferedReader()
                        var x = b.readLine()
                        while (x != "stp") {
                            a.add(x)
                            x = b.readLine()
                        }
                        connection.close()
                    }
                    t.join()
                    val adapter = ArrayAdapter(
                        context, // Context
                        android.R.layout.simple_expandable_list_item_1, // Layout
                        a // Array
                    )
                    list.adapter = adapter
                    layout.removeAllViews()
                    layout.addView(view)
                }

            }
            true
        }


        removeBtn.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    addBtn.colorFilter = fAdd
                    listBtn.colorFilter = fRemove
                }
                MotionEvent.ACTION_DOWN -> {
                    removeBtn.setColorFilter(Color.rgb(0, 0, 0))
                    //todo my stuff
                    val mInflater = requireActivity().layoutInflater
                    val view = mInflater.inflate(R.layout.fragment_list_gestures, null)
                    val list = view.findViewById<ListView>(R.id.listGstView)
                    val a = ArrayList<String>()

                    val t = thread {
                        println("thread remove gst")
                        //val ip = InetAddress.getByName("pspsps")
                        // val ip = "192.168.0.186"
                        var connection = Socket(ip(), 5051)
                        //val reader = connection.getInputStream()
                        val writer = connection.getOutputStream()
                        writer.write("3".toByteArray())
                        connection.close()

                        connection = Socket(ip(), 5050)
                        val reader = connection.getInputStream()
                        //val writer = connection.getOutputStream()
                        val b = reader.bufferedReader()
                        //list Gestures
                        var x = b.readLine()
                        print(x)
                        while (x != "stp") {
                            a.add(x)
                            x = b.readLine()
                            //print("x=")
                            //println(x)
                        }
                        connection.close()
                    }
                    t.join()
                    val adapter = ArrayAdapter(
                        context, // Context
                        android.R.layout.simple_expandable_list_item_1, // Layout
                        a // Array
                    )
                    list.adapter = adapter
                    layout.removeAllViews()
                    layout.addView(view)
                    list.setOnItemClickListener { parent, view, position, id ->
                        val th = thread {
                            print("item = ")
                            println(a[position])
                            val connection = Socket(ip(), 5050)
                            val reader = connection.getInputStream()
                            val writer = connection.getOutputStream()
                            writer.write(a[position].toByteArray())
                            val r = reader.bufferedReader()
                            print("removed gesture read: ")
                            println(r.readLine())
                        }
                        th.join()
                        layout.removeAllViewsInLayout()
                        val txt = TextView(context)
                        txt.text = "removed ${a[position]} successfully"
                        layout.addView(txt)
                    }

                }

            }
            true
        }
        addBtn.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    removeBtn.colorFilter = fAdd
                    listBtn.colorFilter = fRemove
                }
                MotionEvent.ACTION_DOWN -> {
                    val mInflater = requireActivity().layoutInflater
                    val view = mInflater.inflate(R.layout.activity_add_gesture, null)
                    val gestureName = view.findViewById<EditText>(R.id.gestureNameTxt)
                    //todo remove done button
                    val addGstbtn = view.findViewById<Button>(R.id.btnAdd)
                    addBtn.setColorFilter(Color.rgb(0, 0, 0))
                    addGstbtn.setOnClickListener {
                        val t = thread {
                            var connection = Socket(ip(), 5051)
                            //val reader = connection.getInputStream()
                            var writer = connection.getOutputStream()
                            writer.write("2".toByteArray())
                            connection.close()

                            connection = Socket(ip(), 5050)
                            //val reader = connection.getInputStream()
                            writer = connection.getOutputStream()
                            writer.write(gestureName.text.toString().toByteArray())
                            connection.close()
                        }
                        t.join()
                    }
                    layout.removeAllViewsInLayout()
                    layout.addView(view)

                }

            }
            true
        }


        return root
    }
}
