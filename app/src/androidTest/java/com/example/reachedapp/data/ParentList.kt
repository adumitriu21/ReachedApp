package com.example.reachedapp.data

import com.example.reachedapp.models.Parent

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
            ),
            Parent("P008", "John Smith", "john.smith@example.com", "password", listOf("S011")),
            Parent("P009", "Karen Johnson", "karen.johnson@example.com", "password", listOf("S012")),
            Parent("P010", "Mike Brown", "mike.brown@example.com", "password", listOf("S013","S014","S015")),
            Parent("P011", "Lisa Wang", "lisa.lee@example.com", "password", listOf("S016")),
            Parent("P012", "Benjamin Kim", "benjamin.k@example.com", "password", listOf("S017")),
            Parent("P013", "Rachel Liu", "rachel.liu@example.com", "password", listOf("S018","S19")),
            Parent("P014", "Steven Nguyen", "steven.liu@example.com", "password", listOf("S020"))

        )

        return ArrayList(parents)

    }
}