package com.example.reachedapp.views

import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reachedapp.Models.Parent
import com.example.reachedapp.Models.Student
import com.example.reachedapp.R
import com.example.reachedapp.sendNotification
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*


class ParentAttendanceView : Fragment() {

    private lateinit var notificationManager: NotificationManager
    private val database = FirebaseDatabase.getInstance()
    private val ref = database.getReference("Student")
    val attendanceRef = database.getReference("Attendance")
    lateinit var attendanceDate: Date
    private lateinit var dateTV: TextView
    private lateinit var calendarView: CalendarView
    private var studentList: MutableList<Student> = ArrayList<Student>()
    private var studentAdapter = StudentListAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notificationManager = ContextCompat.getSystemService(requireContext(),
                NotificationManager::class.java) as NotificationManager
    }

    private fun createChannel(channelId : String){
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(
                    channelId,
                    "123",
                    NotificationManager.IMPORTANCE_HIGH
            ).apply {
                enableLights(true)
                enableVibration(true)
                lightColor = Color.GREEN
            }
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_parent_attendance_view, container, false)

        // get parent
        @Suppress("DEPRECATION")
        val parent = arguments?.getParcelable<Parent>("parent")
        val parentId = parent?.userId

        // initializing variables for registering the selected date
        dateTV = view.findViewById(R.id.idTVDate)
        calendarView = view.findViewById(R.id.calendarView)

        // on below line we are adding set on
        // date change listener for calendar view.
        val format = SimpleDateFormat("dd-MM-yyyy", Locale.US)
        val currentDate  = Date()

        //default attendance date to today's date
        attendanceDate = currentDate
        calendarView.minDate = currentDate.time

        //display today's date in the top left text box
        dateTV.text = format.format(currentDate.time)
        calendarView
            .setOnDateChangeListener { _: CalendarView, year, month, dayOfMonth ->
                // In this Listener we are getting values
                // such as year, month and day of month
                // on below line we are creating a variable
                // in which we are adding all the variables in it.
                val date = (dayOfMonth.toString() + "-"
                        + (month + 1) + "-" + year)

                // set this date in TextView for Display
                dateTV.text = date
                // convert string representing the date to an actual Date object matching
                // the other entries in the DB
                val inputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.US)
                val parsedDate = inputFormat.parse(dateTV.text as String)
                attendanceDate = parsedDate ?: currentDate
            }


        val studentRecyclerView = view.findViewById<RecyclerView>(R.id.pStudentsList)

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dsp in dataSnapshot.children) {
                    val s = dsp.getValue(Student::class.java)
                    if (s != null && s.parentId == parentId) {
                        studentList.add(s)
                    }
                    studentAdapter.setData(studentList)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })

        studentRecyclerView.adapter = studentAdapter
        studentRecyclerView.layoutManager = LinearLayoutManager(requireContext())


        val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.US)
        studentRecyclerView.setOnClickListener {  }


        val submitBtn = view.findViewById<Button>(R.id.submitReport)
        submitBtn.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle(R.string.dialogTitle)
            //set message for alert dialog
            builder.setMessage(R.string.reportAbsenceMsg)
            builder.setIcon(android.R.drawable.ic_dialog_alert)

            //performing positive action
            builder.setPositiveButton("Yes"){_, _ ->

                val selectedStud = studentAdapter.getSelectedStudents()
                //if (selectedStud != null) {
                    for (std in selectedStud) {
                        ref.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {

                                for (dsp in dataSnapshot.children) {
                                    val s = dsp.getValue(Student::class.java)
                                    if (s != null && s.name == std) {
                                        attendanceRef.child(formatter.format(attendanceDate))
                                                .child("Reported Absences")
                                                .child(s.classId)
                                                .child(s.name)
                                                .child("IsPresent")
                                                .setValue(false)
                                    }
                                }
                            }


                            override fun onCancelled(databaseError: DatabaseError) {
                                println("The read failed: " + databaseError.code)
                            }
                        })
                    }
                //}


                createChannel(getString(R.string.comment_notification_channel_id))

                val title = "REACHED"
                val message = "Absence report has been transmitted successfully to school secretary and teachers."

                notificationManager.sendNotification(
                        title,
                        message,
                        getString(R.string.comment_notification_channel_id),
                        requireContext()
                )

                Toast.makeText(requireContext(),"Absence Reported Successfully!",Toast.LENGTH_LONG).show()
                val bundle = bundleOf("parent" to parent)
                findNavController().navigate(R.id.action_parentAttendanceView_to_parentMainMenu, bundle)
            }
            //performing negative action
            builder.setNegativeButton("No"){_, _ ->
                Toast.makeText(requireContext(),"Cancelled Submit",Toast.LENGTH_LONG).show()
            }
            // Create the AlertDialog
            val alertDialog: AlertDialog = builder.create()
            // Set other dialog properties
            alertDialog.setCancelable(false)
            alertDialog.show()
        }

        return view
    }


}