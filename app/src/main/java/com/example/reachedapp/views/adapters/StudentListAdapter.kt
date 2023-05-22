package com.example.reachedapp.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.reachedapp.models.Student
import com.example.reachedapp.R
import com.example.reachedapp.controllers.AttendanceController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*

class StudentListAdapter(private val isParentView: Boolean = false): RecyclerView.Adapter<StudentListAdapter.StudentViewHolder>() {

    private var studentList = emptyList<Student>()
    private val database = FirebaseDatabase.getInstance()
    private val attendanceRef = database.getReference("Attendance")
    private var absentStudents: MutableList<String> = ArrayList<String>()
    // private lateinit var currentStudent: Student
    class StudentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        return StudentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_student_row, parent, false))
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val currentStudent = studentList[position]
        val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.CANADA)
        val date = Date()
        holder.itemView.findViewById<TextView>(R.id.student_name).text = currentStudent.name

        val checkBox = holder.itemView.findViewById<CheckBox>(R.id.attendance_check)

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

        val attendanceController = AttendanceController(attendanceRef)
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            attendanceController.updateAttendance(currentStudent, isChecked, isParentView, teacherNotified = false, absentStudents)
        }

    }

    override fun getItemCount(): Int {
        return studentList.size
    }

    fun setData(student: List<Student>){
        this.studentList = student
        notifyDataSetChanged()

    }

    fun getSelectedStudents(): List<String?> {
        return absentStudents
    }

}