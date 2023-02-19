package com.example.reachedapp.Views

import android.util.Log
import com.example.reachedapp.Models.Teacher
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.FirebaseDatabase
import org.junit.Before
import org.junit.Test
import kotlin.coroutines.cancellation.CancellationException

class AddTeachersToDbTest {


    private val database = FirebaseDatabase.getInstance()
    private val dbRef= database.reference
    private lateinit var teacherList: com.example.reachedapp.data.TeacherList
    private lateinit var teachers: ArrayList<Teacher>

    //function that runs before any tests begin
    @Before
    fun setUp(){
        //FirebaseApp.initializeApp(InstrumentationRegistry.getInstrumentation().targetContext)
        teacherList = com.example.reachedapp.data.TeacherList()
        teachers = teacherList.initializeTeacherList()
    }

    //test that uses the data initialized in the StudentList class and inserts in
    //in the Firebase DB
    @Test
    fun addNewUser(){
        val taskMap: MutableMap<String, Any> = HashMap()

        for(teacher in teachers){
            taskMap[teacher.userId] = teacher
        }
        try{
            val task = dbRef.child("Teacher").setValue(taskMap)
            Tasks.await(task)
        } catch (e: CancellationException) {
            Log.d("TAG", "Could not push data to DB. Error: $e")
        }
    }
}