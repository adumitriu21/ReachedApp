package com.example.reachedapp.Fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.reachedapp.Models.Student
import com.example.reachedapp.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class StudentListAdapter: RecyclerView.Adapter<StudentListAdapter.StudentViewHolder>() {

    private var studentList = emptyList<Student>()
    private val database = FirebaseDatabase.getInstance()
    val attendanceRef = database.getReference("Attendance")
    class StudentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        return StudentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_students_list, parent, false))
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val currentStudent = studentList[position]
        val formatter = SimpleDateFormat("dd MMMM yyyy")
        val date = Date()

        holder.itemView.findViewById<TextView>(R.id.studentName).text = currentStudent.studentName
        var checkBox = holder.itemView.findViewById<RadioGroup>(R.id.radioGroup)

        attendanceRef.child(formatter.format(date)).child(currentStudent.studentHomeroom.toString()).child(currentStudent.studentName).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dsp in dataSnapshot.children) {
                    val s = dsp.getValue(Boolean::class.java)
                    if (s != null && s == true) {
                        checkBox.check(R.id.present)
                    }
                    else {
                        checkBox.check(R.id.absent)
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })

        checkBox.setOnCheckedChangeListener{ _, isChecked ->
            if(isChecked == R.id.present)
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