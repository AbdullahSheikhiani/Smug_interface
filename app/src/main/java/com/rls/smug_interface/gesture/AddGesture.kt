package com.rls.smug_interface.gesture

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rls.smug_interface.Interface
import com.rls.smug_interface.R
import kotlinx.android.synthetic.main.activity_add_gesture.*
import java.net.Inet4Address
import java.net.Socket
import kotlin.concurrent.thread


class AddGesture : AppCompatActivity() {
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
            // println("DNS REQUEST ERROR")
            //println(ex.message)
            "192.168.1.126"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_gesture)
        //var clickflag = false
        btnAdd.setOnClickListener {
            //  clickflag = true

            val t = thread {
                val connection = Socket(ip(), 5050)
                //val reader = connection.getInputStream()
                val writer = connection.getOutputStream()
                writer.write(gestureNameTxt.text.toString().toByteArray())
                connection.close()
            }
            t.join()
        }


        /*
        bckBtn.setOnClickListener {
            val th = thread {
                val connection = Socket(ip(), 5050)
                //val reader = connection.getInputStream()
                val writer = connection.getOutputStream()
                writer.write("0".toByteArray())
                connection.close()

            }
            th.join()


            val intent = Intent(this, Interface::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            applicationContext.startActivity(intent)
            finish()

        }*/
    }

/*
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

 */
}
