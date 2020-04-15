package com.rls.smug_interface.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rls.smug_interface.Interface
import com.rls.smug_interface.R
import kotlinx.android.synthetic.main.activity_add_user.*
import java.net.Inet4Address
import java.net.Socket
import kotlin.concurrent.thread

class AddUser : AppCompatActivity() {
    /*fun IP(): String? {
      val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
      return sharedPreferences.getString("ip", "192.168.4.1")
  }*/

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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)
//        val ip = "192.168.0.186"
        //var clickflag = false
        this.btnSendUser.setOnClickListener {
           // clickflag = true
            val t = thread {
                val connection = Socket(ip(), 5050)
                //val reader = connection.getInputStream()
                val writer = connection.getOutputStream()
                writer.write(addUserEditText.text.toString().toByteArray())
                connection.close()
            }
            t.join()
        }
    }
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
}
