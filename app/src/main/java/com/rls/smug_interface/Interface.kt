package com.rls.smug_interface

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_interface.*
import java.net.Socket
import kotlin.concurrent.thread

class Interface : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interface)


        exitBtn.setOnClickListener {
            //TODO
            //exitProcess(-2)
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            applicationContext.startActivity(intent)
        }
        lstGstBtn.setOnClickListener {
            val t = thread {
                val connection = Socket("192.168.4.1", 5051)
                //val reader = connection.getInputStream()
                val writer = connection.getOutputStream()
                writer.write("1".toByteArray())
                connection.close()
            }
            t.join()
            val intent = Intent(this, ListGestures::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            applicationContext.startActivity(intent)
        }
        addGstBtn.setOnClickListener {
            val t = thread {
                val connection = Socket("192.168.4.1", 5051)
                //val reader = connection.getInputStream()
                val writer = connection.getOutputStream()
                writer.write("2".toByteArray())
                connection.close()
            }
            t.join()
            val intent = Intent(this, AddGesture::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            applicationContext.startActivity(intent)

        }
    }
}
