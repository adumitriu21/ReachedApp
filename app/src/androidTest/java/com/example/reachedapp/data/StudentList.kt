package com.example.reachedapp.data

import com.example.reachedapp.Models.Student

class StudentList {

    private val studentList = ArrayList<Student>()


    fun intializeStudentList(): ArrayList<Student> {

        var student = Student(108,"Gianny Montana",
            "Al Montana","brapbrap@gunshots.com",
            "416 187 1870")
        var student1 = Student(108,"Hannah Montana",
            "Al Montana","brapbrap@gunshots.com",
            "416 187 1870")
        var student2 = Student(108,"Luka Milic",
            "Mladina Milic","mlami@gmail.com.com",
            "416 123 3211")
        var student3 = Student(108,"Francois Bernard",
            "Pierre Bernard","bernp@yahoo.com",
            "647 229 2211")
        var student4 = Student(108,"Tyler Durk",
            "Mandy Durk","md@gmail.com",
            "416 994 2123")
        var student5 = Student(108,"Rita Faucci",
            "Tony Faucci","tonyF31@gmail.com.com",
            "647 998 9876")
        var student6 = Student(108,"Jose Calderon",
            "Maria Calderon","mariaC@yahoo.com",
            "416 432 1321")
        var student7 = Student(108,"Peter Maclean",
            "Janice Maclean","theMacs123@gmail.com",
            "416 165 3456")
        var student8 = Student(108,"Douglas Wilson",
            "Sarah Wilson","Swizzie22@aol.com",
            "416 327 1990")
        var student9 = Student(107,"Marco Carolla",
            "Deborah Carolla","caroll123@gmail.com",
            "416 543 3256")
        var student10 = Student(107,"Boris Breijcha",
            "Svetlana Breijcha","darkPlanet@gmail.com",
            "416 187 1870")
        var student11 = Student(107,"Reinner Zonnenveld",
            "Myira Zonnenveld","mzonnie@gmail.com.com",
            "416 187 1870")
        var student12 = Student(108,"Carlo Lio",
            "Maggie Leo","lioM556@gunshots.com",
            "416 177 1820")
        var student13 = Student(107,"Markus Schulz",
            "Alice Schulz","trancegod@gmail.com",
            "416 187 1870")
        var student14 = Student(107,"Ann Clue",
            "Martha Clue","anyClue33@aol.com",
            "416 187 1870")
        var student15 = Student(107,"Charlotte De Witte",
            "Kris De Witte","techno4life@gmail.com",
            "416 187 1870")

        studentList.add(student)
        studentList.add(student1)
        studentList.add(student2)
        studentList.add(student3)
        studentList.add(student4)
        studentList.add(student5)
        studentList.add(student6)
        studentList.add(student7)
        studentList.add(student8)
        studentList.add(student9)
        studentList.add(student10)
        studentList.add(student11)
        studentList.add(student12)
        studentList.add(student13)
        studentList.add(student14)
        studentList.add(student15)

        return studentList
    }
}