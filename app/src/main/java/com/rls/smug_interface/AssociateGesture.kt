package com.rls.smug_interface

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_associate_gesture.*
import java.net.Inet4Address
import java.net.Socket
import kotlin.concurrent.thread


class AssociateGesture : AppCompatActivity() {
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

    lateinit var gstName: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_associate_gesture)
        gstName = intent.getStringExtra("EXTRA_SESSION_ID")
        gestureName.text = gstName
        //call add device to get device
        val intent = Intent(baseContext, AddDevice::class.java)
        startActivityForResult(intent, 1)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val device = data?.getStringExtra("result")
            print("device =")
            listOfComTxt.text = device.toString()
            println(device)
            val th = thread {
                val connection = Socket(ip(), 5050)
                val writer = connection.getOutputStream()
                writer.write(gstName.toByteArray())
                writer.write("\n".toByteArray())
                //TODO move to save and apply btn
                //TODO create add action loop
                writer.write("GO\n".toByteArray())
                writer.write("state\n".toByteArray())
                writer.write("On\n".toByteArray())
                writer.write("stp\n".toByteArray())
                writer.flush()
                connection.close()
            }
            th.join()
        }
    }
}



