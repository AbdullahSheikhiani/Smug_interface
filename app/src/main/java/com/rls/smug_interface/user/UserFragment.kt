package com.rls.smug_interface.user

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.rls.smug_interface.R
import kotlinx.android.synthetic.main.fragment_main_ui.view.*
import java.net.Inet4Address
import java.net.Socket
import kotlin.concurrent.thread

class UserFragment : Fragment() {

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
                    listBtn.colorFilter = fList
                }
                MotionEvent.ACTION_DOWN -> {
                    listBtn.setColorFilter(Color.rgb(0, 0, 0))
                    //todo my stuff
                    val mInflater = requireActivity().layoutInflater
                    val view = mInflater.inflate(R.layout.fragment_list_gestures, null)
                    val list = view.findViewById<ListView>(R.id.listGstView)
                    val a = ArrayList<String>()
                    val t = thread {
                        println("list user THREAD")
                        var connection = Socket(ip(), 5051)
                        val writer = connection.getOutputStream()
                        writer.write("5".toByteArray())
                        connection.close()
                        connection = Socket(ip(), 5050)
                        val reader = connection.getInputStream()
                        val b = reader.bufferedReader()
                        //println("AFTER BUFF")
                        var x = b.readLine()
                        while (x != "stp") {
                            a.add(x)
                            x = b.readLine()
                        }
                        connection.close()

                        println("list reading finished")
                    }
                    t.join()
                    val adapter = ArrayAdapter(
                        context, // Context
                        android.R.layout.simple_spinner_item, // Layout
                        a // Array
                    )
                    list.adapter = adapter
                    layout.removeAllViews()
                    layout.addView(view)
                    list.setOnItemClickListener { parent, view, position, id ->
                        val th = thread {
                            println("list user THREAD")
                            val connection = Socket(ip(), 5050)
                            //val reader = connection.getInputStream()
                            val writer = connection.getOutputStream()
                            writer.write(a[position].toByteArray())
                            connection.close()

                        }
                        th.join()
                        layout.removeAllViewsInLayout()
                        val txt = TextView(context)
                        txt.text = "changed to user ${a[position]} successfully"
                        layout.addView(txt)
                    }
                }

            }
            true
        }
        removeBtn.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    removeBtn.colorFilter = fRemove
                }
                MotionEvent.ACTION_DOWN -> {
                    removeBtn.setColorFilter(Color.rgb(0, 0, 0))
                    val mInflater = requireActivity().layoutInflater
                    val view = mInflater.inflate(R.layout.fragment_list_gestures, null)
                    val list = view.findViewById<ListView>(R.id.listGstView)

                    val a = ArrayList<String>()

                    val t = thread {
                        println("remove user THREAD")

                        //val ip = InetAddress.getByName("pspsps")
                        //val ip = "192.168.0.186"
                        var connection = Socket(ip(), 5051)
                        //val reader = connection.getInputStream()
                        val writer = connection.getOutputStream()
                        writer.write("7".toByteArray())
                        connection.close()

                        connection = Socket(ip(), 5050)
                        val reader = connection.getInputStream()
                        //val writer = connection.getOutputStream()
                        val b = reader.bufferedReader()
                        //list users
                        var x = b.readLine()
                        while (x != "stp") {
                            a.add(x)
                            x = b.readLine()
                            //print("x=")
                            println(x)
                        }
                        connection.close()
                    }
                    t.join()
                    val adapter = ArrayAdapter(
                        context, // Context
                        android.R.layout.simple_expandable_list_item_1, // Layout
                        a // Array
                    )
                    adapter.notifyDataSetChanged()
                    list.adapter = adapter
                    layout.removeAllViews()
                    layout.addView(view)
                    list.setOnItemClickListener { parent, view, position, id ->
                        val th = thread {
                            val connection = Socket(ip(), 5050)
                            val reader = connection.getInputStream()
                            val writer = connection.getOutputStream()
                            writer.write(a[position].toByteArray())
                            val r = reader.bufferedReader()
                            if (r.readLine() == "NAK") {
                            }
                            println(r.readLine())
                        }
                        th.join()
                        layout.removeAllViews()
                        val txt = TextView(context)
                        txt.text = "remove user ${a[position]} successfully"
                        layout.addView(txt)
                    }
                }

            }
            true
        }
        addBtn.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    addBtn.colorFilter = fAdd
                }
                MotionEvent.ACTION_DOWN -> {
                    addBtn.setColorFilter(Color.rgb(0, 0, 0))

                    val mInflater = requireActivity().layoutInflater
                    val view = mInflater.inflate(R.layout.activity_add_user, null)
                    val user = view.findViewById<EditText>(R.id.addUserEditText)
                    val btn = view.findViewById<Button>(R.id.btnSendUser)
                    val t = thread {
                        //val ip = InetAddress.getByName("pspsps")
                        //val ip = "192.168.0.186"
                        val connection = Socket(ip(), 5051)
                        //val reader = connection.getInputStream()
                        val writer = connection.getOutputStream()
                        writer.write("6".toByteArray())
                        connection.close()
                    }
                    t.join()
                    btn.setOnClickListener {
                        val t = thread {
                            val connection = Socket(ip(), 5050)
                            //val reader = connection.getInputStream()
                            val writer = connection.getOutputStream()
                            writer.write(user.text.toString().toByteArray())
                            connection.close()
                            //TODO fingerprint confirmation
                        }
                        t.join()
                    }

                }

            }
            true
        }



        return root
    }
}
