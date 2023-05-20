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
import com.example.reachedapp.controllers.ParentListController
import com.example.reachedapp.interfaces.OnContactClickListener
import com.example.reachedapp.models.Parent
import com.example.reachedapp.models.Teacher
import com.example.reachedapp.R
import com.example.reachedapp.views.adapters.ParentListAdapter

class ParentListView : Fragment(), OnContactClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var parentAdapter: ParentListAdapter
    private lateinit var parentListController: ParentListController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_parent_list_view, container, false)

        // Create an instance of ParentListController with this fragment as the listener
        parentListController = ParentListController(this)

        // Get the Teacher object passed as an argument from the previous fragment
        val teacher = arguments?.getParcelable<Teacher>("teacher") ?: return view
        val classId = teacher.classId

        // Initialize the ParentListAdapter with the listener and teacher object
        parentAdapter = ParentListAdapter(listener = this, teacher = teacher)

        // Initialize and configure the recycler view
        recyclerView = view.findViewById(R.id.parentsList)
        recyclerView.adapter = parentAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Fetch parents by classId using the ParentListController
        parentListController.fetchParentsByClassId(classId,
            onSuccess = { parents ->
                parentAdapter.setData(parents)
                parentAdapter.notifyDataSetChanged()
            },
            onError = { error ->
                println(error)
            }
        )

        return view
    }

    override fun onParentItemClick(parent: Parent, teacher: Teacher) {
        // Handle click event for a parent item
        // Navigate to the teacher messaging fragment passing the teacher and parent objects as arguments
        val bundle = bundleOf("teacher" to teacher, "parent" to parent)
        findNavController().navigate(R.id.action_parentListMenu_to_teacherMessaging, bundle)
    }

    override fun onTeacherItemClick(teacher: Teacher, parent: Parent) {
        // Handle click event for a teacher item
    }
}
