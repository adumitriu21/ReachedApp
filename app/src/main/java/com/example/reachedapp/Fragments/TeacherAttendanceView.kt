package com.example.reachedapp.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reachedapp.Models.Student
import com.example.reachedapp.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class TeacherAttendanceView : Fragment() {

    private val database = FirebaseDatabase.getInstance()
    val ref = database.getReference("Student")
    val teacherRef = database.getReference("Teacher")

    val homeroom = arrayOf("107", "108")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        val fruits = arrayOf("Apple", "Banana", "Cherry", "Date", "Grape", "Kiwi", "Mango", "Pear")
        val view = inflater.inflate(R.layout.fragment_teacher_attendance_view, container, false)

        //Creating the instance of ArrayAdapter containing list of fruit names
        val adapter = ArrayAdapter(requireActivity(), R.layout.drpdown_item, homeroom)

        //Getting the instance of AutoCompleteTextView
        val grades = view.findViewById<AutoCompleteTextView>(R.id.gradesAutoComplete)

        grades.threshold = 1 //will start working from first character

        grades.setAdapter(adapter) //setting the adapter data into the AutoCompleteTextView

        val mRecyclerView = view.findViewById<RecyclerView>(R.id.daySelector)
        mRecyclerView.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, true)

        val studentAdapter = StudentListAdapter()
        val studentRecyclerView = view.findViewById<RecyclerView>(R.id.studentsList)
        studentRecyclerView.adapter = studentAdapter
        studentRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        grades.setOnItemClickListener(OnItemClickListener { parent, arg1, position, arg3 ->
            val item = parent.getItemAtPosition(position)
            val studentList: MutableList<Student> = ArrayList<Student>()
            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    for (dsp in dataSnapshot.children) {
                        val s = dsp.getValue(Student::class.java)
                        if (s != null && s.studentHomeroom.toString() == item.toString()) {
                            studentList.add(s)
                        }
                        studentAdapter.setData(studentList)
                    }
                }


                override fun onCancelled(databaseError: DatabaseError) {
                    println("The read failed: " + databaseError.code)
                }
            })
        })



        studentRecyclerView.setOnClickListener {  }
        return view
    }

}