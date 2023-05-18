package com.example.reachedapp.controllers

import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reachedapp.models.Parent
import com.example.reachedapp.models.Student
import com.example.reachedapp.R
import com.example.reachedapp.sendNotification
import com.example.reachedapp.views.adapters.StudentListAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*

class ParentAttendanceController(
        private val context: Context,
        private val navController: NavController,
        private val notificationTitle: String
) {

    private val notificationManager =
            ContextCompat.getSystemService(context, NotificationManager::class.java) as NotificationManager
    private val database = FirebaseDatabase.getInstance()
    private val ref = database.getReference("Student")
    private val attendanceRef = database.getReference("Attendance")
    private lateinit var attendanceDate: Date
    private lateinit var studentAdapter: StudentListAdapter

    // Create a notification channel for displaying notifications
    fun createChannel(channelId: String) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
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

    // Set up the views and event listeners for the parent attendance screen
    fun setupViews(
            parent: Parent?,
            dateTV: TextView,
            calendarView: CalendarView,
            studentRecyclerView: RecyclerView,
            submitBtn: Button
    ) {
        val format = SimpleDateFormat("dd-MM-yyyy", Locale.US)
        val currentDate = Date()
        attendanceDate = currentDate
        calendarView.minDate = currentDate.time
        dateTV.text = format.format(currentDate.time)

        // Handle date selection in the calendar view
        calendarView.setOnDateChangeListener { _: CalendarView, year, month, dayOfMonth ->
            val date = (dayOfMonth.toString() + "-" + (month + 1) + "-" + year)
            dateTV.text = date
            val inputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.US)
            val parsedDate = inputFormat.parse(dateTV.text as String)
            attendanceDate = parsedDate ?: currentDate
        }

        studentAdapter = StudentListAdapter(isParentView = true)
        studentRecyclerView.adapter = studentAdapter
        studentRecyclerView.layoutManager = LinearLayoutManager(context)

        // Fetch the list of students from the database and populate the RecyclerView
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val studentList: MutableList<Student> = ArrayList()
                for (dsp in dataSnapshot.children) {
                    val s = dsp.getValue(Student::class.java)
                    if (s != null && s.parentId == parent?.userId) {
                        studentList.add(s)
                    }
                    studentAdapter.setData(studentList)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })

        // Handle the submit button click event to report absence
        submitBtn.setOnClickListener {
                // Create an AlertDialog.Builder instance
                val builder = AlertDialog.Builder(context)
                builder.setTitle(R.string.dialogTitle)
                builder.setMessage(R.string.reportAbsenceMsg)
                builder.setIcon(android.R.drawable.ic_dialog_alert)

                // Set the positive button and its click listener
                builder.setPositiveButton("Yes") { _, _ ->
                    // Get the selected students from the student adapter
                    val selectedStud = studentAdapter.getSelectedStudents()

                    // Iterate over the selected students
                    for (std in selectedStud) {
                        // Add a ValueEventListener to retrieve data from the Firebase database
                        ref.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                // Iterate over the data snapshot children
                                for (dsp in dataSnapshot.children) {
                                    // Get a Student object from the data snapshot
                                    val s = dsp.getValue(Student::class.java)

                                    // Check if the student is not null and has the selected student ID
                                    if (s != null && s.studentId == std) {
                                        // Update the attendance data for the selected student
                                        attendanceRef.child(format.format(attendanceDate))
                                                .child(s.classId)
                                                .child("Reported Absences")
                                                .child(s.studentId)
                                                .setValue(
                                                        mapOf(
                                                                "IsPresent" to false,
                                                                "TeacherNotified" to false
                                                        )
                                                )
                                    }
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                println("The read failed: " + databaseError.code)
                            }
                        })
                    }
                // Create the notification channel
                createChannel(notificationTitle)

                val title = "REACHED"
                val message = "Absence report has been transmitted successfully to school secretary and teachers."

                // Send the notification
                notificationManager.sendNotification(
                        title,
                        message,
                        notificationTitle,
                        context
                )
                // Display a toast message to indicate successful absence report
                Toast.makeText(context, "Absence Reported Successfully!", Toast.LENGTH_LONG).show()

                // Create a bundle to pass the parent object to the next destination
                val bundle = bundleOf("parent" to parent)

                // Navigate to the parent's main menu using the NavController, passing the bundle
                navController.navigate(R.id.action_parentAttendanceView_to_parentMainMenu, bundle)
            }
            builder.setNegativeButton("No") { _, _ ->
                // Display a toast message to indicate cancellation of submission
                Toast.makeText(context, "Cancelled Submit", Toast.LENGTH_LONG).show()
            }
            // Create an AlertDialog with the defined properties
            // Make the AlertDialog not cancelable when the user clicks outside of it
            // Show the AlertDialog on the screen
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        }
    }
}
