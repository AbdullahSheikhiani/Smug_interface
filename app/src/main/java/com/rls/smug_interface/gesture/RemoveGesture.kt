package com.rls.smug_interface.gesture

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.rls.smug_interface.Interface
import com.rls.smug_interface.R
import kotlinx.android.synthetic.main.activity_remove_gesture.*
import java.net.Inet4Address
import java.net.Socket
import kotlin.concurrent.thread

class RemoveGesture : AppCompatActivity() {

    /*fun IP(): String? {
       val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
       return sharedPreferences.getString("ip", "192.168.4.1")
   }*/

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
            println(ex.message)
            "192.168.1.126"
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remove_gesture)
        //val ip = "192.168.0.186"
        val a = ArrayList<String>()
        val t = thread {
            println("THREAD")
            val connection = Socket(ip(), 5050)
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
        btnRemove.setOnClickListener {
            val th = thread {
                val connection = Socket(ip(), 5050)
                val reader = connection.getInputStream()
                val writer = connection.getOutputStream()
                writer.write(spinner.selectedItem.toString().toByteArray())
                val r = reader.bufferedReader()
                print("removed gesture read: ")
                println(r.readLine())

            }
            th.join()

        }
        btnBack.setOnClickListener {
            val intent = Intent(this, Interface::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val th = thread {
            val connection = Socket(ip(), 5050)
            //val reader = connection.getInputStream()
            val writer = connection.getOutputStream()
            writer.write("0".toByteArray())
            connection.close()

        }
        th.join()
        println("sent 0 to return to Interface")
        val intent = Intent(this, Interface::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        applicationContext.startActivity(intent)
        finish()
    }
}
