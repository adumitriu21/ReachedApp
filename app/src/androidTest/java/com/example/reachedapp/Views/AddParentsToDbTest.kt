package com.example.reachedapp.Views

import android.util.Log
import com.example.reachedapp.Models.Parent
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.FirebaseDatabase
import org.junit.Before
import org.junit.Test
import kotlin.coroutines.cancellation.CancellationException

class AddParentsToDbTest {


    private val database = FirebaseDatabase.getInstance()
    private val dbRef= database.reference
    private lateinit var parentList: com.example.reachedapp.data.ParentList
    private lateinit var parents: ArrayList<Parent>

    //function that runs before any tests begin
    @Before
    fun setUp(){
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
        }
        try{
            val task = dbRef.child("Parent").setValue(taskMap)
            Tasks.await(task)
        } catch (e: CancellationException) {
            Log.d("TAG", "Could not push data to DB. Error: $e")
        }
    }
}