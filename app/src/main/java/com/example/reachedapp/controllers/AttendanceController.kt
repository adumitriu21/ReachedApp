package com.example.reachedapp.controllers

import android.widget.CheckBox
import com.example.reachedapp.models.Student
import com.example.reachedapp.repositories.AttendanceRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*

class AttendanceController() {

    private val attendanceRepository = AttendanceRepository()

    fun fetchAttendanceData(currentStudent: Student, isParentView: Boolean, callback: (Boolean) -> Unit) {
        attendanceRepository.fetchAttendanceData(currentStudent, isParentView, callback)
    }

    fun updateAttendance(currentStudent: Student, isPresent: Boolean, isParentView: Boolean, teacherNotified: Boolean) {
        attendanceRepository.updateAttendance(currentStudent, isPresent, isParentView, teacherNotified)
    }
}
