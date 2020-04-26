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


    val deviceStates = MutableLiveData<ArrayList<String>>()
    val deviceAddrList = MutableLiveData<ArrayList<String>>()

    val deviceList2 = MutableLiveData<ArrayList<String>>()
    val deviceAddrList2 = MutableLiveData<ArrayList<String>>()

    fun getDeviceList(flag: Int) {
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
                if (flag == 1)
                    deviceList.postValue(a)
                else
                    deviceList2.postValue(a)

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


    fun getDeviceStates(deviceAddrList: ArrayList<String>, deviceNameList: ArrayList<String>) {
        viewModelScope.launch(Dispatchers.Default) {
            val states = ArrayList<String>()
            thread {
                var connection = Socket(ip(), port_main)
                var writer = connection.getOutputStream()
                writer.write("15".toByteArray())
                println("sent 15, request to get state")
                writer.close()
                connection.close()

                for (i in 0 until deviceAddrList.size) {
                    connection = Socket(ip(), port_service)
                    writer = connection.getOutputStream()
                    if (deviceNameList[i].contains("plug"))
                        writer.write("${deviceAddrList[i]},1".toByteArray())
                    else
                        writer.write("${deviceAddrList[i]},0".toByteArray())

                    val reader = connection.getInputStream()
                    val x = reader.readBytes().toString(Charsets.UTF_8)
                    //println("x: $x")
                    //print(x)
                    states.add(x)
                    //println("states: $states")
                    writer.close()
                    reader.close()
                    connection.close()
                }
                //println("00states: $states")
                connection = Socket(ip(), port_service)
                writer = connection.getOutputStream()
                writer.write("stp".toByteArray())
                writer.flush()
                println("wrote STP")

                writer.close()
                connection.close()

                deviceStates.postValue(states)

                //deviceStates.postValue(states)
                //println("STATES:$states ")
                //println(states)
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
            thread {
                print("sent to device")

                var connection = Socket(ip(), port_main)
                var writer = connection.getOutputStream()
                writer.write("10".toByteArray())
                println("send 10 for add action ")
                writer.close()
                connection.close()

                connection = Socket(ip(), port_service)
                writer = connection.getOutputStream()
                val reader = connection.getInputStream()
                println(writer.write((gestureName + "\n").toByteArray()))

                var ack = ""
                println("sent gesture name")
                while (!ack.contains("ack")) {
                    for (i in 0 until devices.size) {
                        println("${devices[i]},${attr[i]},${values[i]}")
                        println(writer.write(("${devices[i]},${attr[i]},${values[i]}\n".toByteArray())))
                        println("After writer\ni= $i")
                    }
                    ack = reader.readBytes().toString(Charsets.UTF_8)
                    println("ack = $ack")
                }
                writer.flush()
                connection.close()
            }
        }
    }

    fun addDevice() {
        viewModelScope.launch(Dispatchers.Default) {
            thread {
                val connection = Socket(ip(), port_main)

                val writer = connection.getOutputStream()
                writer.write("13".toByteArray())
                println("sent 13, request discover devices")
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
                var temp = value
                if (attribute.contains("brightness")) {
                    var tmp = value.toInt()
                    if (tmp < 9)
                        temp = "10";
                }
                println("action requested: $ieeeAddr $attribute $temp")
                connection = Socket(ip(), port_service)
                println(connection)
                writer = connection.getOutputStream()
                /*
                writer.write((ieeeAddr + "\n").toByteArray())
                writer.write((attribute + "\n").toByteArray())
                writer.write((value + "\n").toByteArray())
                 */
                writer.write("$ieeeAddr,$attribute,$temp".toByteArray())
                println("wrote")

                writer.flush()
                writer.close()
                connection.close()
            }
        }
    }

    fun getDeviceAddrList(flag: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            val a = ArrayList<String>()
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
                if (flag == 1)
                    deviceAddrList.postValue(a)
                else
                    deviceAddrList2.postValue(a)

            }
        }
    }

    fun removeDevice(deviceAddr: String) {
        viewModelScope.launch(Dispatchers.Default) {
            thread {
                var connection = Socket(ip(), port_main)
                var writer = connection.getOutputStream()
                writer.write("14".toByteArray())
                println("requested 14, remove iot device")
                writer.close()
                connection.close()

                connection = Socket(ip(), port_service)
                writer = connection.getOutputStream()
                writer.write(deviceAddr.toByteArray())
                println("sent removed device address")
                writer.close()
                connection.close()

            }
        }
    }
}