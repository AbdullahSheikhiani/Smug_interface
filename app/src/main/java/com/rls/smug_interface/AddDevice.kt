package com.rls.smug_interface

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_add_device.*
import java.net.Inet4Address
import java.net.Socket
import kotlin.concurrent.thread

class AddDevice : AppCompatActivity() {
    fun IP(): String {
        val host = "pspspspi"
        lateinit var ipas: String
        try {
            //print("INSIDE SHOW IP ADDRESS")
            val t = thread {
                ipas = Inet4Address.getByName(host).hostAddress
            }
            t.join()
            //println("The IP address(es) for '$host' is/are:\n")
            //println(ipas)
            return ipas
        } catch (ex: Exception) {
            println(ex.message)
        }
        return "192.168.4.1"
    }

    private var success = false
    override fun onCreate(savedInstanceState: Bundle?) {
        //TODO add refresh button
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_device)

        val a = ArrayList<String>()
        val adapter = ArrayAdapter(
            this, // Context
            android.R.layout.simple_expandable_list_item_1, // Layout
            a // Array
        )
        deviceList.adapter = adapter
        val t = thread {
            println("thread")
            val connection = Socket(IP(), 5050)
            println(connection)
            val reader = connection.getInputStream()
            val b = reader.bufferedReader()
            var x = b.readLine()
            while (x != "stp") {
                a.add(x)
                x = b.readLine()
            }
            connection.close()
            success = true
        }
        t.join()
        adapter.notifyDataSetChanged()
        println("\ngot list of devices")
        for (i in a) {
            print("d: ")
            println(i)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (!success) {
            val th = thread {
                val connection = Socket(IP(), 5050)
                //val reader = connection.getInputStream()
                val writer = connection.getOutputStream()
                writer.write("0".toByteArray())
                connection.close()

            }
            th.join()
            println("sent 0 to return to Interface")
        }
        val intent = Intent(this, Interface::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        applicationContext.startActivity(intent)
        finish()
    }


}
