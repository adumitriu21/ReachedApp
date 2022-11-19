package com.example.reachedapp.Views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
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

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ParentAttendanceView.newInstance] factory method to
 * create an instance of this fragment.
 */
class ParentAttendanceView : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val database = FirebaseDatabase.getInstance()
    val ref = database.getReference("Student")
    val attendanceRef = database.getReference("Attendance")
    private lateinit var dateTimeDisplay: TextView
    private lateinit var dayOfWeek: TextView
    private lateinit var calendar: Calendar
    private lateinit var dateFormat: SimpleDateFormat
    private lateinit var date: String

    val parentList: MutableList<String> = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        */
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (dsp in dataSnapshot.children) {
                    val s = dsp.getValue(Student::class.java)
                    if (s != null) {
                        parentList.add(s.studentParent1)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_parent_attendance_view, container, false)

        val adapter = ArrayAdapter(requireActivity(), R.layout.drpdown_item, parentList)

        val allParentsList = view.findViewById<AutoCompleteTextView>(R.id.parentSelection)

        allParentsList.threshold = 1 //will start working from first character
        allParentsList.setAdapter(adapter) //setting the adapter data into the AutoCompleteTextView
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
        val studentRecyclerView = view.findViewById<RecyclerView>(R.id.pStudentsList)
        studentRecyclerView.adapter = studentAdapter
        studentRecyclerView.layoutManager = LinearLayoutManager(requireContext())


        val formatter = SimpleDateFormat("dd MMMM yyyy")
        val attendanceDate = Date()

        allParentsList.setOnItemClickListener(AdapterView.OnItemClickListener { parent, arg1, position, arg3 ->
            val item = parent.getItemAtPosition(position)
            val studentList: MutableList<Student> = ArrayList<Student>()

            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    for (dsp in dataSnapshot.children) {
                        val s = dsp.getValue(Student::class.java)
                        if (s != null && s.studentParent1.toString() == item.toString()) {
                            studentList.add(s)
                            attendanceRef.child(formatter.format(attendanceDate)).child(s.studentHomeroom.toString()).child(s.studentName).child("IsPresent").setValue(true)
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ParentAttendanceView.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                ParentAttendanceView().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}