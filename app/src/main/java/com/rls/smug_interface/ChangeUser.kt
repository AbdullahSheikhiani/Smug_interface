package com.rls.smug_interface

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_change_user.*
import java.net.Inet4Address
import java.net.Socket
import kotlin.concurrent.thread

class ChangeUser : AppCompatActivity() {
    /*fun IP(): String? {
       val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
       return sharedPreferences.getString("ip", "192.168.4.1")
   }*/
    fun ip(): String {
//        return "192.168.1.126"

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
        setContentView(R.layout.activity_change_user)
        // val ip = "192.168.0.186"
        println("change user")
        println(ip())

        val a = ArrayList<String>()
        val t = thread {
            println("THREAD")
            val connection = Socket(ip(), 5050)
            val reader = connection.getInputStream()
            //val writer = connection.getOutputStream()
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

        btnChgUser.setOnClickListener {
            val th = thread {
                println("THREAD")
                val connection = Socket(ip(), 5050)
                //val reader = connection.getInputStream()
                val writer = connection.getOutputStream()
                writer.write(userSpinner.selectedItem.toString().toByteArray())
                //todo validation
            }
            th.join()
            /*
            Looper.prepare()
            Toast.makeText(
                applicationContext,
                "changed to user to ${userSpinner.selectedItem}",
                Toast.LENGTH_SHORT
                //applicationContext,
                //wifiManager.scanResults[0].toString(),
                //Toast.LENGTH_LONG

            ).show()
             */
            val intent = Intent(this, Interface::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            applicationContext.startActivity(intent)
            finish()
        }

    }
}
