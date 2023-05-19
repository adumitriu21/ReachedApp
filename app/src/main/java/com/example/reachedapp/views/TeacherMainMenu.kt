package com.example.reachedapp.views

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
import com.example.reachedapp.models.Teacher
import com.example.reachedapp.R
import com.example.reachedapp.controllers.GoogleAuthController
import com.example.reachedapp.interfaces.OnGoogleAuthListener
import com.example.reachedapp.repositories.AttendanceRepository
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

class TeacherMainMenu : Fragment(), OnGoogleAuthListener {

    private lateinit var name: TextView
    private lateinit var signOutBtn: ImageView
    private lateinit var messageButton: ImageView
    private lateinit var attendanceRepo: AttendanceRepository
    private lateinit var googleAuthController: GoogleAuthController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_teacher_main_menu, container, false)
        val teacher = arguments?.getParcelable<Teacher>("teacher")
        name = view.findViewById(R.id.teacherName)
        if (teacher != null) {
            name.text = teacher.name
        }

        val attendanceBtn = view.findViewById<ImageView>(R.id.take_attendance_btn)
        attendanceRepo = AttendanceRepository()

        // Initialize GoogleAuthController
        context?.let {
            googleAuthController = GoogleAuthController(it, this)
        }

        val isSubmitted = arguments?.getBoolean("isSubmitted") ?: false //TEMPORARY WORK AROUND
        attendanceBtn.setOnClickListener {

            /*verify if the attendance has previously been submitted. If it has been then a Toast
            message indicating to the user to try again tomorrow will pop up.
            In the even that there is no attendance entry with today's date, or that today's
            attendance has not been submitted, the Teacher will be redirected to the Teacher
            Attendance View screen*/
            if(isSubmitted){ //TEMPORARY WORK AROUND
                Toast.makeText( //TEMPORARY WORK AROUND
                    requireContext(), //TEMPORARY WORK AROUND
                    "Attendance already submitted, come back tomorrow!", //TEMPORARY WORK AROUND
                    Toast.LENGTH_LONG //TEMPORARY WORK AROUND
                ).show() //TEMPORARY WORK AROUND
            }else{

            attendanceRepo.isAttendanceSubmitted(teacher?.classId ?: "") { isSubmitted ->
                if (isSubmitted) {
                    Toast.makeText(
                        requireContext(),
                        "Attendance already submitted, come back tomorrow!",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    val bundle = bundleOf("teacher" to teacher)
                    findNavController().navigate(
                        R.id.action_teacherMainMenu_to_teacherAttendanceView,
                        bundle
                    )
                }
            }
            }
        }
        signOutBtn = view.findViewById(R.id.signout)

        signOutBtn.setOnClickListener {
            googleAuthController.signOut(requireActivity())
        }

        messageButton = view.findViewById(R.id.message_parent_btn)

        messageButton.setOnClickListener{
            val bundle = bundleOf("teacher" to teacher)
            findNavController().navigate(R.id.action_teacherMainMenu_to_parentListMenu, bundle)
        }

        return view
    }


    override fun onGoogleAuthStart() {
        // Handle onGoogleAuthStart event
    }

    override fun onGoogleAuthSuccess(account: GoogleSignInAccount) {
        // Handle onGoogleAuthSuccess event
    }

    override fun onGoogleAuthError() {
        // Handle onGoogleAuthError event
    }

    override fun onGoogleSignOutSuccess() {
        Toast.makeText(requireContext(), "Successfully signed out!", Toast.LENGTH_SHORT).show()
    }

    override fun onGoogleSignOutError() {
        Toast.makeText(requireContext(), "Error Signing Out!", Toast.LENGTH_SHORT).show()
    }

}