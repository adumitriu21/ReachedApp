package com.example.reachedapp.data

import com.example.reachedapp.models.Student

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
            ),
            Student("S011", "Alice Smith", "C004", "P008"),
            Student("S012", "Bob Johnson", "C004", "P009"),
            Student("S013", "Charlie Brown", "C004", "P010"),
            Student("S014", "David Brown", "C004", "P010"),
            Student("S015", "Emily Brown", "C004", "P010"),
            Student("S016", "Frank Wang", "C004", "011"),
            Student("S017", "Grace Kim", "C004", "P012"),
            Student("S018", "Henry Liu", "C004", "P013"),
            Student("S019", "Isabel Liu", "C004", "P013"),
            Student("S020", "Jackie Nguyen", "C004", "P014")

        )

        return ArrayList(students)
    }
}