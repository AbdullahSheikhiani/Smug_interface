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
    private val devices = ArrayList<String>()
    private val attr = ArrayList<String>()
    private val vals = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_associate_gesture)
        gstName = intent.getStringExtra("EXTRA_SESSION_ID")
        gestureName.text = gstName
        //call add device to get device
        val intent = Intent(baseContext, AddDevice::class.java)
        startActivityForResult(intent, 1)

        addComBtn.setOnClickListener {
            val t = thread {
                //todo make this elegant & fix part in pi
                //this part just emulates the process without using it
                var connection = Socket(ip(), 5050)
                var writer = connection.getOutputStream()
                writer = connection.getOutputStream()
                writer.write("AIG".toByteArray())
                connection.close()
                connection = Socket(ip(), 5051)
                writer = connection.getOutputStream()
                writer.write("10".toByteArray())
                connection.close()
                connection = Socket(ip(), 5050)
                print(connection)
                val reader = connection.getInputStream()
                val b = reader.bufferedReader()
                var x = b.readLine()
                println(x)
                while (x != "stp") {
                    x = b.readLine()
                }
                println("got list of gestures")
                connection.close()
            }
            t.join()
            //call add action or add device? but save string of listofComTxt somehow
            val intentR = Intent(baseContext, AddDevice::class.java)
            //intentR.putExtra("EXTRA_SESSION_ID", gstName)
            startActivityForResult(intentR, 1)
        }
        saveBtn.setOnClickListener {
            val th = thread {
                print("sent to device")
                print(listOfComTxt.text)
                val connection = Socket(ip(), 5050)
                val writer = connection.getOutputStream()
                writer.write((this.gstName + "\n").toByteArray())
                //TODO move to save and apply btn
                //TODO create add action loop
                for (i in devices.indices) {
                    writer.write((devices[i] + "\n").toByteArray())
                    writer.write((attr[i] + "\n").toByteArray())
                    writer.write((vals[i] + "\n").toByteArray())
                    //writer.write("stp\n".toByteArray())
                    writer.flush()
                }
                connection.close()
            }
            th.join()
            val intentRt = Intent(this, Interface::class.java)
            intentRt.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            applicationContext.startActivity(intentRt)
            finish()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //TODO add things to an array and send on button pressed
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val device = data?.getStringExtra("device")
            val attribute = data?.getStringExtra("attribute")
            val value = data?.getStringExtra("value")
            if (device != null) {
                devices.add(device)
                print("device =")
                println(devices)
            }
            if (attribute != null) {
                attr.add(attribute)
                print("attr=")
                println(attr)
            }
            if (value != null) {
                vals.add(value)
                print("vals= ")
                println(vals)
            }
            print("device =")
            //fixme: setText :^)
            listOfComTxt.text =
                "${listOfComTxt.text}\n${device.toString()},${attribute.toString()},${value.toString()}"
            println(device)
        }
    }
}



