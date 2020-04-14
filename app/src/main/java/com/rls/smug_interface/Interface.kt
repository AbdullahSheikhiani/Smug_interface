package com.rls.smug_interface

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import com.rls.smug_interface.deviceAndAction.AddDevice
import com.rls.smug_interface.gesture.AddGesture
import com.rls.smug_interface.gesture.ListGestures
import com.rls.smug_interface.gesture.RemoveGesture
import com.rls.smug_interface.user.AddUser
import com.rls.smug_interface.user.ChangeUser
import com.rls.smug_interface.user.RemoveUser
import com.rls.smug_interface.utilities.SliderDialog
import com.rls.smug_interface.utilities.YesNoDialog
import kotlinx.android.synthetic.main.activity_interface.*
import java.net.Inet4Address
import java.net.Socket
import kotlin.concurrent.thread
import kotlin.String as String


class Interface : AppCompatActivity(), YesNoDialog.NoticeDialogListener,
    SliderDialog.OnSaveListener {

    /*fun IP(): String? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        return sharedPreferences.getString("ip", "192.168.4.1")
    }*/

    fun ip(): String {
//        return "192.168.1.126"

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
            println(ex.message)
            "192.168.1.126"
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_interface)

        print("pspspspi =")
        println(ip())


        exitBtn.setOnClickListener {
            //TODO
            val t = thread {
                //println("ip by DNS")
                //val ip = InetAddress.getByName("pspsps")
                //val ip = "192.168.0.186"
                //print("ip =")
                //println(ip)
                // val ip = "192.168.4.1"

                try {
                    val connection = Socket(ip(), 5051)
                    //val reader = connection.getInputStream()
                    val writer = connection.getOutputStream()
                    writer.write("8".toByteArray())
                    connection.close()
                } catch (e: Exception) {
                    /*
                    PreferenceManager.getDefaultSharedPreferences(applicationContext).edit()
                        .clear()
                        .commit()
                        */
                    println()
                    print("e: ")
                    println(e)
                    val connection = Socket("192.168.4.1", 5051)
                    //val reader = connection.getInputStream()
                    val writer = connection.getOutputStream()
                    writer.write("8".toByteArray())
                    connection.close()
                }

            }
            t.join()
            //exitProcess(-2)
            val intent = Intent(this, InitialSetup::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            applicationContext.startActivity(intent)

            //finish()
        }
        lstGstBtn.setOnClickListener {
            //println("XXXXX")
            val t = thread {
                //println("ip by DNS")
                //val ip = InetAddress.getByName("pspsps")
                //val ip = "192.168.0.186"
                //print("ip =")
                //println(ip)
                // val ip = "192.168.4.1"
                /*
                 try {
                     print("load ip value: ")
                     println(IP())
                     println("getting inet address")
                     print("google ip: ")
                     println(InetAddress.getByName("google.com"))
                     val i = InetAddress.getByName("pspspspi")
                     print("ip via inet4 =")
                     println(i)
                 } catch (es: Exception) {
                     println(es)
                     println("this is not working for some reason")
                 }
                 */


                val connection = Socket(ip(), 5051)
                //val reader = connection.getInputStream()
                val writer = connection.getOutputStream()
                writer.write("1".toByteArray())
                connection.close()
            }
            t.join()
            val intent = Intent(this, ListGestures::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            applicationContext.startActivity(intent)
            //finish()
        }
        addGstBtn.setOnClickListener {
            val t = thread {
                //val ip = InetAddress.getByName("pspsps")
                //val ip = "192.168.0.186"
                val connection = Socket(ip(), 5051)
                //val reader = connection.getInputStream()
                val writer = connection.getOutputStream()
                writer.write("2".toByteArray())
                connection.close()
            }
            t.join()
            val intent = Intent(this, AddGesture::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            applicationContext.startActivity(intent)
            //finish()
        }
        rmGstBtn.setOnClickListener {
            val t = thread {
                //val ip = InetAddress.getByName("pspsps")
                // val ip = "192.168.0.186"
                val connection = Socket(ip(), 5051)
                //val reader = connection.getInputStream()
                val writer = connection.getOutputStream()
                writer.write("3".toByteArray())
                connection.close()
            }
            t.join()
            val intent = Intent(this, RemoveGesture::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            applicationContext.startActivity(intent)
        }
        tstGesBtn.setOnClickListener {
            val t = thread {
                //val ip = InetAddress.getByName("pspsps")
                // val ip = "192.168.0.186"
                val connection = Socket(ip(), 5051)
                //val reader = connection.getInputStream()
                val writer = connection.getOutputStream()
                writer.write("4".toByteArray())
                connection.close()
            }
            t.join()

        }

        chgUsrBtn.setOnClickListener {
            val t = thread {
                //val ip = InetAddress.getByName("pspsps")
                //val ip = "192.168.0.186"
                val connection = Socket(ip(), 5051)
                //val reader = connection.getInputStream()
                val writer = connection.getOutputStream()
                writer.write("5".toByteArray())
                connection.close()
            }
            t.join()
            val intent = Intent(this, ChangeUser::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            applicationContext.startActivity(intent)

        }
        addUsrBtn.setOnClickListener {
            val t = thread {
                //val ip = InetAddress.getByName("pspsps")
                //val ip = "192.168.0.186"
                val connection = Socket(ip(), 5051)
                //val reader = connection.getInputStream()
                val writer = connection.getOutputStream()
                writer.write("6".toByteArray())
                connection.close()
            }
            t.join()
            val intent = Intent(this, AddUser::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            applicationContext.startActivity(intent)
        }
        rmUsrBtn.setOnClickListener {
            val t = thread {
                //val ip = InetAddress.getByName("pspsps")
                //val ip = "192.168.0.186"
                val connection = Socket(ip(), 5051)
                //val reader = connection.getInputStream()
                val writer = connection.getOutputStream()
                writer.write("7".toByteArray())
                connection.close()
            }
            t.join()
            val intent = Intent(this, RemoveUser::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            applicationContext.startActivity(intent)
            //finish()
        }
        addDeviceBtn.setOnClickListener {
            val t = thread {
                val connection = Socket(ip(), 5051)
                val writer = connection.getOutputStream()
                writer.write("9".toByteArray())
                println("sent 9")
                connection.close()
            }
            t.join()
            val intent = Intent(this, AddDevice::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            applicationContext.startActivity(intent)
        }
        addActionBtn.setOnClickListener {
            showSlider()
            //showYesNo()
            //val dialog = SliderDialog()
            //dialog.show(supportFragmentManager, "yesno")
            // showYesNo()
            /*
            val t = thread {
                val connection = Socket(ip(), 5051)
                val writer = connection.getOutputStream()
                writer.write("10".toByteArray())
                connection.close()
            }
            t.join()
            val intent = Intent(this, AddAction::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            applicationContext.startActivity(intent)
             */
        }
    }

    private fun showYesNo() {
        val dialog = YesNoDialog()
        dialog.show(supportFragmentManager, "yesno")
    }

    override fun onDialogPositiveClick(answer: String) {
        print("user pressed: ")
        println(answer)
    }

    override fun onDialogNegativeClick(answer: String) {
        print("user pressed: ")
        println(answer)
    }


    private fun showSlider() {
        val dialog = SliderDialog()
        dialog.show(supportFragmentManager, "slider")
    }

    override fun onSave(value: String) {
        print("ON SAVE:")
        print(value)
    }

    override fun onCancel() {
        print("on cancel")
    }

}
