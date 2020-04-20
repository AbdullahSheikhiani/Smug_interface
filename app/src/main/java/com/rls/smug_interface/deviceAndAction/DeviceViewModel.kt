package com.rls.smug_interface.deviceAndAction

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rls.smug_interface.utilities.EssenceViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.Socket
import kotlin.concurrent.thread

class DeviceViewModel : EssenceViewModel() {

    val deviceList = MutableLiveData<ArrayList<String>>()
    val gestureList = MutableLiveData<ArrayList<String>>()

    //val deviceTypeList = MutableLiveData<ArrayList<String>>()
    val deviceAdderList = MutableLiveData<ArrayList<String>>()


    fun getDeviceList() {
        viewModelScope.launch(Dispatchers.Default) {
            val a = ArrayList<String>()
            println("thread device list")
            thread {
                var connection = Socket(ip(), port_main)
                println(connection)
                val writer = connection.getOutputStream()
                writer.write("9".toByteArray())
                println("sent 9")
                connection.close()
                Thread.sleep(1)
                connection = Socket(ip(), port_service)
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
                var connection = Socket(ip(), port_main)
                val writer = connection.getOutputStream()
                writer.write("12".toByteArray())
                println("sent 12, request to get unlinked gestures")
                connection.close()
                //Thread.sleep(0)
                connection = Socket(ip(), port_service)
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

    fun getDeviceStates() {
        viewModelScope.launch(Dispatchers.Default) {
            thread {
                var connection = Socket(ip(), port_main)
                val writer = connection.getOutputStream()
                writer.write("15".toByteArray())
                println("sent 15, request to get state")
                connection.close()

                //todo logic
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
            println(gestureName)
            println(devices)
            println(attr)
            println(values)
            val th = thread {
                print("sent to device")

                var connection = Socket(ip(), port_main)
                var writer = connection.getOutputStream()
                writer.write("10".toByteArray())
                println("send 10 for add action ")
                connection.close()

                Thread.sleep(1)

                connection = Socket(ip(), port_service)
                writer = connection.getOutputStream()
                writer.write((gestureName + "\n").toByteArray())
                writer.flush()
                for (i in devices.indices) {
                    println("${devices[i]},${attr[i]},${values[i]}")
                    writer.write(("${devices[i]},${attr[i]},${values[i]}\n").toByteArray())
                }
                writer.flush()
                connection.close()
            }
        }
    }

    fun issueLiveCommand(ieeeAddr: String, attribute: String, value: String) {
        viewModelScope.launch(Dispatchers.Default) {
            thread {
                var connection = Socket(ip(), port_main)

                var writer = connection.getOutputStream()
                writer.write("17".toByteArray())
                println("sent 17, request live action")
                connection.close()
                //Thread.sleep(0)

                println("action requested: $ieeeAddr $attribute $value")
                connection = Socket(ip(), port_service)
                println(connection)
                writer = connection.getOutputStream()
                /*
                writer.write((ieeeAddr + "\n").toByteArray())
                writer.write((attribute + "\n").toByteArray())
                writer.write((value + "\n").toByteArray())
                 */
                writer.write("$ieeeAddr,$attribute,$value".toByteArray())
                println("wrote")

                writer.flush()
            }
        }
    }

    fun getDeviceAddrList() {
        viewModelScope.launch(Dispatchers.Default) {
            val a = ArrayList<String>()
            //todo make the actual implementation
            thread {
                var connection = Socket(ip(), port_main)
                val writer = connection.getOutputStream()
                writer.write("16".toByteArray())
                println("sent 12, request to get unlinked gestures")
                connection.close()
                //Thread.sleep(0)
                connection = Socket(ip(), port_service)
                println(connection)
                val reader = connection.getInputStream()
                val b = reader.bufferedReader()
                var x = b.readLine()
                while (x != "stp") {
                    a.add(x)
                    x = b.readLine()
                }
                deviceAdderList.postValue(a)
                getDeviceStates()
            }
        }
    }

}