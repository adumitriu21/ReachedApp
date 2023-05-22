package com.example.reachedapp.controllers

import com.example.reachedapp.models.Student
import com.google.firebase.database.DatabaseReference
import java.text.SimpleDateFormat
import java.util.*

class AttendanceController(private val attendanceRef: DatabaseReference) {

    fun updateAttendance(
        student: Student,
        isPresent: Boolean,
        isParentView: Boolean,
        teacherNotified: Boolean = false,
        absentStudents: MutableList<String>
    ) {
        val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.CANADA)
        val date = Date()
        val attendancePath = if (isParentView) {
            attendanceRef.child(formatter.format(date))
                .child(student.classId)
                .child("Reported Absences")
                .child(student.studentId)
                .child("IsPresent")
        } else {
            attendanceRef.child(formatter.format(date))
                .child(student.classId)
                .child(student.studentId)
                .child("IsPresent")
        }


        attendancePath.setValue(isPresent).addOnSuccessListener {
            if (isPresent) {
                absentStudents.remove(student.studentId)
            } else {
                if (!absentStudents.contains(student.studentId)) {
                    absentStudents.add(student.studentId)
                }
            }
        }.addOnFailureListener {
            // Handle failure
        }
    }
}
