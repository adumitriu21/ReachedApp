package com.example.reachedapp.Views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.reachedapp.Models.Student
import com.example.reachedapp.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*

class StudentListAdapter: RecyclerView.Adapter<StudentListAdapter.StudentViewHolder>() {

    private var studentList = emptyList<Student>()
    private val database = FirebaseDatabase.getInstance()
    private val attendanceRef = database.getReference("Attendance")
    private var absentStudents: MutableList<String> = ArrayList<String>()

    class StudentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        return StudentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_student_row, parent, false))
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val currentStudent = studentList[position]
        val formatter = SimpleDateFormat("dd MMMM yyyy")
        val date = Date()
        holder.itemView.findViewById<TextView>(R.id.student_name).text = currentStudent.name

        val checkBox = holder.itemView.findViewById<CheckBox>(R.id.attendance_check)

        attendanceRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(attSnapshot: DataSnapshot) {
                val attStat = attSnapshot.child(formatter.format(date)).child(currentStudent.classId)
                        .child(currentStudent.name).child("IsPresent").value
                checkBox.isChecked = attStat == true || attStat == null
                if (!checkBox.isChecked) {
                    absentStudents.add(currentStudent.name)
                }
            }
            override fun onCancelled(attError: DatabaseError) {
                println("The read failed: " + attError.code)
            }
        })

        checkBox.setOnCheckedChangeListener{ _, _ ->
            if(checkBox.isChecked)
            {
                attendanceRef.child(formatter.format(date)).child(currentStudent.classId).child(currentStudent.name).child("IsPresent").setValue(true)
                if(absentStudents.contains(currentStudent.name)) {
                    absentStudents.remove(currentStudent.name)
                }
            } else {
                attendanceRef.child(formatter.format(date)).child(currentStudent.classId).child(currentStudent.name).child("IsPresent").setValue(false)
                if(!absentStudents.contains(currentStudent.name)) {
                    absentStudents.add(currentStudent.name)
                }
            }

        }

    }

    override fun getItemCount(): Int {
        return studentList.size
    }

    fun setData(student: List<Student>){
        this.studentList = student
        notifyDataSetChanged()

    }

    fun getSelectedStudents(): List<String?>? {
        return absentStudents
    }

    fun resetSelectedStudents() {
        absentStudents.clear()
    }
}