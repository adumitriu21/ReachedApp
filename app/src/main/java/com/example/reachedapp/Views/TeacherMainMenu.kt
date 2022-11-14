package com.example.reachedapp.Views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.reachedapp.R


class TeacherMainMenu : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_teacher_main_menu, container, false)

        val attendanceBtn = view.findViewById<Button>(R.id.take_attendance_btn)

        attendanceBtn.setOnClickListener{
            findNavController().navigate(R.id.action_teacherMainMenu_to_teacherAttendanceView)
        }

        return view
    }



}