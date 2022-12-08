package com.example.reachedapp.Views

import android.util.Log
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
import java.util.concurrent.CountDownLatch

class StudentListAdapter: RecyclerView.Adapter<StudentListAdapter.StudentViewHolder>() {

    private var studentList = emptyList<Student>()
    private val database = FirebaseDatabase.getInstance()
    val attendanceRef = database.getReference("Attendance")
    class StudentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        return StudentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_student_row, parent, false))
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val currentStudent = studentList[position]
        val formatter = SimpleDateFormat("dd MMMM yyyy")
        val date = Date()

        holder.itemView.findViewById<TextView>(R.id.student_name).text = currentStudent.studentName

        var checkBox = holder.itemView.findViewById<CheckBox>(R.id.attendance_check)

        attendanceRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(attSnapshot: DataSnapshot) {
                val attStat = attSnapshot.child(formatter.format(date)).child(currentStudent.studentHomeroom.toString())
                        .child(currentStudent.studentName).child("IsPresent").getValue()
                if ( attStat == true || attStat == null) {
                    checkBox.isChecked = true
                }
                else {
                    checkBox.isChecked = false
                }
            }
            override fun onCancelled(attError: DatabaseError) {
                println("The read failed: " + attError.code)
            }
        })

        checkBox.setOnCheckedChangeListener{ _, isChecked ->
            if(checkBox.isChecked)
            {
                attendanceRef.child(formatter.format(date)).child(currentStudent.studentHomeroom.toString()).child(currentStudent.studentName).child("IsPresent").setValue(true)
            } else {
                attendanceRef.child(formatter.format(date)).child(currentStudent.studentHomeroom.toString()).child(currentStudent.studentName).child("IsPresent").setValue(false)
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
}