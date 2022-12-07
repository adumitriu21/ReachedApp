package com.example.reachedapp.Views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.reachedapp.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*


class TeacherMainMenu : Fragment() {

    private val database = FirebaseDatabase.getInstance()
    private val attendanceRef = database.getReference("Attendance")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_teacher_main_menu, container, false)

        val attendanceBtn = view.findViewById<Button>(R.id.take_attendance_btn)

        val formatter = SimpleDateFormat("dd MMMM yyyy")
        val attendanceDate = Date()

        attendanceBtn.setOnClickListener{

            /*verify if the attendance has previously been submitted. If it has been then a Toast
            message indicating to the user to try again tomorrow will pop up.
            In the even that there is no attendance entry with today's date, or that today's
            attendance has not been submitted, the Teacher will be redirected to the Teacher
            Attendance View screen*/
            attendanceRef.addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val datesList = arrayListOf<String>()
                    val currentDate = formatter.format(attendanceDate)
                    for (dsp in dataSnapshot.children) {
                        dsp.key?.let { it1 -> datesList.add(it1) }
                    }

                    if(datesList.contains(currentDate)){
                            val checkSubmitted = dataSnapshot.child(currentDate).child("IsSubmitted")

                            if(checkSubmitted.value == true){
                                Toast.makeText(requireContext(), "Attendance already submitted, come back tomorrow!", Toast.LENGTH_LONG).show()

                        }
                        else{
                            findNavController().navigate(R.id.action_teacherMainMenu_to_teacherAttendanceView)
                        }

                    }

                }

                override fun onCancelled(databaseError: DatabaseError) {
                    println("The read failed: " + databaseError.code)
                }
            })




        }

        return view
    }



}