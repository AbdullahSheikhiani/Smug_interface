package com.rls.smug_interface

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.preference.PreferenceManager
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_remove_user.*
import java.net.Socket
import kotlin.concurrent.thread

class RemoveUser : AppCompatActivity() {
    fun IP(): String? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        return sharedPreferences.getString("ip", "192.168.4.1")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remove_user)
        val a = ArrayList<String>()
        rldBtn.setOnClickListener {
            val t = thread {
                println("THREAD")
                val connection = Socket(IP(), 5050)
                val reader = connection.getInputStream()
                //val writer = connection.getOutputStream()
                val b = reader.bufferedReader()
                //list users
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
            val adapter = ArrayAdapter(
                this, // Context
                android.R.layout.simple_spinner_item, // Layout
                a // Array
            )
            adapter.notifyDataSetChanged()
            spinnerrmUsr.adapter = adapter
        }
        btnRemove.setOnClickListener {
            val t = thread {
                val connection = Socket(IP(), 5050)
                val reader = connection.getInputStream()
                val writer = connection.getOutputStream()
                writer.write(spinnerrmUsr.selectedItem.toString().toByteArray())
                val r = reader.bufferedReader()
                if (r.readLine() == "NAK") {
                    Looper.prepare()
                    Toast.makeText(
                        applicationContext,
                        "User ${spinnerrmUsr.selectedItem} not found",
                        Toast.LENGTH_SHORT
                        //applicationContext,
                        //wifiManager.scanResults[0].toString(),
                        //Toast.LENGTH_LONG

                    ).show()
                } else {
                    Looper.prepare()
                    Toast.makeText(
                        applicationContext,
                        "User ${spinnerrmUsr.selectedItem} removed",
                        Toast.LENGTH_SHORT
                        //applicationContext,
                        //wifiManager.scanResults[0].toString(),
                        //Toast.LENGTH_LONG

                    ).show()

                }

                println(r.readLine())
            }
            t.join()

        }
        btnBack.setOnClickListener {
            val intent = Intent(this, Interface::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }
}
