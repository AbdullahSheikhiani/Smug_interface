package com.rls.smug_interface

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.net.Socket
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        //val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val networks = arrayOf(
            "getting networks list "
        )//= arrayOf("light Green", "clear Blue", "Red", "Blue", "clear Green", "clear Red")


        //val address = "192.168.4.1"
        val port = 5050
        val adapter = ArrayAdapter(
            this, // Context
            android.R.layout.simple_spinner_item, // Layout
            networks // Array
        )

        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

        spinner0.adapter = adapter

        //val file = "ip"
        //val oldIP = "192.168.4.1"
        //var newIP: String = oldIP
        val networkList = ArrayList<String>()
        /*  thread {
              val connection = Socket("192.168.4.1", 5050)
              val writer0: OutputStream = connection.getOutputStream()
              writer0.write("conf".toByteArray())
              val reader: InputStream = connection.getInputStream()

              Log.d("READING", reader.read().toString())

              /*
              val server0 = ServerSocket(5050)
              val client0 = server0.accept()
              val scanner0 = Scanner(client0.inputStream)
              if (scanner0.hasNext()) {
                  var t = scanner0.nextLine()
                  while (t.toString() != "stp") {
                      networkList.add(t.toString())
                      //println(scanner0.next())
                      Log.d("sc", scanner0.next())
                  }
              }*/
              connection.close()
          }*/

        reloadBtn.setOnClickListener {
            var networks = ArrayList<String>()
            var t = thread {
                val connection = Socket(ip, 5050)
                val reader = connection.getInputStream()
                val writer = connection.getOutputStream()
                writer.write("conf".toByteArray())

                var s = reader.bufferedReader()
                var x = s.readLine()
                while (x != "stp") {
                    networks.add(x)
                    x = s.readLine()
                }
                //println("out")
                writer.write("ACK".toByteArray())
                connection.close()
            }
            t.join()
            /*for (i in networks) {
                println(i)
            }*/

            val adapter = ArrayAdapter(
                this, // Context
                android.R.layout.simple_spinner_item, // Layout
                networks // Array
            )
            adapter.notifyDataSetChanged()
            spinner0.adapter = adapter
            //adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

            //spinner0.adapter = adapter


        }
        confBtn.setOnClickListener {
            val intent = Intent(this@MainActivity, Interface::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            applicationContext.startActivity(intent)
            finish()

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
