package com.example.reachedapp.views

import android.util.Log
import com.example.reachedapp.Models.Student
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.FirebaseDatabase
import org.junit.Before
import org.junit.Test
import kotlin.coroutines.cancellation.CancellationException

class AddStudentsToDbTest {

    private val database = FirebaseDatabase.getInstance()
    private val dbRef= database.reference
    private lateinit var studentList: com.example.reachedapp.data.StudentList
    private lateinit var students: ArrayList<Student>

    //function that runs before any tests begin
    @Before
    fun setUp(){
        //FirebaseApp.initializeApp(InstrumentationRegistry.getInstrumentation().targetContext)
        studentList = com.example.reachedapp.data.StudentList()
        students = studentList.initializeStudentList()
    }

    //test that uses the data initialized in the StudentList class and inserts in
    //in the Firebase DB
    @Test
    fun addNewUser(){
        val taskMap: MutableMap<String, Any> = HashMap()

        for(student in students){
            taskMap[student.studentId] = student
        }
        try{
            val task = dbRef.child("Student").setValue(taskMap)
            Tasks.await(task)
        } catch (e: CancellationException) {
           Log.d("TAG", "Could not push data to DB. Error: $e")
        }
    }
}