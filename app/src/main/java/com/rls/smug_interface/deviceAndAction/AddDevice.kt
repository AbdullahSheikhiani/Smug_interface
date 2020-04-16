package com.rls.smug_interface.deviceAndAction

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.rls.smug_interface.Interface
import com.rls.smug_interface.R
import com.rls.smug_interface.utilities.ColorHandler
import com.rls.smug_interface.utilities.OnOffDialog
import com.rls.smug_interface.utilities.YesNoDialog
import kotlinx.android.synthetic.main.activity_add_device.*
import java.net.Inet4Address
import java.net.Socket
import kotlin.concurrent.thread


class AddDevice : AppCompatActivity(), YesNoDialog.NoticeDialogListener,
    OnOffDialog.NoticeDialogListener {

    fun ip(): String {
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
            //println(ex.message)
            "192.168.4.1"
        }
    }

    private val returnIntent = Intent()
    private var success = false
    override fun onCreate(savedInstanceState: Bundle?) {
        //TODO add refresh button
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_device)
        var listOfDevices = ArrayList<String>()

        val adapter = ArrayAdapter(
            this, // Context
            android.R.layout.simple_expandable_list_item_1, // Layout
            listOfDevices // Array
        )
        deviceList.adapter = adapter
        /*
        val t = thread {

            println("thread add device")
            val connection = Socket(ip(), 5050)
            println(connection)
            val reader = connection.getInputStream()
            val b = reader.bufferedReader()
            var x = b.readLine()
            while (x != "stp") {
                listOfDevices.add(x)
                x = b.readLine()
            }
            connection.close()
            success = true
            adapter.notifyDataSetChanged()
        }
        t.join()
        */
        println("\ngot list of devices")
        for (i in listOfDevices) {
            print("d: ")
            println(i)
        }

        //get attribute and value
        //val a = "state"
        val v = "On"
        //need button instead of this
        var flag = true
        deviceList.setOnItemClickListener { _, _, position, _ ->
            if (flag) {
                print("device = ")
                println(listOfDevices[position])
                returnIntent.putExtra("device", listOfDevices[position])
                if (listOfDevices[position].contains("Smart plug")) {
                    listOfDevices.clear()
                    listOfDevices.add("Turn on")
                    listOfDevices.add("Turn off")
                    adapter.notifyDataSetChanged()
                } else {
                    listOfDevices.clear()
                    listOfDevices.add("Turn on/Off")
                    listOfDevices.add("Change color")
                    listOfDevices.add("Change brightness")
                    adapter.notifyDataSetChanged()
                }
                flag = false
            } else
            //handle what attribute is clicked
                when {
                    listOfDevices[position] == "Change color" -> {
                        //todo show color dialog
                        println("CHANGE COLOR")
                        val colorIntent = Intent(baseContext, ColorHandler::class.java)
                        startActivityForResult(colorIntent, 2)
                    }
                    listOfDevices[position] == "Change brightness" -> {
                        //todo show slider dialog
                        returnIntent.putExtra("attribute", "brightness")
                        returnIntent.putExtra("value", "180")
                        setResult(Activity.RESULT_OK, returnIntent)
                        finish()
                    }
                    listOfDevices[position] == "Turn on/Off" -> {
                        showOnOff()
                        returnIntent.putExtra("attribute", "state")
                        if (onOff != "")
                            returnIntent.putExtra("value", onOff)
                        else
                            returnIntent.putExtra("value", "off")
                        setResult(Activity.RESULT_OK, returnIntent)
                        finish()
                    }
                    listOfDevices[position] == "Turn on" -> {
                        returnIntent.putExtra("attribute", "state")
                        returnIntent.putExtra("value", "on")
                        setResult(Activity.RESULT_OK, returnIntent)
                        finish()
                    }
                    listOfDevices[position] == "Turn off" -> {
                        returnIntent.putExtra("attribute", "state")
                        returnIntent.putExtra("value", "off")
                        setResult(Activity.RESULT_OK, returnIntent)
                        finish()
                    }
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2) {
            val color = data?.getStringExtra("color")
            print("color =")
            println(color)
            returnIntent.putExtra("attribute", "color")
            returnIntent.putExtra("value", color)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (!success) {
            val th = thread {
                val connection = Socket(ip(), 5050)
                //val reader = connection.getInputStream()
                val writer = connection.getOutputStream()
                writer.write("0".toByteArray())
                connection.close()
            }
            th.join()
            println("sent 0 to return to Interface")
        }
        val intent = Intent(this, Interface::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        applicationContext.startActivity(intent)
        finish()
    }

    private var yesNoAnsser = ""
    private fun showYesNo() {
        val dialog = YesNoDialog()
        dialog.show(supportFragmentManager, "yesno")
    }

    override fun onDialogPositiveClick(answer: String) {
        println("user pressed ON")
        print(answer)
        yesNoAnsser = "yes"
    }

    override fun onDialogNegativeClick(answer: String) {
        println("user pressed OFF")
        print(answer)
        yesNoAnsser = "no"
    }

    private var onOff = ""
    private fun showOnOff() {
        val dialog = OnOffDialog()
        dialog.show(supportFragmentManager, "onoff")
    }

    override fun onOnClick(answer: String) {
        onOff = "on"
    }

    override fun onOffClick(answer: String) {
        onOff = "off"
    }

}
