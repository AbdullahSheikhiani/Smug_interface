package com.rls.smug_interface

import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.rls.smug_interface.deviceAndAction.DeviceViewModel
import com.rls.smug_interface.utilities.ColorPickerDialog
import java.net.Inet4Address
import kotlin.concurrent.thread

class MainUI : AppCompatActivity(), ColorPickerDialog.ColorListener {
    lateinit var viewModel: DeviceViewModel

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
            val intent = Intent(this, InitialSetup::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            applicationContext.startActivity(intent)
            finish()
            //todo fix ip
            "192.168.1.126"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_ui)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        viewModel = ViewModelProvider(this).get(DeviceViewModel::class.java)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_user, R.id.navigation_gesture, R.id.navigation_device
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun saveColor(color: String, code: String) {
        print("save color $color")
        viewModel.issueLiveCommand(code, "color", color)
        //todo pass through view model to live action

    }
}
