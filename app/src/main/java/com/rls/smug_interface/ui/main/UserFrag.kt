package com.rls.smug_interface.ui.main

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.Paint.Align
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.rls.smug_interface.Interface
import com.rls.smug_interface.R
import kotlinx.android.synthetic.main.fragment_user.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserFrag.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserFrag : Fragment() {
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val a = ArrayList<String>()
        a.add("Add user")
        a.add("remove user")

        val vadapter = ArrayAdapter(
            activity, // Context
            android.R.layout.simple_expandable_list_item_1, // Layout
            a // Array
        )
        lView.adapter = vadapter
        lView.setOnItemClickListener { _, _, position, _ ->
            println(a[position])

            if (a[position].contains("Add")) {
                println("IF")
                val intent = Intent(context, Interface::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context!!.startActivity(intent)
            } else if (a[position].contains("remove")) {
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false)

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserFrag.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            UserFrag().apply {

            }
    }
    
}
