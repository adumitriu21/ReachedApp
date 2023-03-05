package com.example.reachedapp.Views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.reachedapp.MainActivity
import com.example.reachedapp.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.database.FirebaseDatabase


class ParentMainMenu : Fragment() {
    private val database = FirebaseDatabase.getInstance()
    private val attendanceRef = database.getReference("Attendance")
    private lateinit var gso: GoogleSignInOptions
    private lateinit var gsc: GoogleSignInClient
    private lateinit var name: TextView
    private lateinit var signOutBtn: ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_parent_main_menu, container, false)
        val markAttendanceBtn = view.findViewById<ImageView>(R.id.mark_attendance_btn)
        markAttendanceBtn.setOnClickListener{
            findNavController().navigate(R.id.action_parentMainMenu_to_parentAttendanceView)
        }

        name = view.findViewById(R.id.name)
        signOutBtn = view.findViewById(R.id.signout)

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        gsc = GoogleSignIn.getClient(requireContext(), gso)

        val acct = GoogleSignIn.getLastSignedInAccount(requireContext())
        if (acct != null) {
            val personName = acct.displayName
            val personEmail = acct.email
            name.text = personName
        }

        signOutBtn.setOnClickListener {
            signOut()
        }
        return view
    }

    private fun signOut() {
        gsc.signOut().addOnCompleteListener(requireActivity()) {
            requireActivity().finish()
            startActivity(Intent(requireContext(), MainActivity::class.java))
        }
    }
}