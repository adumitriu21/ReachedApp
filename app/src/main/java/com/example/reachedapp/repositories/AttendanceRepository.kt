package com.example.reachedapp.repositories
import com.example.reachedapp.interfaces.AttendanceStatusListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*

class AttendanceRepository {
    private val database = FirebaseDatabase.getInstance()
    private val attendanceRef = database.getReference("Attendance")
    private val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.CANADA)
    private var currentDate = formatter.format(Date())
    private lateinit var attendanceStatusListener: AttendanceStatusListener

    fun onAttendanceSubmitted( classId: String): Task<Void> {
        var attendance = attendanceRef.child(currentDate)
            .child(classId)
            .child("IsSubmitted")

        return attendance.setValue(true)
    }

    fun isAttendanceSubmitted(classId: String, callback: (Boolean) -> Unit) {

        val attendanceRef = database.getReference("Attendance/$currentDate/$classId/IsSubmitted")
        attendanceRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val isSubmitted = snapshot.exists() && snapshot.getValue(Boolean::class.java) == true
                callback(isSubmitted)
            }
            override fun onCancelled(error: DatabaseError) {
                // Handle error
                callback(false)
            }
        })
    }

    fun setAttendanceStatusListener(listener: AttendanceStatusListener) {
        attendanceStatusListener = listener
    }
}