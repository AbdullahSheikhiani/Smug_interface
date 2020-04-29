package com.rls.smug_interface.utilities

import androidx.lifecycle.ViewModel
import org.jetbrains.annotations.TestOnly
import java.net.Inet4Address
import kotlin.concurrent.thread

open class EssenceViewModel : ViewModel() {
    val port_main = 5051
    val port_service = 5050
    fun ip(): String {
        return "192.168.1.254"

        val host = "pspspspi"
        lateinit var ipas: String
        return try {
            //print("INSIDE SHOW IP ADDRESS")
            val t = thread {
                ipas = Inet4Address.getByName(host).hostAddress
            }
            t.join()
            ipas
        } catch (ex: Exception) {
            println(ex.message)
            "192.168.4.1"
        }
    }
}