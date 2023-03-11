package com.example.reachedapp.Views

import android.util.Log
import com.example.reachedapp.Models.Admin
import com.example.reachedapp.Models.Teacher
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import org.junit.Before
import org.junit.Test
import kotlin.coroutines.cancellation.CancellationException

class AddAdminsToDbTest {

    private lateinit var auth: FirebaseAuth
    private val database = FirebaseDatabase.getInstance()
    private val dbRef= database.reference
    private lateinit var adminList: com.example.reachedapp.data.AdminList
    private lateinit var admins: ArrayList<Admin>

    //function that runs before any tests begin
    @Before
    fun setUp(){
        auth = FirebaseAuth.getInstance()
        //FirebaseApp.initializeApp(InstrumentationRegistry.getInstrumentation().targetContext)
        adminList = com.example.reachedapp.data.AdminList()
        admins = adminList.initializeAdminList()
    }

    //test that uses the data initialized in the StudentList class and inserts in
    //in the Firebase DB
    @Test
    fun addNewAdmins(){
        val taskMap: MutableMap<String, Any> = HashMap()

        for(admin in admins){
            auth.createUserWithEmailAndPassword(admin.email, admin.password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("Test", "User created successfully")
                    } else {
                        Log.e("Test", "Error creating user", task.exception)
                    }
                }
            taskMap[admin.userId] = admin
        }
        try{
            val task = dbRef.child("Admin").setValue(taskMap)
            Tasks.await(task)
        } catch (e: CancellationException) {
            Log.d("TAG", "Could not push data to DB. Error: $e")
        }
    }
}