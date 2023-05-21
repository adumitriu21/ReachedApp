package com.example.reachedapp.controllers

import android.util.Log
import com.example.reachedapp.models.Student
import com.google.firebase.database.DatabaseReference
import java.text.SimpleDateFormat
import java.util.*

class AttendanceController(private val attendanceRef: DatabaseReference) {



    fun updateAttendance(
        currentStudent: Student,
        isPresent: Boolean,
        isParentView: Boolean,
        teacherNotified: Boolean = false,
        absentStudents: MutableList<String>
    ) {
        val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.CANADA)
        val date = Date()
        val attendancePath = if (isParentView) {
            attendanceRef.child(formatter.format(date))
                .child(currentStudent.classId)
                .child("Reported Absences")
                .child(currentStudent.studentId)
                .child("IsPresent")
        } else {
            attendanceRef.child(formatter.format(date))
                .child(currentStudent.classId)
                .child(currentStudent.studentId)
                .child("IsPresent")
        }


        attendancePath.setValue(isPresent).addOnSuccessListener {
            if (isPresent) {
                absentStudents.remove(currentStudent.studentId)
            } else {
                if (!absentStudents.contains(currentStudent.studentId)) {
                    absentStudents.add(currentStudent.studentId)
                }
            }
        }.addOnFailureListener {
            // Handle failure
        }
    }
}
