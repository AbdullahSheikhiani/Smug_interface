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

class DeviceViewModel : ViewModel() {

    val deviceList = MutableLiveData<ArrayList<String>>()
    val gestureList = MutableLiveData<ArrayList<String>>()
    //val deviceTypeList = MutableLiveData<ArrayList<String>>()
    //val deviceIeeeAddressList = MutableLiveData<ArrayList<String>>()

    fun ip(): String {

        val host = "pspspspi"
        lateinit var ipas: String
        return try {
            //print("INSIDE SHOW IP ADDRESS")
            val t = thread {
                ipas = Inet4Address.getByName(host).hostAddress
            }
            t.join()
            //ipas

            //work around
            return if (ipas == "127.0.1.1")
                "192.168.1.126"
            else
                ipas
        } catch (ex: Exception) {
            println(ex.message)
            "192.168.1.126"
        }
    }

    fun getDeviceList() {
        viewModelScope.launch(Dispatchers.Default) {
            val a = ArrayList<String>()
            println("thread device list")
            thread {

                var connection = Socket(ip(), 5051)
                print(connection)
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
                println("posting device array value")
                deviceList.postValue(a)
                /*
                a.clear()
                x = b.readLine()
                while (x != "stp") {
                    a.add(x)
                    x = b.readLine()
                }
                deviceTypeList.postValue(a)
                a.clear()
                x = b.readLine()
                while (x != "stp") {
                    a.add(x)
                    x = b.readLine()
                }
                deviceIeeeAddressList.postValue(a)
                 */

                connection.close()
            }

        }
    }

    fun getListOfUnLinkedGestures() {
        viewModelScope.launch(Dispatchers.Default) {
            val a = ArrayList<String>()
            thread {
                var connection = Socket(ip(), 5051)
                val writer = connection.getOutputStream()
                writer.write("12".toByteArray())
                println("sent 12, request to get unlinked gestures")
                connection.close()
                //Thread.sleep(0)
                connection = Socket(ip(), 5050)
                println(connection)
                val reader = connection.getInputStream()
                val b = reader.bufferedReader()
                var x = b.readLine()
                while (x != "stp") {
                    a.add(x)
                    x = b.readLine()
                }
                gestureList.postValue(a)
            }
        }
    }

    fun setGestureAssociation(
        gestureName: String,
        devices: ArrayList<String>,
        attr: ArrayList<String>,
        values: ArrayList<String>
    ) {
        viewModelScope.launch(Dispatchers.Default) {
            val th = thread {
                print("sent to device")
                val connection = Socket(ip(), 5050)
                val writer = connection.getOutputStream()
                writer.write((gestureName + "\n").toByteArray())
                for (i in devices.indices) {
                    writer.write((devices[i] + "\n").toByteArray())
                    writer.write((attr[i] + "\n").toByteArray())
                    writer.write((values[i] + "\n").toByteArray())
                    writer.flush()
                }
                connection.close()
            }
        }
    }
}
