package com.example.reachedapp.controllers

import com.example.reachedapp.interfaces.OnContactClickListener
import com.example.reachedapp.models.Parent
import com.example.reachedapp.models.Student
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ParentListController(private val listener: OnContactClickListener) {

    private val database = FirebaseDatabase.getInstance()

    // Reference to the "Parent" node in the Firebase Realtime Database
    private val parentsRef = database.getReference("Parent")

    // Reference to the "Student" node in the Firebase Realtime Database
    private val studentsRef = database.getReference("Student")

    /**
     * Fetches parents associated with a specific class ID.
     *
     * @param classId The ID of the class to retrieve parents for.
     * @param onSuccess Callback function to handle successful retrieval of parents.
     * @param onError Callback function to handle errors during retrieval.
     */
    fun fetchParentsByClassId(
        classId: String?,
        onSuccess: (List<Parent>) -> Unit,
        onError: (String) -> Unit
    ) {
        // List to store the retrieved parents
        val parentList: MutableList<Parent> = mutableListOf()

        // List to store the parent IDs associated with the given class ID
        val studentParentIds: MutableList<String> = mutableListOf()

        // Listen for changes in the "Student" node
        studentsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                studentParentIds.clear()

                // Iterate over the children of the dataSnapshot
                for (dsp in dataSnapshot.children) {
                    // Get the value of each child as a Student object
                    val student = dsp.getValue(Student::class.java)

                    // Check if the student is not null and the class ID matches the given class ID
                    if (student != null && student.classId == classId) {
                        // Add the parent ID associated with the student to the list
                        studentParentIds.add(student.parentId)
                    }
                }

                // Listen for changes in the "Parent" node
                parentsRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        parentList.clear()

                        // Iterate over the children of the snapshot
                        for (data in snapshot.children) {
                            // Get the value of each child as a Parent object
                            val parent = data.getValue(Parent::class.java)

                            // Check if the parent is not null and its user ID is in the studentParentIds list
                            if (parent != null && studentParentIds.contains(parent.userId)) {
                                // Add the parent to the list
                                parentList.add(parent)
                            }
                        }
                        // Call the success callback with the retrieved parent list
                        onSuccess(parentList)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        onError("The read failed: ${error.code}")
                    }
                })
            }

            override fun onCancelled(databaseError: DatabaseError) {
                onError("The read failed: ${databaseError.code}")
            }
        })
    }
}
