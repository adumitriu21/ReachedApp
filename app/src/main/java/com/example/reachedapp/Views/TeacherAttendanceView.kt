package com.example.reachedapp.Views

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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
        date = dateFormat.format(calendar.time)
        dateTimeDisplay.text = date

        //display day of the week
        dayOfWeek = view.findViewById(R.id.day)
        dayOfWeek.text = LocalDate.now().dayOfWeek.name

        val studentAdapter = StudentListAdapter()
        val studentRecyclerView = view.findViewById<RecyclerView>(R.id.studentsList)
        studentRecyclerView.adapter = studentAdapter
        studentRecyclerView.layoutManager = LinearLayoutManager(requireContext())


        val formatter = SimpleDateFormat("dd MMMM yyyy")
        val attendanceDate = Date()

        /*
        * the Homeroom select drop down menu is a feature that will only exist
        * until Authentication is implemented, at which point when a teacher logs
        * into the application, it will automatically pull his/her homeroom student
        * list.
        *
        * For now, as soon as a homeroom is selected, a recycler view with the students
        * in the respective class is populated. At the same time a new Firebase Attendance
        * entry is created with all the Student's attendance status defaulted to present
        * */
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
                        attendanceRef.child(formatter.format(attendanceDate))
                            .child("IsSubmitted").setValue(false)
                        studentAdapter.setData(studentList)
                    }
                }


                override fun onCancelled(databaseError: DatabaseError) {
                    println("The read failed: " + databaseError.code)
                }
            })
        }

        val submitBtn = view.findViewById<Button>(R.id.submitAttendance)

        /*
        * Logic for clicking the Attendance submit button
        * Each attendance entry (Data) in Firebase has an IsSubmitted
        * property that defaults to false. When this button is clicked
        * and confirmed, the boolean value is changed to true, disabling
        * the Teacher from accessing the Attendance screen for the remainder
        * of the current day
        * */
        submitBtn.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle(R.string.dialogTitle)
            //set message for alert dialog
            builder.setMessage(R.string.dialogMessage)
            builder.setIcon(android.R.drawable.ic_dialog_alert)

            //performing positive action
            builder.setPositiveButton("Yes"){dialogInterface, which ->

                ref.addValueEventListener(object : ValueEventListener {

                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                            attendanceRef.child(formatter.format(attendanceDate))
                                .child("IsSubmitted")
                                .setValue(true)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        println("The read failed: " + databaseError.code)
                    }
                })


                Toast.makeText(requireContext(),"Attendance Submitted",Toast.LENGTH_LONG).show()
                findNavController().navigate(R.id.action_teacherAttendanceView_to_teacherMainMenu)
            }
            //performing negative action
            builder.setNegativeButton("No"){dialogInterface, which ->
                Toast.makeText(requireContext(),"Cancelled Submit",Toast.LENGTH_LONG).show()
            }
            // Create the AlertDialog
            val alertDialog: AlertDialog = builder.create()
            // Set other dialog properties
            alertDialog.setCancelable(false)
            alertDialog.show()
        }




        studentRecyclerView.setOnClickListener {  }
        return view
    }

}