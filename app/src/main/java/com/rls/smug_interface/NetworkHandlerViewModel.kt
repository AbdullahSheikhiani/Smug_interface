package com.rls.smug_interface

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.Socket

class NetworkHandlerViewModel : ViewModel() {

    fun fetchData() {
        viewModelScope.launch(Dispatchers.Default) {

        }

    }

    fun sendData(s: String) {
        viewModelScope.launch(Dispatchers.Default) {
            println("THREAD")
            //print("IP= ")
            val connection = Socket("192.168.1.126", 5050)
            print(connection)
            val reader = connection.getInputStream()
            //val writer = connection.getOutputStream()
            val b = reader.bufferedReader()
            //println("AFTER BUFF")
            var x = b.readLine()
            print("X= ")
            println(x)
            while (x != "stp") {
                //a.add(x)
                print("X= ")
                println(x)
                x = b.readLine()
                //print("x=")
                //println(x)
            }
            connection.close()
        }

    }
}
