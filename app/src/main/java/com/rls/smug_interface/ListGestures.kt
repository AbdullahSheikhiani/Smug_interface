package com.rls.smug_interface

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_list_gestures.*
import java.net.Inet4Address
import java.net.Socket
import kotlin.concurrent.thread


class ListGestures : AppCompatActivity() {
    /*fun IP(): String? {
      val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
      return sharedPreferences.getString("ip", "192.168.4.1")
  }*/

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
            println(ex.message)
            "192.168.1.126"
        }
    }

    private var success = false

    //lateinit var viewModel: NetworkHandlerViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_gestures)
        //viewModel = ViewModelProvider(this).get(NetworkHandlerViewModel::class.java)
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
            //Looper.prepare()
            println("THREAD")
            print("IP= ")
            //val ip = "192.168.0.134"
            println(ip())
            val connection = Socket(ip(), 5050)
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
            success = true
        }
        t.join()
        println("\nlist reading finished")
        val adapter = ArrayAdapter(
            this, // Context
            android.R.layout.simple_expandable_list_item_1, // Layout
            a // Array
        )
        listGstView.adapter = adapter
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
