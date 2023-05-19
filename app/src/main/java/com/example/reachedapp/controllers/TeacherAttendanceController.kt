package com.example.reachedapp.controllers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reachedapp.R
import com.example.reachedapp.models.Student
import com.example.reachedapp.models.Teacher
import com.example.reachedapp.repositories.AttendanceRepository
import com.example.reachedapp.util.NOTIFICATION_CHANNEL_ID
import com.example.reachedapp.util.NOTIFICATION_CHANNEL_NAME
import com.example.reachedapp.sendNotification
import com.example.reachedapp.views.TeacherAttendanceView
import com.example.reachedapp.views.adapters.StudentListAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class TeacherAttendanceController(
        private val context: Context,
        private val navController: NavController,
        private val teacher: Teacher?,
        private val view: TeacherAttendanceView
) {
    private val attendanceRepo: AttendanceRepository = AttendanceRepository()
    private val database = FirebaseDatabase.getInstance()
    private val ref = database.getReference("Student")
    private val studentList: MutableList<Student> = mutableListOf()
    private val studentAdapter = StudentListAdapter()

    fun initialize() {
        // Set current date and day of the week in the view
        view.dateTimeDisplay.text = getCurrentDate()
        view.dayOfWeek.text = getCurrentDayOfWeek()

        // Listen for text changes in the search bar and filter the student list accordingly
        view.searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                filterStudents(p0.toString())
            }

            override fun afterTextChanged(editable: Editable?) {}
        })

        // Set the attendance date and formatter
        val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.CANADA)
        val attendanceDate = Date()

        // Retrieve the classId from the teacher object and populate the class list if classId is not null
        val classId: String = teacher?.classId ?: ""
        if (classId != null) {
            populateClassList(classId, attendanceDate, formatter)
        }

        // Set the student adapter and layout manager for the recycler view
        view.studentRecyclerView.adapter = studentAdapter
        view.studentRecyclerView.layoutManager = LinearLayoutManager(context)

        // Set a click listener for the submit button to show a confirmation dialog
        view.submitBtn.setOnClickListener {
            showConfirmationDialog(classId)
        }

    }

    // Function to get the current date in MM/dd/yyyy format
    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.CANADA)
        return dateFormat.format(calendar.time)
    }

    // Function to get the current day of the week
    private fun getCurrentDayOfWeek(): String {
        return LocalDate.now().dayOfWeek.name
    }

    // Function to filter the student list based on the search query
    private fun filterStudents(query: String) {
        val filteredStudents = studentList.filter { student ->
            student.name.lowercase().contains(query.lowercase())
        }
        // Set the filtered student data in the student adapter
        studentAdapter.setData(filteredStudents)
    }

    // Function to populate the class list based on the homeroom and attendance date
    private fun populateClassList(homeroom: String, date: Date, dateFormat: SimpleDateFormat) {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dsp in dataSnapshot.children) {
                    val student = dsp.getValue(Student::class.java)
                    if (student != null && student.classId == homeroom) {
                        // Add the student to the student list
                        studentList.add(student)

                        // Set the attendance of the student as present in the database
                        val attendanceRef = database.getReference("Attendance").child(dateFormat.format(date)).child(student.classId).child(student.studentId)
                        attendanceRef.child("IsPresent").setValue(true)
                    }
                }
                // Set the "IsSubmitted" field for the attendance of the homeroom on the specific date
                database.getReference("Attendance").child(dateFormat.format(date)).child(homeroom).child("IsSubmitted")
                // Set the student list data in the student adapter
                studentAdapter.setData(studentList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: ${databaseError.code}")
            }
        })
    }

    // Function to show the confirmation dialog for submitting the attendance
    private fun showConfirmationDialog(classId: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.dialogTitle)
        builder.setMessage(R.string.dialogMessage)
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        // If the user confirms, submit the attendance
        builder.setPositiveButton("Yes") { dialogInterface, which ->
            submitAttendance(classId)
        }

        // If the user cancels, show a toast message
        builder.setNegativeButton("No") { dialogInterface, which ->
            Toast.makeText(context, "Cancelled Submit", Toast.LENGTH_LONG).show()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    // Function to submit the attendance
    private fun submitAttendance(classId: String) {
        attendanceRepo.onAttendanceSubmitted(classId)
                .addOnSuccessListener {
                    // Create a notification channel
                    createNotificationChannel()

                    // Send a notification to notify parents about the absence report
                    val title = "REACHED"
                    val message = "Absence report has been transmitted successfully to notify parents."
                    val notificationManager = ContextCompat.getSystemService(
                            context,
                            NotificationManager::class.java
                    ) as NotificationManager
                    notificationManager.sendNotification(
                            title,
                            message,
                            NOTIFICATION_CHANNEL_ID,
                            context
                    )
                    Toast.makeText(context, "Attendance Submitted", Toast.LENGTH_LONG).show()

                    // Navigate back to the teacher main menu with the teacher object
                    val bundle = bundleOf("teacher" to teacher)
                    navController.navigate(R.id.action_teacherAttendanceView_to_teacherMainMenu, bundle)
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(
                            context,
                            "Error submitting attendance: ${exception.message}",
                            Toast.LENGTH_LONG
                    ).show()
                }
    }

    // Function to create a notification channel for high importance notifications
    private fun createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val notificationManager =
                    ContextCompat.getSystemService(context, NotificationManager::class.java)
                            as NotificationManager
            val notificationChannel = NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            ).apply {
                enableLights(true)
                enableVibration(true)
                lightColor = Color.GREEN
            }
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}
