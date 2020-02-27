package com.rls.smug_interface

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_add_user.*
import java.net.Socket
import kotlin.concurrent.thread

class addUser : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)
        val ip = "192.168.0.186"
        var clickflag = false
        this.btnSendUser.setOnClickListener {
            clickflag = true
            val t = thread {
                val connection = Socket(ip, 5050)
                //val reader = connection.getInputStream()
                val writer = connection.getOutputStream()
                writer.write(editText.text.toString().toByteArray())
                connection.close()
            }
            t.join()
        }
    }
}
