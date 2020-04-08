package com.rls.smug_interface.deviceAndAction

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.rls.smug_interface.R
import kotlinx.android.synthetic.main.activity_add_action.*
import java.net.Inet4Address
import java.net.Socket
import kotlin.concurrent.thread


class AddAction : AppCompatActivity() {
    fun ip(): String {
        //return "192.168.1.126"

        val host = "pspspspi"
        lateinit var ipas: String
        return try {
            //print("INSIDE SHOW IP ADDRESS")
            val t = thread {
                ipas = Inet4Address.getByName(host).hostAddress
            }
            t.join()
            //println("The IP address(es) for '$host' is/are:\n")
            //println(ipas)
            ipas
        } catch (ex: Exception) {
            //println(ex.message)
            "192.168.1.126"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_action)
        val gstList = ArrayList<String>()
        val devices = ArrayList<String>()
        val th = thread {
            val connection = Socket(ip(), 5050)
            print(connection)
            val reader = connection.getInputStream()
            val b = reader.bufferedReader()
            var x = b.readLine()
            println(x)
            while (x != "stp") {
                gstList.add(x)
                x = b.readLine()
                println(x)
            }
            println("got list of gestures")
            connection.close()
        }
        th.join()
        for (i in devices) {
            println(i)
        }
        val adapter = ArrayAdapter(
            this, // Context
            android.R.layout.simple_expandable_list_item_1, // Layout
            gstList // Array
        )
        gstListView.adapter = adapter
        gstListView.setOnItemClickListener { parent, view, position, id ->
            println(gstList[position])
            //call associate -> calls add device to return device -> add action
            //return h
            val intent = Intent(this, AssociateGesture::class.java)
            println(gstList[position])
            intent.putExtra("EXTRA_SESSION_ID", gstList[position])
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            applicationContext.startActivity(intent)
            finish()
        }


    }
}
