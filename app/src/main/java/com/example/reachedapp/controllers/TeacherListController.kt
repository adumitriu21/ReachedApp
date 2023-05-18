package com.example.reachedapp.controllers

import com.example.reachedapp.interfaces.OnContactClickListener
import com.example.reachedapp.models.Parent
import com.example.reachedapp.models.Student
import com.example.reachedapp.models.Teacher
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TeacherListController(private val listener: OnContactClickListener) {

    private val database = FirebaseDatabase.getInstance()

    // Reference to the "Student" node in the Firebase Realtime Database
    private val studentsRef = database.getReference("Student")

    // Reference to the "Teacher" node in the Firebase Realtime Database
    private val teachersRef = database.getReference("Teacher")

    /**
     * Fetches teachers associated with a specific parent.
     *
     * @param parent The parent for whom to retrieve teachers.
     * @param onSuccess Callback function to handle successful retrieval of teachers.
     * @param onError Callback function to handle errors during retrieval.
     */
    fun fetchTeachersByParent(
            parent: Parent,
            onSuccess: (List<Teacher>) -> Unit,
            onError: (String) -> Unit
    ) {
        // List to store the retrieved teachers
        val teachersList: MutableList<Teacher> = mutableListOf()

        studentsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                teachersList.clear()
                val studentClassIds = mutableListOf<String>()

                // Iterate over the children of the dataSnapshot
                for (dsp in dataSnapshot.children) {
                    // Get the value of each child as a Student object
                    val student = dsp.getValue(Student::class.java)

                    // Check if the student is not null and the parent ID matches the given parent's user ID
                    if (student != null && student.parentId == parent.userId) {
                        // Add the class ID associated with the student to the list
                        studentClassIds.add(student.classId)
                    }
                }

                // Iterate over the class IDs of the students
                for (classId in studentClassIds) {
                    teachersRef.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            // Iterate over the children of the snapshot
                            for (data in snapshot.children) {
                                // Get the value of each child as a Teacher object
                                val teacher = data.getValue(Teacher::class.java)

                                // Check if the teacher is not null and its class ID matches the current class ID
                                if (teacher != null && teacher.classId == classId) {
                                    // Add the teacher to the list
                                    teachersList.add(teacher)
                                }
                            }

                            // Call the success callback with the retrieved teacher list
                            onSuccess(teachersList)
                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Call the error callback with the error message
                            onError("The read failed: ${error.code}")
                        }
                    })
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Call the error callback with the error message
                onError("The read failed: ${databaseError.code}")
            }
        })
    }

}
