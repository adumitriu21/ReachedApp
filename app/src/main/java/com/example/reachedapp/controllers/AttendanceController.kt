package com.example.reachedapp.controllers

import android.widget.CheckBox
import com.example.reachedapp.models.Student
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*

class AttendanceController(private val attendanceRef: DatabaseReference) {

    fun fetchAttendanceData(
        currentStudent: Student,
        isParentView: Boolean,
        date: Date,
        formatter: SimpleDateFormat,
        checkBox: CheckBox,
        absentStudents: MutableList<String>
    ) {
        attendanceRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(attSnapshot: DataSnapshot) {
                val attStat = if (isParentView) {
                    attSnapshot.child(currentStudent.classId)
                        .child("Reported Absences")
                        .child(currentStudent.studentId)
                        .child("IsPresent").value
                    attSnapshot.child(currentStudent.classId)
                        .child("Reported Absences")
                        .child(currentStudent.studentId)
                        .child("teacherNotified").value
                } else {
                    attSnapshot.child(formatter.format(date)).child(currentStudent.classId)
                        .child(currentStudent.studentId).child("IsPresent").value
                }
                checkBox.isChecked = attStat == true || attStat == null
                if (!checkBox.isChecked) {
                    absentStudents.add(currentStudent.studentId)
                }
            }

            override fun onCancelled(attError: DatabaseError) {
                println("The read failed: " + attError.code)
            }
        })
    }
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
