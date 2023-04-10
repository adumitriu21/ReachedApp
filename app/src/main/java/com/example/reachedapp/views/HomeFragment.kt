package com.example.reachedapp.views
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.reachedapp.controllers.AuthController
import com.example.reachedapp.interfaces.AuthenticationCallback
import com.example.reachedapp.models.*
import com.example.reachedapp.R
import com.example.reachedapp.repositories.UserRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var gso: GoogleSignInOptions
    private lateinit var gsc: GoogleSignInClient
    private lateinit var googleBtn: ImageView
    private lateinit var auth: FirebaseAuth
    private var database = FirebaseDatabase.getInstance()
    private lateinit var authController: AuthController
    private lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        authController = AuthController(auth)
        userRepository = UserRepository(database)

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
                // Launch a coroutine to wait for the database operation to complete
                lifecycleScope.launch {
                    val user = userRepository.getUserFromDatabase( email)
                    if (user == null) {
                        // User not found, display an error message
                        Toast.makeText(
                            activity,
                            "Invalid email or password. Please try again",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // User found, proceed with login
                        authenticateUser(user as User)
                    }
                }
            } else {
                Toast.makeText(
                    activity,
                    "Please enter username and password",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun authenticateUser(user: User) {
        authController.authenticateUser(requireContext(), user, object : AuthenticationCallback {
            override fun onAuthenticationSuccess(user: User) {
                // Redirect to appropriate menu based on user role
                when (user) {
                    is Teacher -> {
                        val action = HomeFragmentDirections.actionHomeFragment3ToTeacherMainMenu(user)
                        findNavController().navigate(action)
                    }
                    is Parent -> {
                        val action = HomeFragmentDirections.actionHomeFragment3ToParentMainMenu(user)
                        findNavController().navigate(action)
                    }
                    is Admin -> {
                        val action = HomeFragmentDirections.actionHomeFragment3ToAdminMainMenu22(user)
                        findNavController().navigate(action)
                    }
                }
            }

            override fun onAuthenticationFailure() {
                Toast.makeText(activity, "Login failed. Please try again.", Toast.LENGTH_SHORT).show()
            }
        })


    }




    private fun signIn() {
        val signInIntent = gsc.signInIntent
        startActivityForResult(signInIntent, 1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    val email = account.email
                    if (email != null) {
                        // Launch a coroutine to wait for the database operation to complete
                        lifecycleScope.launch {
                            val user = userRepository.getUserFromDatabase(email)
                            if (user == null) {
                                Toast.makeText(
                                    requireContext(),
                                    "No user found for this Google account. Please try another account or log in with your email and password",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                // User found, proceed with login
                                authenticateUser(user as User)
                            }
                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Failed to get email address from Google account. Please try another account or log in with your email and password",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Failed to sign in with Google. Please try again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: ApiException) {
                Toast.makeText(
                    requireContext(),
                    "Failed to sign in with Google. Please try again",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}





