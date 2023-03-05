package com.example.reachedapp.Models

class Student (
    val studentId: String,
    val name: String,
    val classId: String,
    val parentId: String // Reference to the Parent entity by ID
    ){
    constructor() : this("", "", "", "")
}