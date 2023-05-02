package com.example.reachedapp.views

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.example.reachedapp.models.Parent
import com.example.reachedapp.R
import com.example.reachedapp.controllers.GoogleAuthController
import com.example.reachedapp.interfaces.OnGoogleAuthListener
import com.example.reachedapp.util.Session
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.messaging.FirebaseMessaging


class ParentMainMenu : Fragment(), OnGoogleAuthListener {
    private lateinit var name: TextView
    private lateinit var signOutBtn: ImageView
    private lateinit var messageButton: ImageView
    private lateinit var googleAuthController: GoogleAuthController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_parent_main_menu, container, false)
        name = view.findViewById(R.id.name)
        //get parent object from arguments
        val parent = arguments?.getParcelable<Parent>("parent")
        if (parent != null) {
            name.text = parent.name //set display name
        }
        // Initialize GoogleAuthController
        context?.let {
            googleAuthController = GoogleAuthController(it, this)
        }

        signOutBtn = view.findViewById(R.id.signout)
        getFCMToken()

        val bundle = bundleOf("parent" to parent)
        val markAttendanceBtn = view.findViewById<ImageView>(R.id.mark_attendance_btn)
        markAttendanceBtn.setOnClickListener{
            findNavController().navigate(R.id.action_parentMainMenu_to_parentAttendanceView, bundle)
        }
        signOutBtn.setOnClickListener {
            googleAuthController.signOut(requireActivity())
        }

        messageButton = view.findViewById(R.id.message_teacher_btn)

        messageButton.setOnClickListener{
            val bundle = bundleOf("parent" to parent)
            findNavController().navigate(R.id.action_parentMainMenu_to_teacherListMenu, bundle)
        }


        return view
    }

    private fun getFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log the token and/or send it to your server
            Log.d(TAG, "FCM registration token: $token")
        }
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