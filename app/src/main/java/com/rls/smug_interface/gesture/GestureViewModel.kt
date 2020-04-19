package com.rls.smug_interface.gesture

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rls.smug_interface.utilities.EssenceViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.Socket
import kotlin.concurrent.thread

class GestureViewModel : EssenceViewModel() {

    val gestureList = MutableLiveData<ArrayList<String>>()
    val remainingTimes = MutableLiveData<Int>()


    fun getGestureList() {
        viewModelScope.launch(Dispatchers.Default) {
            val a = ArrayList<String>()
            println("thread add device")
            thread {
                println("list gestures THREAD")
                print("IP= ")
                println(ip())
                var connection = Socket(ip(), port_main)
                //val reader = connection.getInputStream()
                val writer = connection.getOutputStream()
                writer.write("1".toByteArray())
                connection.close()
                connection = Socket(ip(), port_service)
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
                val connection = Socket(ip(), port_service)
                val reader = connection.getInputStream()
                val b = reader.bufferedReader()
                x = b.readLine()
            }.join()
            remainingTimes.postValue(x.toInt())
        }
    }

    fun removeGesture(gestureToBeRemoved: String) {
        viewModelScope.launch(Dispatchers.Default) {
            val th = thread {
                print("item = ")
                println(gestureToBeRemoved)
                val connection = Socket(ip(), port_service)
                val reader = connection.getInputStream()
                val writer = connection.getOutputStream()
                writer.write(gestureToBeRemoved.toByteArray())
                val r = reader.bufferedReader()
                print("removed gesture read: ")
                println(r.readLine())
            }.join()
        }
    }

    fun addGesture(gestureName: String) {
        val t = thread {
            var connection = Socket(ip(), port_main)
            //val reader = connection.getInputStream()
            var writer = connection.getOutputStream()
            writer.write("2".toByteArray())
            connection.close()
            Thread.sleep(10)
            connection = Socket(ip(), port_service)
            writer = connection.getOutputStream()
            writer.write(gestureName.toByteArray())
            connection.close()
        }
        t.join()
    }


}
