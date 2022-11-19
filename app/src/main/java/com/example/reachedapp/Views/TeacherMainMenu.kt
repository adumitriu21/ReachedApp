package com.example.reachedapp.Views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.reachedapp.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*


class TeacherMainMenu : Fragment() {

    private val database = FirebaseDatabase.getInstance()

    val attendanceRef = database.getReference("Attendance")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_teacher_main_menu, container, false)

        val attendanceBtn = view.findViewById<Button>(R.id.take_attendance_btn)

        val formatter = SimpleDateFormat("dd MMMM yyyy")
        val attendanceDate = Date()

        attendanceBtn.setOnClickListener{

            findNavController().navigate(R.id.action_teacherMainMenu_to_teacherAttendanceView)
        }

        return view
    }



}