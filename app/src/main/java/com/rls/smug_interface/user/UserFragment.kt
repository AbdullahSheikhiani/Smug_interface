package com.rls.smug_interface.user

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rls.smug_interface.R
import kotlinx.android.synthetic.main.activity_add_user.view.*
import kotlinx.android.synthetic.main.fragment_list.view.*
import kotlinx.android.synthetic.main.fragment_main_ui.view.*
import java.util.*


class UserFragment : Fragment() {
    private lateinit var viewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)


        val root = inflater.inflate(R.layout.fragment_main_ui, container, false)

        val layout = root.deviceLayout

        val listBtn = root.imageViewList
        val removeBtn = root.imageViewDel
        val addBtn = root.imageViewAdd

        val fList = listBtn.colorFilter
        val fRemove = removeBtn.colorFilter
        val fAdd = addBtn.colorFilter
        //todo loading until model is loaded and ack the phone
        listBtn.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    addBtn.colorFilter = fAdd
                    removeBtn.colorFilter = fRemove
                }
                MotionEvent.ACTION_DOWN -> {
                    listBtn.setColorFilter(Color.rgb(0, 0, 0))
                    //val mInflater = requireActivity().layoutInflater
                    //val view = mInflater.inflate(R.layout.fragment_list, null)
                    val view = View.inflate(context, R.layout.fragment_list, null)
                    val list = view.findViewById<ListView>(R.id.listView)
                    //getUserList()
                    viewModel.getUserList()
                    viewModel.userList.observe(viewLifecycleOwner, Observer {
                        val adapter = ArrayAdapter(
                            context!!, // Context
                            android.R.layout.simple_expandable_list_item_1, // Layout
                            it // Array
                        )
                        list.adapter = adapter
                    })
                    layout.removeAllViews()
                    layout.addView(view)
                    list.setOnItemClickListener { parent, _, position, _ ->
                        //changeUser()
                        viewModel.changeUser(parent.adapter.getItem(position).toString())

                        layout.removeAllViewsInLayout()
                        val txt = TextView(context)
                        txt.text =
                            "changed to user ${parent.adapter.getItem(position)} successfully"
                        layout.addView(txt)
                    }
                }
            }
            true
        }
        removeBtn.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    addBtn.colorFilter = fAdd
                    listBtn.colorFilter = fList
                }
                MotionEvent.ACTION_DOWN -> {
                    removeBtn.setColorFilter(Color.rgb(0, 0, 0))
                    //val mInflater = requireActivity().layoutInflater
                    //val view = mInflater.inflate(R.layout.fragment_list, null)
                    val view = View.inflate(context, R.layout.fragment_list, null)
                    val list = view.listView

                    viewModel.getUserList()
                    viewModel.userList.observe(viewLifecycleOwner, Observer {
                        val adapter = ArrayAdapter(
                            context!!, // Context
                            android.R.layout.simple_expandable_list_item_1, // Layout
                            it // Array
                        )
                        list.adapter = adapter
                    })
                    layout.removeAllViews()
                    layout.addView(view)
                    list.setOnItemClickListener { parent, _, position, _ ->
                        //removeUser()
                        viewModel.removeUser(parent.adapter.getItem(position).toString(), context)
                        layout.removeAllViews()
                        val txt = TextView(context)
                        txt.text = "remove user ${parent.adapter.getItem(position)} successfully"
                        layout.addView(txt)
                    }
                }
            }
            true
        }
        addBtn.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    listBtn.colorFilter = fList
                    removeBtn.colorFilter = fRemove
                }
                MotionEvent.ACTION_DOWN -> {
                    addBtn.setColorFilter(Color.rgb(0, 0, 0))
                    //val mInflater = requireActivity().layoutInflater
                   // val view = mInflater.inflate(R.layout.activity_add_user, null)
                    val view = View.inflate(context, R.layout.activity_add_user, null)

                    val user = view.addUserEditText
                    val btn = view.btnSendUser
                    layout.removeAllViewsInLayout()
                    layout.addView(view)
                    btn.setOnClickListener {
                        viewModel.addUser(user.text.toString())

                        viewModel.getFingerPrintConfirmation()
                        layout.removeAllViews()
                        val txt = TextView(context)
                        txt.textSize = 18F
                        txt.text =
                            "Please touch the touch sensor once then put your finger on the fingerprint scanner"
                        layout.addView(txt)
                        viewModel.fingerPrintConfirmation.observe(viewLifecycleOwner, Observer {
                            if (it.toLowerCase(Locale.ROOT).contains("ack")) {
                                txt.text = "UserAdded successfully"
                            }
                        })
                    }
                }
            }
            true
        }
        return root
    }
}