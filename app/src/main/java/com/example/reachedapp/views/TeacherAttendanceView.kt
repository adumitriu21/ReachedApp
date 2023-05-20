package com.example.reachedapp.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.reachedapp.R
import com.example.reachedapp.models.Teacher
import com.example.reachedapp.controllers.TeacherAttendanceController

class TeacherAttendanceView : Fragment() {
    private lateinit var controller: TeacherAttendanceController
    lateinit var dateTimeDisplay: TextView
    lateinit var dayOfWeek: TextView
    lateinit var searchBar: EditText
    lateinit var studentRecyclerView: RecyclerView
    lateinit var submitBtn: Button
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_teacher_attendance_view, container, false)

        // Initialize the views
        dateTimeDisplay = view.findViewById(R.id.date)
        dayOfWeek = view.findViewById(R.id.day)
        searchBar = view.findViewById(R.id.searchByName)
        studentRecyclerView = view.findViewById(R.id.studentsList)
        submitBtn = view.findViewById(R.id.submitAttendance)

        // Retrieve the teacher object passed as an argument
        val teacher = arguments?.getParcelable<Teacher>("teacher")

        // Create an instance of TeacherAttendanceController and initialize it
        controller = TeacherAttendanceController(requireContext(), findNavController(), teacher, this)
        controller.initialize()

        return view
    }
}
