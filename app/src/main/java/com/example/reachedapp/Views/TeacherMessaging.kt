package com.example.reachedapp.Views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.reachedapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class TeacherMessaging : Fragment() {
    private lateinit var bottomNavigationView: BottomNavigationView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_teacher_messaging, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomNavigationView = view.findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    // Navigate to home
                    findNavController().navigate(R.id.action_teacherMainMenu_to_teacherAttendanceView)
                    true
                }
                R.id.navigation_report -> {
                    // Navigate to report
                    findNavController().navigate(R.id.action_teacherMessaging_to_teacherAttendanceView)
                    true
                }
                R.id.navigation_messages -> {
                    // Navigate to messages
                    findNavController().navigate(R.id.action_teacherMessaging_self)
                    true
                }
                R.id.navigation_profile -> {
                    // Navigate to profile
                    findNavController().navigate(R.id.action_teacherMessaging_to_teacherProfileView)
                    true
                }
                else -> false
            }
        }
    }
}
