package com.example.reachedapp.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reachedapp.controllers.TeacherListController
import com.example.reachedapp.interfaces.OnContactClickListener
import com.example.reachedapp.models.Parent
import com.example.reachedapp.models.Teacher
import com.example.reachedapp.R
import com.example.reachedapp.views.adapters.TeacherListAdapter

class TeacherListView : Fragment(), OnContactClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var teacherAdapter: TeacherListAdapter
    private lateinit var teacherListController: TeacherListController

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_teacher_list_view, container, false)

        // Create an instance of TeacherListController with this fragment as the listener
        teacherListController = TeacherListController(this)

        // Get the Parent object passed as an argument from the previous fragment
        val parent = arguments?.getParcelable<Parent>("parent") ?: return view

        // Initialize the TeacherListAdapter with the listener and parent object
        teacherAdapter = TeacherListAdapter(listener = this, parent = parent)

        // Initialize and configure the recycler view
        recyclerView = view.findViewById<RecyclerView>(R.id.teachersList)
        recyclerView.adapter = teacherAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Fetch teachers by parent using the TeacherListController
        teacherListController.fetchTeachersByParent(parent,
                onSuccess = { teachers ->
                    teacherAdapter.setData(teachers)
                    teacherAdapter.notifyDataSetChanged()
                },
                onError = { error ->
                    println(error)
                }
        )
        return view
    }
    override fun onTeacherItemClick(teacher: Teacher, parent: Parent) {
        // Handle click event for a teacher item
        // Navigate to the parent messaging fragment passing the parent and teacher objects as arguments
        val bundle = bundleOf("teacher" to teacher, "parent" to parent)
        findNavController().navigate(R.id.action_teacherListMenu_to_parentMessaging, bundle)
    }
    override fun onParentItemClick(parent: Parent, teacher: Teacher) {
        // Handle click event for a parent item
    }
}
