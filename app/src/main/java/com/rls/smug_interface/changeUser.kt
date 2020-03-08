package com.rls.smug_interface

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_change_user.*
import java.net.Socket
import kotlin.concurrent.thread

class changeUser : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_user)
       // val ip = "192.168.0.186"
        println('x')
        println(ip)

        val a = ArrayList<String>()
        btnReload.setOnClickListener {
            val t = thread {
                println("THREAD")
                val connection = Socket(ip, 5050)
                val reader = connection.getInputStream()
                val writer = connection.getOutputStream()
                val b = reader.bufferedReader()
                //println("AFTER BUFF")
                var x = b.readLine()
                println(x)
                while (x != "stp") {
                    a.add(x)
                    x = b.readLine()
                    println(x)
                }
                connection.close()
                println("list reading finished")
            }
            t.join()
            val adapter = ArrayAdapter(
                this, // Context
                android.R.layout.simple_spinner_item, // Layout
                a // Array
            )
            adapter.notifyDataSetChanged()
            userSpinner.adapter = adapter
        }

    }
}
