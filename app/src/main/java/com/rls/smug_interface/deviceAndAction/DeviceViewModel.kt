package com.rls.smug_interface.deviceAndAction

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.Inet4Address
import java.net.Socket
import java.nio.channels.AsynchronousChannel
import kotlin.concurrent.thread

class DeviceViewModel : ViewModel(), AsynchronousChannel {

    val deviceList = MutableLiveData<ArrayList<String>>()
    fun ip(): String {

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
            "192.168.1.126"
        }
    }

    fun getDeviceList() {

        viewModelScope.launch(Dispatchers.Default) {
            val a = ArrayList<String>()
            println("thread add device")
            thread {
                var connection = Socket(ip(), 5051)
                val writer = connection.getOutputStream()
                writer.write("9".toByteArray())
                println("sent 9")
                connection.close()
                Thread.sleep(1)

                connection = Socket(ip(), 5050)
                println(connection)
                val reader = connection.getInputStream()
                val b = reader.bufferedReader()
                var x = b.readLine()
                while (x != "stp") {
                    a.add(x)
                    x = b.readLine()
                }
                connection.close()
            }.join()
            deviceList.postValue(a)
        }
    }

    override fun isOpen(): Boolean {
        return true
    }

    override fun close() {
    }
}
