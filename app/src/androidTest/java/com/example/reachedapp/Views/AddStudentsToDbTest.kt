package com.example.reachedapp.Views

import com.example.reachedapp.Models.Student
import com.google.firebase.database.FirebaseDatabase
import org.junit.Before
import org.junit.Test

class AddStudentsToDbTest {

    private val database = FirebaseDatabase.getInstance()
    private val dbRef= database.reference
    private lateinit var studentList: com.example.reachedapp.data.StudentList
    private lateinit var students: ArrayList<Student>


    @Before
    fun setUp(){
        //FirebaseApp.initializeApp(InstrumentationRegistry.getInstrumentation().targetContext)
        studentList = com.example.reachedapp.data.StudentList()
        students = studentList.intializeStudentList()
    }

    @Test
    fun addNewUser(){
        val taskMap: MutableMap<String, Any> = HashMap()

        for((count, student) in students.withIndex()){
            taskMap["StudentNumber$count"] = student
        }
        dbRef.child("Student").setValue(taskMap)

            //dbRef.child("Student").push().setValue(students)
        val num = 0

    }
}