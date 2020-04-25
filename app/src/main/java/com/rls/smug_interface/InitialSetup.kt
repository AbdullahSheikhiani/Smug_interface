package com.rls.smug_interface

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_initial_setup.*
import java.net.Socket
import kotlin.concurrent.thread


class InitialSetup : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        //val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial_setup)
        val networks = arrayOf(
            "getting networks list "
        )
        print("loadIP value: ")

        var adapter = ArrayAdapter(
            this, // Context
            android.R.layout.simple_spinner_item, // Layout
            networks // Array
        )

        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

        spinner0.adapter = adapter


        val networkList = ArrayList<String>()
        val t = thread {
            var connection = Socket("192.168.4.1", 5051)
            var writer = connection.getOutputStream()
            writer.write("8".toByteArray())
            connection.close()

            connection = Socket("192.168.4.1", 5005)
            val reader = connection.getInputStream()
            writer = connection.getOutputStream()
            writer.write("conf".toByteArray())

            val s = reader.bufferedReader()
            var x = s.readLine()
            print("x= ")
            println(x)
            while (x != "stp") {
                networkList.add(x)
                x = s.readLine()
                println(x)
            }

            connection.close()
        }
        t.join()

        adapter = ArrayAdapter(
            this, // Context
            android.R.layout.simple_spinner_item, // Layout
            networkList // Array
        )
        adapter.notifyDataSetChanged()
        spinner0.adapter = adapter
        println(networks)

        reloadBtn.setOnClickListener {
            val th = thread {
                //TODO add handling clicking reload on server side

                val connection = Socket("192.168.4.1", 5005)
                val reader = connection.getInputStream()
                val writer = connection.getOutputStream()
                writer.write("conf".toByteArray())

                val s = reader.bufferedReader()
                var x = s.readLine()
                print("x= ")
                println(x)
                while (x != "stp") {
                    networkList.add(x)
                    x = s.readLine()
                    println(x)
                }
                connection.close()
            }
            th.join()
            adapter.notifyDataSetChanged()

        }
        confBtn.setOnClickListener {
            val th = thread {


                val connection = Socket("192.168.4.1", 5005)
                val reader = connection.getInputStream()
                val writer = connection.getOutputStream()
                writer.write(spinner0.selectedItem.toString().toByteArray())
                val s = reader.bufferedReader()
                println()
                print("ack: ")
                println(s.readLine())
                println(spinner0.selectedItem)
                writer.write(inputPassword.text.toString().toByteArray())
                println(inputPassword.text)
                val ip = s.readLine()
                print("ip value: ")
                println(ip)
            }
            th.join()
            intent = Intent(this, MainUI::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }
}
