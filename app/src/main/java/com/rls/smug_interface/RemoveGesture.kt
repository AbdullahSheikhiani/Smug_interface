package com.rls.smug_interface

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_remove_gesture.*
import java.net.Socket
import kotlin.concurrent.thread

class RemoveGesture : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remove_gesture)
        //val ip = "192.168.0.186"
        val a = ArrayList<String>()
        rldBtn.setOnClickListener {
            val t = thread {
                println("THREAD")
                val connection = Socket(ip, 5050)
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
                this, // Context
                android.R.layout.simple_spinner_item, // Layout
                a // Array
            )
            adapter.notifyDataSetChanged()
            spinner.adapter = adapter
        }
        btnRemove.setOnClickListener {
            val t = thread {
                val connection = Socket(ip, 5050)
                val reader = connection.getInputStream()
                val writer = connection.getOutputStream()
                writer.write(spinner.selectedItem.toString().toByteArray())
                val r = reader.bufferedReader()
                print("removed gesture read: ")
                println(r.readLine())

            }
            t.join()

        }
        btnBack.setOnClickListener {
            val intent = Intent(this, Interface::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }
}
