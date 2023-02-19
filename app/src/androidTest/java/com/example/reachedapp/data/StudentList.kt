package com.example.reachedapp.data

import com.example.reachedapp.Models.Student

/*
* Class where all the hard coded student data resides
* this was done to test out integration with the Firebase
* Realtime DB and to ensure that the objects inserted in
* the DB had the desired structure
*
* Additionally, this class acts as a backup in case the DB
* date gets deleted, which can be easily done by mistake in
* Firebase
*
* */
class StudentList {

    fun initializeStudentList(): ArrayList<Student> {

        val students = listOf(
            Student(
                "S001",
                "Alice Cooper",
                "C001",
                "P001"
            ),
            Student(
                "S002",
                "Bob Dylan",
                "C002",
                "P002"
            ),
            Student(
                "S003",
                "Charlie Dylan",
                "C001",
                "P002"
            ),
            Student(
                "S004",
                "David Bowie",
                "C003",
                "P003"
            ),
            Student(
                "S005",
                "Emily Dickinson",
                "C002",
                "P004"
            ),
            Student(
                "S006",
                "Frank Dickinson",
                "C003",
                "P004"
            ),
            Student(
                "S007",
                "Grace Kelly",
                "C001",
                "P005"
            ),
            Student(
                "S008",
                "Henry Ford",
                "C002",
                "P006"
            ),
            Student(
                "S009",
                "Isaac Newton",
                "C003",
                "P007"
            ),
            Student(
                "S010",
                "Jessica Newton",
                "C001",
                "P007"
            )
        )

        return ArrayList(students)
    }
}