package com.example.reachedapp.Views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.reachedapp.R

class ParentMainMenu : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_parent_main_menu, container, false)
        val markAttendanceBtn = view.findViewById<Button>(R.id.mark_attendance_btn)
        markAttendanceBtn.setOnClickListener{
            findNavController().navigate(R.id.action_parentMainMenu_to_parentAttendanceView)
        }

        return view
    }

}