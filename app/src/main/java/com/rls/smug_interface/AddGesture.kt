package com.rls.smug_interface

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_gesture.*
import kotlinx.android.synthetic.main.activity_list_gestures.bckBtn
import kotlinx.coroutines.runBlocking
import java.net.Inet4Address
import java.net.Socket
import kotlin.concurrent.thread


class AddGesture : AppCompatActivity() {
    /*fun IP(): String? {
         val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
         return sharedPreferences.getString("ip", "192.168.4.1")
     }*/

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
        setContentView(R.layout.activity_add_gesture)
        var clickflag = false
        btnAdd.setOnClickListener {
            clickflag = true

            val t = thread {
                val connection = Socket(IP(), 5050)
                //val reader = connection.getInputStream()
                val writer = connection.getOutputStream()
                writer.write(gestureNameTxt.text.toString().toByteArray())
                connection.close()
            }
            t.join()
        }

        /*
        var x = ""

        val t = thread {
            val connection = Socket("192.168.4.1", 5050)
            val reader = connection.getInputStream()
            val b = reader.bufferedReader()
            //var s = reader.bufferedReader()
            x = b.readLine()
            while (x != "2") {
                x = b.readLine()
            }
            connection.close()
        }
        */


/*
        val t = thread {
            val connection = Socket("192.168.4.1", 5050)
            val reader = connection.getInputStream()
            val b = reader.bufferedReader()
            //var s = reader.bufferedReader()
            var x = b.readLine()
            textView3.text = "recordings left: " + x
            while (x != "2") {
                x = b.readLine()
            }
        }
        t.start()
        t.join()
*/
        bckBtn.setOnClickListener {
            val intent = Intent(this, Interface::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            applicationContext.startActivity(intent)
            finish()

        }
    }
}
