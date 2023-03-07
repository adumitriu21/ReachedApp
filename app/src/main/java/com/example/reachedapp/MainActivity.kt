package com.example.reachedapp


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //val database = FirebaseDatabase.getInstance()
        //val ref = database.getReference("Student")


        supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment

/*
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val studentList: MutableList<Student> = ArrayList<Student>()
                for (dsp in dataSnapshot.children) {
                    val s = dsp.getValue(Student::class.java)
                    if (s != null) {
                        studentList.add(s)
                    }
                    println(s.toString())
                }
            }


            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })
*/
    }


}

