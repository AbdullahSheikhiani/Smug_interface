package com.rls.smug_interface

import android.content.Context
import android.content.Intent
import android.net.DhcpInfo
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.Looper
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_list_gestures.*
import java.net.Inet4Address
import java.net.Inet6Address
import java.net.InetAddress
import java.net.Socket
import kotlin.concurrent.thread


class ListGestures : AppCompatActivity() {
    fun IP(): String? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        return sharedPreferences.getString("ip", "192.168.4.1")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_gestures)
        //sleep(100)
        //val ip = InetAddress.getByName("pspsps")
        //val x:DnsResolver();
        //println('x')
        //println(ip)
        val a = ArrayList<String>()
        val t = thread {
            //val wifi = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            //val info: DhcpInfo = wifi.dhcpInfo
            //print("info= ")
            //println(info.dns1)
            //println(info.dns2.toString())
            //val ipas = InetAddress.getByName("pspspspi")
            //print("ipa= ")
            //println(ipas)
            /*
            for (ipa in ipas) {
                print(when (ipa) {
                    is Inet4Address -> "  ipv4 : "
                    is Inet6Address -> "  ipv6 : "
                    else            -> "  ipv? : "
                })
                print("host address=")
                println(ipa)
            }
            */
            Looper.prepare()
            println("THREAD")
            print("IP= ")
            //val ip = "192.168.0.134"
            println(IP())
            val connection = Socket(IP(), 5050)
            print(connection)
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
        println("\nlist reading finished")
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
