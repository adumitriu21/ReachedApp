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
class AdminMainMenu : Fragment() {
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
                return inflater.inflate(R.layout.fragment_admin_main_menu, container, false)
        }

        // This method is called after the view is created, so you can access the UI elements of the layout here.
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
                super.onViewCreated(view, savedInstanceState)

                // Access the UI elements of the layout here and do whatever you want with them.
                val textView = view.findViewById<TextView>(R.id.name)
                textView.text = "Your name"
        }
}

