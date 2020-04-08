package com.rls.smug_interface.utilities

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ramotion.fluidslider.FluidSlider

import com.rls.smug_interface.R
import kotlinx.android.synthetic.main.fragment_scrollbar.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Scrollbar.newInstance] factory method to
 * create an instance of this fragment.
 */
class SliderFrag : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val slider = fluidSlider
        val max = 255
        val min = 0
        val total = max - min
        slider.positionListener = { pos -> slider.bubbleText = "${min + (total * pos).toInt()}" }
        slider.position = 1F
        slider.startText = "$min"
        slider.endText = "$max"
        //slider.positionListener = { p -> print(p) }

        slider.endTrackingListener = { print("done") }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scrollbar, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Scrollbar.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            SliderFrag().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
