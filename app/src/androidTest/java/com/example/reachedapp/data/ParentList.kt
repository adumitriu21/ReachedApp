package com.example.reachedapp.data

import com.example.reachedapp.Models.Parent

class ParentList {

    fun initializeParentList(): ArrayList<Parent> {
        val parents = listOf(
            Parent(
                "P001",
                "Martha Cooper",
                "mcooper@example.com",
                "password123",
                listOf("S001")
            ),
            Parent(
                "P002",
                "Jonny Dylan",
                "jdylan@example.com",
                "password123",
                listOf("S002", "S003")
            ),
            Parent(
                "P003",
                "Ana Bowie",
                "anabowie@example.com",
                "password123",
                listOf("S004")
            ),
            Parent(
                "P004",
                "Alexa Dickinson",
                "theDickinsons@example.com",
                "password123",
                listOf("S005", "S006")
            ),
            Parent(
                "P005",
                "Norm Kelly",
                "gkellyt@example.com",
                "password123",
                listOf("S007")
            ),
            Parent(
                "P006",
                "TJ Ford",
                "tomjohnford@example.com",
                "password123",
                listOf("S008")
            ),
            Parent(
                "P007",
                "Gustav Newton",
                "gNew@example.com",
                "password123",
                listOf("S009", "S010")
            )
        )

        return ArrayList(parents)

    }
}