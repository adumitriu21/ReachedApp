package com.example.reachedapp.views

import android.util.Log
import com.example.reachedapp.models.Teacher
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import org.junit.Before
import org.junit.Test
import kotlin.coroutines.cancellation.CancellationException

class AddTeachersToDbTest {

    private lateinit var auth: FirebaseAuth
    private val database = FirebaseDatabase.getInstance()
    private val dbRef= database.reference
    private lateinit var teacherList: com.example.reachedapp.data.TeacherList
    private lateinit var teachers: ArrayList<Teacher>

    //function that runs before any tests begin
    @Before
    fun setUp(){
        auth = FirebaseAuth.getInstance()
        //FirebaseApp.initializeApp(InstrumentationRegistry.getInstrumentation().targetContext)
        teacherList = com.example.reachedapp.data.TeacherList()
        teachers = teacherList.initializeTeacherList()
    }

    //test that uses the data initialized in the StudentList class and inserts in
    //in the Firebase DB
    @Test
    fun addNewTeachers(){
        val taskMap: MutableMap<String, Any> = HashMap()

        for(teacher in teachers){
            auth.createUserWithEmailAndPassword(teacher.email, teacher.password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("Test", "User created successfully")
                    } else {
                        Log.e("Test", "Error creating user", task.exception)
                    }
                }
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