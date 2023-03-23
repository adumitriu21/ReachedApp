package com.example.reachedapp.views

import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reachedapp.Models.Student
import com.example.reachedapp.Models.Teacher
import com.example.reachedapp.R
import com.example.reachedapp.sendNotification
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*


class TeacherAttendanceView : Fragment() {
    private lateinit var notificationManager: NotificationManager
    private val database = FirebaseDatabase.getInstance()
    val ref = database.getReference("Student")
    val attendanceRef = database.getReference("Attendance")
    private lateinit var dateTimeDisplay: TextView
    private lateinit var dayOfWeek: TextView
    private lateinit var calendar: Calendar
    private lateinit var dateFormat: SimpleDateFormat
    private lateinit var date: String
    private var studentList: MutableList<Student> = ArrayList<Student>()
    private var studentAdapter = StudentListAdapter()
    private lateinit var studentRecyclerView : RecyclerView
    //private val homeroom = arrayOf("107", "108")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_teacher_attendance_view, container, false)

        //Creating the instance of ArrayAdapter containing list of fruit names
        //val adapter = ArrayAdapter(requireActivity(), R.layout.drpdown_item, homeroom)
/*
        //Getting the instance of AutoCompleteTextView
        val homeroomSelect = view.findViewById<AutoCompleteTextView>(R.id.homeroomSelect)
        homeroomSelect.threshold = 1 //will start working from first character
        homeroomSelect.setAdapter(adapter) //setting the adapter data into the AutoCompleteTextView
*/
        val teacher = arguments?.getParcelable<Teacher>("teacher")
        //display date
        dateTimeDisplay = view.findViewById(R.id.date)
        calendar = Calendar.getInstance()
        dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.CANADA)
        date = dateFormat.format(calendar.time)
        dateTimeDisplay.text = date
        //display day of the week
        dayOfWeek = view.findViewById(R.id.day)
        dayOfWeek.text = LocalDate.now().dayOfWeek.name

        val searchBar = view.findViewById<EditText>(R.id.searchByName)

        studentRecyclerView = view.findViewById<RecyclerView>(R.id.studentsList)
        studentRecyclerView.adapter = studentAdapter
        studentRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.CANADA)
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

        /*
        homeroomSelect.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                val selectedHomeroom = parent.getItemAtPosition(position)
                studentList.clear()
                ref.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (dsp in dataSnapshot.children) {
                            val s = dsp.getValue(Student::class.java)
                            if (s != null && s.classId == selectedHomeroom.toString()) {
                                studentList.add(s)
                                attendanceRef.addListenerForSingleValueEvent(object :
                                    ValueEventListener {
                                    override fun onDataChange(attSnapshot: DataSnapshot) {
                                        if (!attSnapshot.child(formatter.format(attendanceDate))
                                                .child(s.classId.toString())
                                                .child(s.name).hasChild("IsPresent")
                                        ) {
                                            attendanceRef.child(formatter.format(attendanceDate))
                                                .child(s.classId.toString())
                                                .child(s.classId)
                                                .child("IsPresent")
                                                .setValue(true)
                                        }
                                    }

                                    override fun onCancelled(attError: DatabaseError) {
                                        println("The read failed: " + attError.code)
                                    }
                                })

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
*/
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                filter(p0.toString())

            }

            override fun afterTextChanged(editable: Editable?) {

            }
        })

        val homeroomNum = teacher?.homeroomNumber

        // Populate the class list
        if (homeroomNum != null) {
            populateClassList(homeroomNum, attendanceDate, formatter)
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
                            attendanceRef.child(formatter.format(attendanceDate)).child(homeroomNum.toString())
                                .child("IsSubmitted")
                                .setValue(true)
                    }
                    override fun onCancelled(databaseError: DatabaseError) {
                        println("The read failed: " + databaseError.code)
                    }
                })
                createChannel(getString(R.string.comment_notification_channel_id),
                        "12345")
                val title = "REACHED"
                val message = "Absence report has been transmitted successfully to notify parents."
                notificationManager.sendNotification(
                        title,
                        message,
                        getString(R.string.comment_notification_channel_id),
                        requireContext()
                )

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notificationManager = ContextCompat.getSystemService(requireContext(),
                NotificationManager::class.java) as NotificationManager
    }

    private fun createChannel(channelId : String, channelName : String){
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_HIGH
            ).apply {
                enableLights(true)
                enableVibration(true)
                lightColor = Color.GREEN
            }
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private  fun filter(e: String) {
        //Declare the array list that holds the filtered values
        val filteredStudents = ArrayList<Student>()
        // loop through the array list to obtain the required value
        for (student in studentList) {
            if (student.name.lowercase().contains(e.lowercase())) {
                filteredStudents.add(student)
            }
        }
        // add the filtered value to adapter
        studentAdapter.setData(filteredStudents)

    }

    private fun populateClassList(homeroom: String, date: Date, dateFormat: SimpleDateFormat ){

        // Retrieve the list of all Student objects from your data source

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dsp in dataSnapshot.children) {
                    val s = dsp.getValue(Student::class.java)
                    if (s != null && s.classId == homeroom) {
                        studentList.add(s)
                        attendanceRef.addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(attSnapshot: DataSnapshot) {
                                if (!attSnapshot.child(dateFormat.format(date))
                                        .child(s.classId)
                                        .child(s.name).hasChild("IsPresent")
                                ) {
                                    attendanceRef.child(dateFormat.format(date))
                                        .child(s.classId)
                                        .child(s.classId)
                                        .child("IsPresent")
                                        .setValue(true)
                                }
                            }

                            override fun onCancelled(attError: DatabaseError) {
                                println("The read failed: " + attError.code)
                            }
                        })

                    }
                    attendanceRef.child(dateFormat.format(date)).child(homeroom)
                        .child("IsSubmitted").setValue(false)
                    studentAdapter.setData(studentList)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })

    }

}