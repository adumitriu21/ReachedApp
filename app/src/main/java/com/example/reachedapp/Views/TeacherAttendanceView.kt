package com.example.reachedapp.Views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
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
import kotlin.collections.ArrayList


class TeacherAttendanceView : Fragment() {

    private val database = FirebaseDatabase.getInstance()
    val ref = database.getReference("Student")
    val attendanceRef = database.getReference("Attendance")
    private lateinit var dateTimeDisplay: TextView
    private lateinit var dayOfWeek: TextView
    private lateinit var calendar: Calendar
    private lateinit var dateFormat: SimpleDateFormat
    private lateinit var date: String

    val homeroom = arrayOf("107", "108")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_teacher_attendance_view, container, false)

        //Creating the instance of ArrayAdapter containing list of fruit names
        val adapter = ArrayAdapter(requireActivity(), R.layout.drpdown_item, homeroom)

        //Getting the instance of AutoCompleteTextView
        val homeroomSelect = view.findViewById<AutoCompleteTextView>(R.id.homeroomSelect)
        homeroomSelect.threshold = 1 //will start working from first character
        homeroomSelect.setAdapter(adapter) //setting the adapter data into the AutoCompleteTextView

        //display date
        dateTimeDisplay = view.findViewById(R.id.date)
        calendar = Calendar.getInstance()
        dateFormat = SimpleDateFormat("MM/dd/yyyy")
        date = dateFormat.format(calendar.getTime())
        dateTimeDisplay.setText(date)

        //display day of the week
        dayOfWeek = view.findViewById(R.id.day)
        dayOfWeek.setText(LocalDate.now().dayOfWeek.name)

        val studentAdapter = StudentListAdapter()
        val studentRecyclerView = view.findViewById<RecyclerView>(R.id.studentsList)
        studentRecyclerView.adapter = studentAdapter
        studentRecyclerView.layoutManager = LinearLayoutManager(requireContext())


        val formatter = SimpleDateFormat("dd MMMM yyyy")
        val attendanceDate = Date()

        homeroomSelect.onItemClickListener = OnItemClickListener { parent, _, position, _ ->

            val selectedHomeroom = parent.getItemAtPosition(position)
            val studentList: MutableList<Student> = ArrayList<Student>()

            ref.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    for (dsp in dataSnapshot.children) {
                        val s = dsp.getValue(Student::class.java)
                        if (s != null && s.studentHomeroom.toString() == selectedHomeroom.toString()) {

                            studentList.add(s)

                            attendanceRef.child(formatter.format(attendanceDate))
                                .child(s.studentHomeroom.toString())
                                .child(s.studentName)
                                .child("IsPresent")
                                .setValue(true)
                        }
                        studentAdapter.setData(studentList)
                    }
                }


                override fun onCancelled(databaseError: DatabaseError) {
                    println("The read failed: " + databaseError.code)
                }
            })
        }



        studentRecyclerView.setOnClickListener {  }
        return view
    }

}