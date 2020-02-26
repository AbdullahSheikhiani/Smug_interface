package com.rls.smug_interface

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_list_gestures.*
import java.lang.Thread.sleep
import java.net.InetAddress
import java.net.Socket
import kotlin.concurrent.thread

class ListGestures : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_gestures)
        //sleep(100)
        //val ip = InetAddress.getByName("pspsps")
        val ip = "192.168.0.186"
        println('x')
        println(ip)
        val a = ArrayList<String>()
        val t = thread {
            println("THREAD")
            val connection = Socket(ip, 5050)
            val reader = connection.getInputStream()
            //val writer = connection.getOutputStream()
            val b = reader.bufferedReader()
            //println("AFTER BUFF")
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
        var r = ""
        for (i in a) {
            print("i =")
            println(i)
            r = r + i + "\n"
        }
        print("r=")
        println(r)
        txt.text = r
        bckBtn.setOnClickListener {
            val intent = Intent(this, Interface::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }
}
