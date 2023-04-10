package com.example.reachedapp.views

import android.util.Log
import com.example.reachedapp.models.Parent
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import org.junit.Before
import org.junit.Test
import kotlin.coroutines.cancellation.CancellationException

class AddParentsToDbTest {

    private lateinit var auth: FirebaseAuth

    private val database = FirebaseDatabase.getInstance()
    private val dbRef= database.reference
    private lateinit var parentList: com.example.reachedapp.data.ParentList
    private lateinit var parents: ArrayList<Parent>

    //function that runs before any tests begin
    @Before
    fun setUp(){
        auth = FirebaseAuth.getInstance()

        //FirebaseApp.initializeApp(InstrumentationRegistry.getInstrumentation().targetContext)
        parentList = com.example.reachedapp.data.ParentList()
        parents = parentList.initializeParentList()
    }

    //test that uses the data initialized in the StudentList class and inserts in
    //in the Firebase DB
    @Test
    fun addNewUser(){
        val taskMap: MutableMap<String, Any> = HashMap()

        for(parent in parents){
            taskMap[parent.userId] = parent

            auth.createUserWithEmailAndPassword(parent.email, parent.password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("Test", "User created successfully")
                    } else {
                        Log.e("Test", "Error creating user", task.exception)
                    }
                }
        }
        try{
            val task = dbRef.child("Parent").setValue(taskMap)
            Tasks.await(task)


        } catch (e: CancellationException) {
            Log.d("TAG", "Could not push data to DB. Error: $e")
        }
    }
}