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

class TeacherListView: Fragment(), OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var teacherAdapter: TeacherListAdapter
    private lateinit var parent: Parent
    private val database = FirebaseDatabase.getInstance()
    val studentsRef = database.getReference("Student")
    val parentsRef = database.getReference("Parent")
    val teachersRef = database.getReference("Teacher")
    private var teachersList: MutableList<Teacher> = ArrayList<Teacher>()



    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_teacher_list_view, container, false)

        val parent = arguments?.getParcelable<Parent>("parent") ?: return view

        teacherAdapter = TeacherListAdapter(listener = this, parent = parent)
        recyclerView = view.findViewById<RecyclerView>(R.id.teachersList)
        recyclerView.adapter = teacherAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        studentsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                teachersList.clear()
                val studentClassIds = mutableListOf<String>()

                for (dsp in dataSnapshot.children) {
                    val s = dsp.getValue(Student::class.java)
                    if (s != null && s.parentId == parent.userId) {
                        studentClassIds.add(s.classId)
                    }
                }
                println(studentClassIds)
                for (classId in studentClassIds) {
                    teachersRef.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (data in snapshot.children) {
                                val t = data.getValue(Teacher::class.java)
                                if (t != null && t.classId == classId)
                                {
                                    teachersList.add(t)
                                }
                            }

                            teacherAdapter.setData(teachersList)
                            teacherAdapter.notifyDataSetChanged()
                        }

                        override fun onCancelled(error: DatabaseError) {
                            println("The read failed: " + error.code)
                        }
                    })
                    }
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

    }

    override fun onTeacherItemClick(teacher: Teacher, parent: Parent) {
        val bundle = bundleOf("teacher" to teacher, "parent" to parent)
        findNavController().navigate(R.id.action_teacherListMenu_to_parentMessaging, bundle)
    }


}