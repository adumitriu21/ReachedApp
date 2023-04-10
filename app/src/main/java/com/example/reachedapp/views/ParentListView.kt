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
import com.example.reachedapp.Models.Parent
import com.example.reachedapp.Models.Student
import com.example.reachedapp.Models.Teacher
import com.example.reachedapp.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList

class ParentListView: Fragment(), OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var parentAdapter: ParentListAdapter
    private lateinit var teacher: Teacher
    private val database = FirebaseDatabase.getInstance()
    val studentsRef = database.getReference("Student")
    val parentsRef = database.getReference("Parent")
    private var parentsList: MutableList<Parent> = ArrayList<Parent>()



    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_parent_list_view, container, false)



        val teacher = arguments?.getParcelable<Teacher>("teacher") ?: return view
        val classId = teacher?.classId
        parentAdapter = ParentListAdapter(listener = this, teacher = teacher)
        recyclerView = view.findViewById<RecyclerView>(R.id.parentsList)
        recyclerView.adapter = parentAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        studentsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                parentsList.clear()
                val studentParentIds = mutableListOf<String>()

                for (dsp in dataSnapshot.children) {
                    val s = dsp.getValue(Student::class.java)
                    if (s != null && s.classId == classId) {
                        studentParentIds.add(s.parentId)
                    }
                }

                parentsRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        parentsList.clear()
                        for (data in snapshot.children) {
                            val p = data.getValue(Parent::class.java)
                            if (p != null && studentParentIds.contains(p.userId)) {
                                parentsList.add(p)
                            }
                        }
                        parentAdapter.setData(parentsList)
                        parentAdapter.notifyDataSetChanged()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        println("The read failed: " + error.code)
                    }
                })
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onParentItemClick(parent: Parent, teacher: Teacher) {
        val bundle = bundleOf("teacher" to teacher, "parent" to parent)
        findNavController().navigate(R.id.action_parentListMenu_to_teacherMessaging, bundle)
    }

    override fun onTeacherItemClick(teacher: Teacher, parent: Parent) {
        TODO("Not yet implemented")
    }


}