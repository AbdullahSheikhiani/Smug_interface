package com.rls.smug_interface

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_device.*
import java.net.Inet4Address
import java.net.Socket
import kotlin.concurrent.thread


class AddDevice : AppCompatActivity() {
    fun ip(): String {
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
            "192.168.4.1"
        }
    }

    private var success = false
    override fun onCreate(savedInstanceState: Bundle?) {
        //TODO add refresh button
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_device)

        val listOfDevices = ArrayList<String>()
        val adapter = ArrayAdapter(
            this, // Context
            android.R.layout.simple_expandable_list_item_1, // Layout
            listOfDevices // Array
        )
        deviceList.adapter = adapter
        val t = thread {
            println("thread")
            val connection = Socket(ip(), 5050)
            println(connection)
            val reader = connection.getInputStream()
            val b = reader.bufferedReader()
            var x = b.readLine()
            while (x != "stp") {
                listOfDevices.add(x)
                x = b.readLine()
            }
            connection.close()
            success = true
        }
        t.join()
        adapter.notifyDataSetChanged()
        println("\ngot list of devices")
        for (i in listOfDevices) {
            print("d: ")
            println(i)
        }
        //get attribute and value
        val a = "state"
        val v = "On"
        //need button instead of this

        var called = true
        if (called) {
            deviceList.setOnItemClickListener { parent, view, position, id ->
                print("device = ")
                println(listOfDevices[position])
                val returnIntent = Intent()
                returnIntent.putExtra("device", listOfDevices[position])
                returnIntent.putExtra("attribute", a)
                returnIntent.putExtra("value", v)
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (!success) {
            val th = thread {
                val connection = Socket(ip(), 5050)
                //val reader = connection.getInputStream()
                val writer = connection.getOutputStream()
                writer.write("0".toByteArray())
                connection.close()

            }
            th.join()
            println("sent 0 to return to Interface")
        }
        val intent = Intent(this, Interface::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        applicationContext.startActivity(intent)
        finish()
    }


}
