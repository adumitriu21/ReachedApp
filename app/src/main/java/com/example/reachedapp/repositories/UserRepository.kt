package com.example.reachedapp.repositories

import com.example.reachedapp.models.Admin
import com.example.reachedapp.models.Parent
import com.example.reachedapp.models.Teacher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import com.google.firebase.database.FirebaseDatabase


class UserRepository(private val database: FirebaseDatabase) {

    private val usersRef = database.reference

    suspend fun getUserFromDatabase(

        email: String

    ): Any? = withContext(Dispatchers.IO) {
        var user: Any? = null
        try {
            val teacherSnapshot = usersRef.child("Teacher").orderByChild("email").equalTo(email).get().await()
            for (snapshot in teacherSnapshot.children) {
                val teacher = snapshot.getValue(Teacher::class.java)
                if (teacher != null) {
                    user = teacher
                    break
                }
            }

            if (user == null) {
                val parentSnapshot = usersRef.child("Parent").orderByChild("email").equalTo(email).get().await()
                for (snapshot in parentSnapshot.children) {
                    val parent = snapshot.getValue(Parent::class.java)
                    if (parent != null) {
                        user = parent
                        break
                    }
                }
            }

            if (user == null) {
                val adminSnapshot = usersRef.child("Admin").orderByChild("email").equalTo(email).get().await()
                for (snapshot in adminSnapshot.children) {
                    val admin = snapshot.getValue(Admin::class.java)
                    if (admin != null) {
                        user = admin
                        break
                    }
                }
            }
        } catch (e: Exception) {
            // Handle exception
        }
        user
    }
}