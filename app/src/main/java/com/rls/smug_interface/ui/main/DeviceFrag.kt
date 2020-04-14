package com.rls.smug_interface.ui.main

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.rls.smug_interface.R
import kotlinx.android.synthetic.main.fragment_device.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DeviceFrag.newInstance] factory method to
 * create an instance of this fragment.
 */
class DeviceFrag : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val r1 = RelativeLayout(context)
        val txt = TextView(context)
        txt.text = "device 1\nstate: xxx \nlmao"
        r1.addView(txt)
        val imageView = ImageView(context)
        r1.addView(imageView)
        imageView.setImageResource(R.drawable.bulb)
        val leftV = leftv
        val rightV = rightv
        leftV.addView(r1)
        imageView.setOnClickListener {
            println("image was clicked")
            val i = ImageView(context)
            i.setImageResource(R.drawable.go)
            rightV.addView(i)
            val x = ImageView(context)
            x.setImageResource(R.drawable.strip)
            rightV.addView(x)
        }


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_device, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DeviceFrag.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            DeviceFrag().apply {

            }
    }
}
