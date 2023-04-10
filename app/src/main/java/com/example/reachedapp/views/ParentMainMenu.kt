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
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.reachedapp.MainActivity
import com.example.reachedapp.Models.Parent
import com.example.reachedapp.R
import com.example.reachedapp.Util.Session
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.messaging.FirebaseMessaging


class ParentMainMenu : Fragment() {
    private lateinit var gso: GoogleSignInOptions
    private lateinit var gsc: GoogleSignInClient
    private lateinit var name: TextView
    private lateinit var signOutBtn: ImageView
    private lateinit var messageButton: ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_parent_main_menu, container, false)
        val parent = arguments?.getParcelable<Parent>("parent")

        name = view.findViewById(R.id.name)
        signOutBtn = view.findViewById(R.id.signout)

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        gsc = GoogleSignIn.getClient(requireContext(), gso)
        getFCMToken()

        // Getting user from session
        val loggedInUser = Session.getUser(requireContext())
        if (loggedInUser != null) {
            name.text = loggedInUser.name
        }

        val acct = GoogleSignIn.getLastSignedInAccount(requireContext())
        if (acct != null) {
            val personName = acct.displayName
            val personEmail = acct.email

        }
        val bundle = bundleOf("parent" to parent)
        val markAttendanceBtn = view.findViewById<ImageView>(R.id.mark_attendance_btn)
        markAttendanceBtn.setOnClickListener{
            findNavController().navigate(R.id.action_parentMainMenu_to_parentAttendanceView, bundle)
        }
        signOutBtn.setOnClickListener {
            signOut()
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

    private fun signOut() {
        gsc.signOut().addOnCompleteListener(requireActivity()) {
           // Clear the session
            Session.endUserSession(requireContext())
            requireActivity().finish()
            startActivity(Intent(requireContext(), MainActivity::class.java))
        }
    }


}