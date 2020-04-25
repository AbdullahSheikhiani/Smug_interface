package com.rls.smug_interface.user

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rls.smug_interface.utilities.EssenceViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.Socket
import kotlin.concurrent.thread

class UserViewModel : EssenceViewModel() {
    val fingerPrintConfirmation = MutableLiveData<String>()
    val userList = MutableLiveData<ArrayList<String>>()

    fun getUserList() {
        val a = ArrayList<String>()
        thread {
            println("list user THREAD")
            var connection = Socket(ip(), 5051)
            val writer = connection.getOutputStream()
            writer.write("11".toByteArray())
            connection.close()
            connection = Socket(ip(), 5050)
            val reader = connection.getInputStream()
            val b = reader.bufferedReader()
            //println("AFTER BUFF")
            var x = b.readLine()
            while (x != "stp") {
                a.add(x)
                x = b.readLine()
            }
            connection.close()
            userList.postValue(a)
            println("list reading finished")
        }
    }


    fun changeUser(userName: String) {
        thread {
            println("chg user THREAD")
            var connection = Socket(ip(), 5051)
            var writer = connection.getOutputStream()
            writer.write("5".toByteArray())
            connection.close()
            Thread.sleep(0.1.toLong())
            connection = Socket(ip(), 5050)
            writer = connection.getOutputStream()
            writer.write(userName.toByteArray())
            connection.close()
        }
    }


    fun removeUser(userName: String, context: Context?) {
        viewModelScope.launch(Dispatchers.Default) {
            thread {
                println("remove user THREAD")
                var connection = Socket(ip(), 5051)
                //val reader = connection.getInputStream()
                val writer = connection.getOutputStream()
                writer.write("7".toByteArray())
                connection.close()
                Thread.sleep(10)
                connection = Socket(ip(), 5050)
                val reader = connection.getInputStream()
                //val writer = connection.getOutputStream()
                val buff = reader.bufferedReader()
                val ack = buff.readLine()
                if (ack == "ack") {
                    println("ack")
                    val text = "user $userName removed successfully"
                    val duration = Toast.LENGTH_LONG
                    val toast = Toast.makeText(context, text, duration)
                    toast.show()
                }
            }

        }
    }

    fun getFingerPrintConfirmation() {
        viewModelScope.launch(Dispatchers.Default) {
            thread {
                val connection = Socket(ip(), port_service)
                val reader = connection.getInputStream()
                val b = reader.bufferedReader()
                val x = b.readLine()
                fingerPrintConfirmation.postValue(x)
                print("confirmation for user: $x")
            }

        }

    }

    fun addUser(userName: String) {
        thread {
            var connection = Socket(ip(), 5051)
            var writer = connection.getOutputStream()
            writer.write("6".toByteArray())
            connection.close()
            Thread.sleep(0.1.toLong())
            connection = Socket(ip(), 5050)
            println(connection)
            //val reader = connection.getInputStream()
            writer = connection.getOutputStream()
            writer.write(userName.toByteArray())
            connection.close()
        }.join()
    }

}