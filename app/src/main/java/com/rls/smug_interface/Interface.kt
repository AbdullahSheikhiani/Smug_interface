package com.rls.smug_interface

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_interface.*
import java.net.Inet4Address
import java.net.InetAddress
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
            //finish()
        }
        lstGstBtn.setOnClickListener {
            //println("XXXXX")
            val t = thread {
                //println("ip by DNS")
                //val ip = InetAddress.getByName("pspsps")
                val ip = "192.168.0.186"
                print("ip =")
                println(ip)
                // val ip = "192.168.4.1"
                val connection = Socket(ip, 5051)
                //val reader = connection.getInputStream()
                val writer = connection.getOutputStream()
                writer.write("1".toByteArray())
                connection.close()
            }
            t.join()
            val intent = Intent(this, ListGestures::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            applicationContext.startActivity(intent)
            //finish()
        }
        addGstBtn.setOnClickListener {
            val t = thread {
                //val ip = InetAddress.getByName("pspsps")
                val ip = "192.168.0.186"
                val connection = Socket(ip, 5051)
                //val reader = connection.getInputStream()
                val writer = connection.getOutputStream()
                writer.write("2".toByteArray())
                connection.close()
            }
            t.join()
            val intent = Intent(this, AddGesture::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            applicationContext.startActivity(intent)
            //finish()
        }
        rmGstBtn.setOnClickListener {
            val t = thread {
                //val ip = InetAddress.getByName("pspsps")
                val ip = "192.168.0.186"
                val connection = Socket(ip, 5051)
                //val reader = connection.getInputStream()
                val writer = connection.getOutputStream()
                writer.write("3".toByteArray())
                connection.close()
            }
            t.join()
            val intent = Intent(this, RemoveGesture::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            applicationContext.startActivity(intent)
        }
        tstGesBtn.setOnClickListener{
            val t = thread {
                //val ip = InetAddress.getByName("pspsps")
                val ip = "192.168.0.186"
                val connection = Socket(ip, 5051)
                //val reader = connection.getInputStream()
                val writer = connection.getOutputStream()
                writer.write("4".toByteArray())
                connection.close()
            }

        }
        addUsrBtn.setOnClickListener{
            val t = thread {
                //val ip = InetAddress.getByName("pspsps")
                val ip = "192.168.0.186"
                val connection = Socket(ip, 5051)
                //val reader = connection.getInputStream()
                val writer = connection.getOutputStream()
                writer.write("6".toByteArray())
                connection.close()
            }
            t.join()
            val intent = Intent(this, addUser::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            applicationContext.startActivity(intent)
        }
    }
}
