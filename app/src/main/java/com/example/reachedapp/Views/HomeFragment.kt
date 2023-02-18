package com.example.reachedapp.Views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.reachedapp.MainActivity
import com.example.reachedapp.MainActivity2
import com.example.reachedapp.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException


class HomeFragment : Fragment() {

    private lateinit var gso: GoogleSignInOptions
    private lateinit var gsc: GoogleSignInClient
    private lateinit var googleBtn: ImageView
    private lateinit var userTypeRadioGroup: RadioGroup

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
            if (userTypeRadioGroup.checkedRadioButtonId == R.id.parent_radio_btn) {
                setUserType(0)
            } else {
                setUserType(1)
            }
            signIn()
        }

        userTypeRadioGroup = view.findViewById(R.id.user_type_radio_group)

        return view
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

    private fun setUserType(userType: Int) {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putInt(getString(R.string.user_type_key), userType)
            apply()
        }
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