package com.example.reachedapp.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.reachedapp.controllers.ParentAttendanceController
import com.example.reachedapp.models.Parent
import com.example.reachedapp.R

class ParentAttendanceView : Fragment() {

    private lateinit var parentAttendanceController: ParentAttendanceController
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_parent_attendance_view, container, false)

        // Initialize the views
        val dateTV = view.findViewById<TextView>(R.id.idTVDate)
        val calendarView = view.findViewById<CalendarView>(R.id.calendarView)
        val studentRecyclerView = view.findViewById<RecyclerView>(R.id.pStudentsList)
        val submitBtn = view.findViewById<Button>(R.id.submitReport)

        // Retrieve the parent object passed as an argument
        val parent = arguments?.getParcelable<Parent>("parent")

        // Create an instance of ParentAttendanceController and set up the views
        parentAttendanceController = ParentAttendanceController(requireContext(), findNavController(), "Notification")
        parentAttendanceController.setupViews(parent, dateTV, calendarView, studentRecyclerView, submitBtn)

        return view
    }
}
