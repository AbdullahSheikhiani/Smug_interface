package com.rls.smug_interface.gesture

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.Inet4Address
import java.net.Socket
import kotlin.concurrent.thread

class GestureViewModel : ViewModel() {

    val gestureList = MutableLiveData<ArrayList<String>>()
    val remainingTimes = MutableLiveData<Int>()
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

    fun getGestureList() {
        viewModelScope.launch(Dispatchers.Default) {
            val a = ArrayList<String>()
            println("thread add device")
            thread {
                println("list gestures THREAD")
                print("IP= ")
                println(ip())
                var connection = Socket(ip(), 5050)
                //val reader = connection.getInputStream()
                val writer = connection.getOutputStream()
                writer.write("1".toByteArray())
                connection.close()
                connection = Socket(ip(), 5050)
                val reader = connection.getInputStream()
                val b = reader.bufferedReader()
                var x = b.readLine()
                while (x != "stp") {
                    a.add(x)
                    x = b.readLine()
                }
                connection.close()
                gestureList.postValue(a)

            }
        }
    }

    fun getRemainingTimes() {
        viewModelScope.launch(Dispatchers.Default) {
            var x = ""
            thread {
                val connection = Socket(ip(), 5050)
                val reader = connection.getInputStream()
                val b = reader.bufferedReader()
                x = b.readLine()
            }.join()
            remainingTimes.postValue(x.toInt())
        }
    }


}
