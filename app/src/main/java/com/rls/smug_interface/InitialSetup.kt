package com.rls.smug_interface

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_initial_setup.*
import java.net.Socket
import kotlin.concurrent.thread


class InitialSetup : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        //val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial_setup)
        var networks = arrayOf(
            "getting networks list "
        )
        print("loadIP value: ")

        var adapter = ArrayAdapter(
            this, // Context
            android.R.layout.simple_spinner_item, // Layout
            networks // Array
        )

        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

        spinner0.adapter = adapter


        val networkList = ArrayList<String>()
        val t = thread {

            val connection = Socket("192.168.4.1", 5005)
            val reader = connection.getInputStream()
            val writer = connection.getOutputStream()
            writer.write("conf".toByteArray())

            val s = reader.bufferedReader()
            var x = s.readLine()
            print("x= ")
            println(x)
            while (x != "stp") {
                networkList.add(x)
                x = s.readLine()
                println(x)
            }
            //println("out")
            //writer.write("ACK".toByteArray())
            connection.close()
        }
        t.join()
        /*for (i in networks) {
            println(i)
        }*/

        adapter = ArrayAdapter(
            this, // Context
            android.R.layout.simple_spinner_item, // Layout
            networkList // Array
        )
        adapter.notifyDataSetChanged()
        spinner0.adapter = adapter
        println(networks)

        reloadBtn.setOnClickListener {
            val th = thread {
                //TODO add handling clicking reload on server side
                val connection = Socket("192.168.4.1", 5005)
                val reader = connection.getInputStream()
                val writer = connection.getOutputStream()
                writer.write("conf".toByteArray())

                val s = reader.bufferedReader()
                var x = s.readLine()
                print("x= ")
                println(x)
                while (x != "stp") {
                    networkList.add(x)
                    x = s.readLine()
                    println(x)
                }
                connection.close()
            }
            th.join()
            adapter.notifyDataSetChanged()

        }
        confBtn.setOnClickListener {
            val th = thread {
                val connection = Socket("192.168.4.1", 5005)
                val reader = connection.getInputStream()
                val writer = connection.getOutputStream()
                writer.write(spinner0.selectedItem.toString().toByteArray())
                var s = reader.bufferedReader()
                println()
                print("ack: ")
                println(s.readLine())
                //writer.flush()
                //writer.write("\n".toByteArray())
                println(spinner0.selectedItem)
                writer.write(inputPassword.text.toString().toByteArray())
                println(inputPassword.text)
                //writer.write("\n".toByteArray())
                val ip = s.readLine()
                print("ip value: ")
                println(ip)
            }
            th.join()
            intent = Intent(this, MainUI::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
            //val intent = Intent(this@MainActivity, Interface::class.java)
            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            //applicationContext.startActivity(intent)
            //finish()

            /*
            thread {
                val connection = Socket(oldIP, 5050)
                val reader = connection.getInputStream()
                val writer = connection.getOutputStream()
                //write ssid
                writer.write(spinner0.selectedItem.toString().toByteArray())
                //write password
                writer.write(inputPassword.text.toString().toByteArray())
                //println("out")
                var s = reader.bufferedReader()
                if (s.readLine() == "ACK")
                    writer.write("ACK".toByteArray())
                connection.close()
            }*/
            /*thread {
                 //var fileInputStream: FileInputStream? = openFileInput(file)
                 //var inputStreamReader = InputStreamReader(fileInputStream)

                 //val bufferedReader = BufferedReader(inputStreamReader)
                 //val stringBuilder: StringBuilder = StringBuilder()

                 //var text: String? = null
                 //while ({ text = bufferedReader.readLine(); text }() != null) {
                 //    stringBuilder.append(text)
                 // }
                 //if (stringBuilder != null) {
                 //   val fileOutputStream: FileOutputStream
                 //   try {
                 //      fileOutputStream = openFileOutput(file, Context.MODE_PRIVATE)
                 //     if (newIP != oldIP)
                 //        fileOutputStream.write(newIP.toByteArray())
                 //} catch (e: Exception) {
                 //   e.printStackTrace()
                 // }
                 //}
                 //recieve ip from raspberry pi

                 //TODO make ip something that is set after the connection secured

                 //writer.write(ssid.text.toString().toByteArray())
                 //writer.write(inputPassword.text.toString().toByteArray())
                 val connection = Socket("192.168.4.1", 5051)
                 val writer: OutputStream = connection.getOutputStream()
                 val server = ServerSocket(5050)
                 val client = server.accept()
                 val scanner = Scanner(client.inputStream)
                 while (scanner.hasNextLine()) {
                     newIP = scanner.nextLine()
                     println(newIP)
                     break
                 }
                 server.close()

                 Looper.prepare()
                 Toast.makeText(
                     applicationContext,
                     "Connected to server at $oldIP on port $port, got $newIP",
                     Toast.LENGTH_LONG
                     //applicationContext,
                     //wifiManager.scanResults[0].toString(),
                     //Toast.LENGTH_LONG

                 ).show()
             }*/
            /*writer.write(spinner0.selectedItem.toString().toByteArray())
            writer.write(",".toByteArray())
            writer.write(spinner1.selectedItem.toString().toByteArray())
            writer.write(",".toByteArray())
            writer.write(spinner2.selectedItem.toString().toByteArray())
            writer.write(",".toByteArray())
            writer.write(spinner3.selectedItem.toString().toByteArray())
            writer.write(",".toByteArray())
            writer.write(spinner4.selectedItem.toString().toByteArray())
            writer.write(",".toByteArray())
            writer.write(spinner5.selectedItem.toString().toByteArray())
            writer.flush()
             */
            //writer.write("send".toByteArray())
            //writer.flush()

        }

/*
        val wifiScanReceiver = object : BroadcastReceiver() {

            override fun onReceive(context: Context, intent: Intent) {
                val success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
                if (success) {
                    scanSuccess(wifiManager)
                } else {
                    scanFailure(wifiManager)
                }
            }
        }

        val intentFilter = IntentFilter()
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        applicationContext.registerReceiver(wifiScanReceiver, intentFilter)

        val success = wifiManager.startScan()
        if (!success) {
            // scan failure handling
            scanFailure(wifiManager)
        }
*/

    }
/*
    fun scanSuccess(wifiManager: WifiManager) {
        val results = wifiManager.scanResults
        print(results)
        Toast.makeText(
            applicationContext,
            "AAAAAAAAAAAAAA",
            Toast.LENGTH_SHORT
            //applicationContext,
            //wifiManager.scanResults[0].toString(),
            //Toast.LENGTH_LONG

        ).show()

        // ... use new scan results ...
    }

    fun scanFailure(wifiManager: WifiManager) {
        // handle failure: new scan did NOT succeed
        // consider using old scan results: these are the OLD results!
        val results = wifiManager.scanResults
        //... potentially use older scan results ...
    }
*/

/*
    private fun sendUDP(messageStr: String) {
        //Open a port to send the package
        val socket = DatagramSocket()
        //socket.broadcast = true
        val sendData = messageStr.toByteArray()
        val sendPacket = DatagramPacket(
            sendData,
            sendData.size,
            InetAddress.getByAddress("192.168.4.1".toByteArray()),
            5051
        )
        socket.send(sendPacket)
    }
*/
}
