package com.rls.smug_interface

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_add_user.*
import java.net.Socket
import kotlin.concurrent.thread

class AddUser : AppCompatActivity() {
    fun loadIP(): String? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        return sharedPreferences.getString("ip", "192.168.4.1")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)
//        val ip = "192.168.0.186"
        var clickflag = false
        this.btnSendUser.setOnClickListener {
            clickflag = true
            val t = thread {
                val connection = Socket(loadIP(), 5050)
                //val reader = connection.getInputStream()
                val writer = connection.getOutputStream()
                writer.write(editText.text.toString().toByteArray())
                connection.close()
            }
            t.join()
        }
    }
}
