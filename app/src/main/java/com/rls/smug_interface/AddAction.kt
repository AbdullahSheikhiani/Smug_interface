package com.rls.smug_interface

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_action.*
import java.net.Inet4Address
import java.net.Socket
import kotlin.concurrent.thread

class AddAction : AppCompatActivity() {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_action)
        val gstList = ArrayList<String>()
        val devices = ArrayList<String>()
        val th = thread {
            val connection = Socket(IP(), 5050)
            print(connection)
            val reader = connection.getInputStream()
            val b = reader.bufferedReader()
            var x = b.readLine()
            print(x)
            while (x != "stp") {
                gstList.add(x)
                x = b.readLine()
            }
            println("got list of gestures")
            x = b.readLine()
            while (x != "stp") {
                devices.add(x)
                x = b.readLine()
            }
            println("got list of devices")
        }
        th.join()
    }
}
