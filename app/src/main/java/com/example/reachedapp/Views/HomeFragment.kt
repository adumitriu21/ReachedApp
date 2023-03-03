package com.example.reachedapp.Views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.reachedapp.Models.Parent
import com.example.reachedapp.Models.Teacher
import com.example.reachedapp.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HomeFragment : Fragment() {

    private lateinit var gso: GoogleSignInOptions
    private lateinit var gsc: GoogleSignInClient
    private lateinit var googleBtn: ImageView
    private lateinit var auth: FirebaseAuth
    private var database = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        ///////google oauth ////////////////
        googleBtn = view.findViewById(R.id.google_btn)

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        gsc = GoogleSignIn.getClient(requireActivity(), gso)

        val acct = GoogleSignIn.getLastSignedInAccount(requireActivity())
        if (acct != null) {
            navigateToAppropriateMainMenu()
        }

        googleBtn.setOnClickListener {
            signIn()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val loginBtn = view.findViewById<Button>(R.id.login_btn)
        val emailTxt = view.findViewById<EditText>(R.id.login_email)
        val pwdTxt = view.findViewById<EditText>(R.id.login_password)
        // Set up the login button click listener
        loginBtn.setOnClickListener {
            val email = emailTxt.text.toString()
            val password = pwdTxt.text.toString()

            // Check if email and password are not empty
            if (email.isNotEmpty() && password.isNotEmpty()) {
                val usersRef = database.reference

                // Retrieve the user's login credentials from the database
                usersRef.child("Teacher").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                            // Loop through all the teachers with matching email
                            for (teacherSnapshot in dataSnapshot.children) {
                                val teacher = teacherSnapshot.getValue(Teacher::class.java)

                                if (teacher != null && teacher.password == password) {
                                    // Authenticate the teacher with Firebase
                                    auth.signInWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(requireActivity()) { task ->
                                            if (task.isSuccessful) {
                                                val action = HomeFragmentDirections.actionHomeFragment3ToTeacherMainMenu(teacher)
                                                findNavController().navigate(action)

                                            } else {
                                                // Login failed, display an error message
                                                Toast.makeText(
                                                    activity,
                                                    "Login failed. Please try again.",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    return
                                }
                            }


                        // If the email is not found in the Teachers entity, check the Students entity
                        usersRef.child("Parent").orderByChild("email").equalTo(email)
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        // Loop through all the students with matching email
                                        for (studentSnapshot in dataSnapshot.children) {
                                            val student =
                                                studentSnapshot.getValue(Parent::class.java)

                                            if (student != null && student.password == password) {
                                                // Authenticate the student with Firebase
                                                auth.signInWithEmailAndPassword(email, password)
                                                    .addOnCompleteListener(requireActivity()) { task ->
                                                        if (task.isSuccessful) {
                                                            navigateToParentMainMenu()
                                                        } else {
                                                            // Login failed, display an error message
                                                            Toast.makeText(
                                                                activity,
                                                                "Login failed. Please try again.",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        }
                                                    }
                                                return
                                            }
                                        }
                                    }

                                    // Login failed, display an error message
                                    Toast.makeText(
                                        activity,
                                        "Invalid email or password. Please try again",
                                        Toast.LENGTH_SHORT
                                    ).show()


                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Toast.makeText(activity,
                                        "DB Error",
                                        Toast.LENGTH_SHORT).show()
                                }
                            })
                    }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(activity,
                                "DB Error",
                                Toast.LENGTH_SHORT).show()
                        }
                    })
            }
        }
    }

    private fun navigateToAppropriateMainMenu() {
        if (isUserParent()) {
            navigateToParentMainMenu()
        } else {
            navigateToTeacherMainMenu()
        }
    }

    private fun isUserParent(): Boolean {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return true
        val userType = sharedPref.getInt(getString(R.string.user_type_key), -1)
        return userType == 0
    }

//    private fun setUserType(userType: Int) {
//        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
//        with(sharedPref.edit()) {
//            putInt(getString(R.string.user_type_key), userType)
//            apply()
//        }
//    }

    private fun signIn() {
        val signInIntent = gsc.signInIntent
        startActivityForResult(signInIntent, 1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                task.getResult(ApiException::class.java)
                navigateToAppropriateMainMenu()
            } catch (e: ApiException) {
                Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToTeacherMainMenu() {

        findNavController().navigate(R.id.action_homeFragment3_to_teacherMainMenu)
    }

    private fun navigateToParentMainMenu() {
        findNavController().navigate(R.id.action_homeFragment3_to_parentMainMenu)
    }

}