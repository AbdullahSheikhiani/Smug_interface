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

    private val returnIntent = Intent()
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
        var flag = true
        deviceList.setOnItemClickListener { parent, view, position, id ->
            if (flag) {
                print("device = ")
                println(listOfDevices[position])
                returnIntent.putExtra("device", listOfDevices[position])
                if (listOfDevices[position].contains("Smart plug")) {
                    listOfDevices.clear()
                    listOfDevices.add("Turn on")
                    listOfDevices.add("Turn off")
                    adapter.notifyDataSetChanged()
                } else {
                    listOfDevices.clear()
                    listOfDevices.add("Turn on/Off")
                    listOfDevices.add("Change color")
                    listOfDevices.add("Change brightness")
                    adapter.notifyDataSetChanged()
                }
                flag = false
            } else {
                //handle what attribute is clicked
                when {
                    listOfDevices[position] == "Change color" -> {
                        //todo
                        println("CHANGE COLOR")
                        val colorIntent = Intent(baseContext, ColorHandler::class.java)
                        startActivityForResult(colorIntent, 2)
                    }
                    listOfDevices[position] == "Change brightness" -> {
                        //todo
                        returnIntent.putExtra("attribute", "brightness")
                        returnIntent.putExtra("value", v)
                        setResult(Activity.RESULT_OK, returnIntent)
                        finish()
                    }
                    listOfDevices[position] == "Turn on/Off" -> {
                        //todo
                        returnIntent.putExtra("attribute", "state")
                        returnIntent.putExtra("value", v)
                        setResult(Activity.RESULT_OK, returnIntent)
                        finish()
                    }
                    listOfDevices[position] == "Turn on" -> {
                        returnIntent.putExtra("attribute", "state")
                        returnIntent.putExtra("value", "on")
                        setResult(Activity.RESULT_OK, returnIntent)
                        finish()
                    }
                    listOfDevices[position] == "Turn off" -> {
                        returnIntent.putExtra("attribute", "state")
                        returnIntent.putExtra("value", "off")
                        setResult(Activity.RESULT_OK, returnIntent)
                        finish()
                    }
                }
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2) {
            val color = data?.getStringExtra("color")
            print("color =")
            println(color)
            returnIntent.putExtra("attribute", "color")
            returnIntent.putExtra("value", color)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
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
