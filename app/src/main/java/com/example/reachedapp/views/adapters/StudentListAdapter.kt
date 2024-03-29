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
    private val attendanceController = AttendanceController()
    private var absentStudents: MutableList<String> = ArrayList<String>()

    class StudentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        return StudentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_student_row, parent, false))
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val currentStudent = studentList[position]
        holder.itemView.findViewById<TextView>(R.id.student_name).text = currentStudent.name

        val checkBox = holder.itemView.findViewById<CheckBox>(R.id.attendance_check)

        attendanceController.fetchAttendanceData(currentStudent, isParentView) { isPresent ->
            checkBox.isChecked = isPresent
            if (!isPresent) absentStudents.add(currentStudent.studentId)
        }

        checkBox.setOnCheckedChangeListener { _, isChecked ->
            attendanceController.updateAttendance(currentStudent, isChecked, isParentView, teacherNotified = false)
            if (isChecked) absentStudents.remove(currentStudent.studentId) else absentStudents.add(currentStudent.studentId)
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