package com.rls.smug_interface.gesture

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.rls.smug_interface.R
import java.net.Inet4Address
import java.net.Socket
import kotlin.concurrent.thread


class ListGestures {


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

    private var success = false
    public fun getList() {
        val a = ArrayList<String>()
        val t = thread {
            println("THREAD")
            print("IP= ")
            println(ip())
            val connection = Socket(ip(), 5050)
            print(connection)
            val reader = connection.getInputStream()
            val b = reader.bufferedReader()
            var x = b.readLine()
            print(x)
            while (x != "stp") {
                a.add(x)
                x = b.readLine()
            }
            connection.close()
            success = true
        }
        t.join()

    }
}



