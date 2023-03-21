package com.example.reachedapp.views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.reachedapp.MainActivity
import com.example.reachedapp.Models.Teacher
import com.example.reachedapp.R
import com.example.reachedapp.Util.Session
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*


class TeacherMainMenu : Fragment() {

    private val database = FirebaseDatabase.getInstance()
    private val attendanceRef = database.getReference("Attendance")
    private lateinit var gso: GoogleSignInOptions
    private lateinit var gsc: GoogleSignInClient
    private lateinit var name: TextView
    private lateinit var signOutBtn: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_teacher_main_menu, container, false)
        val teacher = arguments?.getParcelable<Teacher>("teacher")
        val attendanceBtn = view.findViewById<ImageView>(R.id.take_attendance_btn)

        val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.CANADA)
        val attendanceDate = Date()

        attendanceBtn.setOnClickListener {

            /*verify if the attendance has previously been submitted. If it has been then a Toast
            message indicating to the user to try again tomorrow will pop up.
            In the even that there is no attendance entry with today's date, or that today's
            attendance has not been submitted, the Teacher will be redirected to the Teacher
            Attendance View screen*/
            attendanceRef.addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val datesList = arrayListOf<String>()
                    val currentDate = formatter.format(attendanceDate)
                    val bundle = bundleOf("teacher" to teacher)
                    for (dsp in dataSnapshot.children) {
                        dsp.key?.let { it1 -> datesList.add(it1) }
                    }
                    if (datesList.contains(currentDate)) {
                        val checkSubmitted = dataSnapshot.child(currentDate).child("IsSubmitted")
                        if (checkSubmitted.value == true) {
                            Toast.makeText(
                                requireContext(),
                                "Attendance already submitted, come back tomorrow!",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        else{

                            findNavController().navigate(R.id.action_teacherMainMenu_to_teacherAttendanceView, bundle)
                        }
                    } else {
                        findNavController().navigate(R.id.action_teacherMainMenu_to_teacherAttendanceView, bundle)
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    println("The read failed: " + databaseError.code)
                }
            })
        }

        name = view.findViewById(R.id.teacherName)
        signOutBtn = view.findViewById(R.id.signout)

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        gsc = GoogleSignIn.getClient(requireContext(), gso)

        val user = Session.getUser(requireContext())
        if (user != null) {
            name.text = user.name
        }
        val acct = GoogleSignIn.getLastSignedInAccount(requireContext())
        if (acct != null) {
            val personName = acct.displayName
            val personEmail = acct.email

        }

        signOutBtn.setOnClickListener {
            signOut()
        }

        return view
    }

    private fun signOut() {
        gsc.signOut().addOnCompleteListener(requireActivity()) {
            Session.endUserSession(requireContext())
            requireActivity().finish()
            startActivity(Intent(requireContext(), MainActivity::class.java))
        }
    }
}