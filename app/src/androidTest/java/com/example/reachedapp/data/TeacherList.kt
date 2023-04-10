package com.example.reachedapp.data

import com.example.reachedapp.models.Teacher

class TeacherList {
    fun initializeTeacherList(): ArrayList<Teacher> {

        val teachers = listOf(
            Teacher(
                "T004",
                "Adi Das",
                "adumitriu21@gmail.com",
                "password",
                "C004"
            ),
            Teacher(
                "T001",
                "John Doe",
                "johndoe@example.com",
                "password",
                "C001"
            ),
            Teacher(
                "T002",
                "Jane Smith",
                "janesmith@example.com",
                "password",
                "C002"
            ),
            Teacher(
                "T003",
                "Jack Black",
                "jackblack@example.com",
                "password",
                "C003"
            )
        )

        return ArrayList(teachers)
    }

}