package com.example.reachedapp.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import com.example.reachedapp.R


class HomeFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_home, container, false)

        val teacherAppBtn = view.findViewById<Button>(R.id.teacher_app)

        teacherAppBtn.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment3_to_teacherMainMenu)
        }

        return view
    }





}