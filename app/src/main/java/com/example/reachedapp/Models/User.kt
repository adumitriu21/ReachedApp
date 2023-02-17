package com.example.reachedapp.Models

open class User(val name: String, val email: String, val password: String, val role: String)

class Parent(name: String, email: String, password: String, val studentNames: Array<String>) : User(name, email, password, "Parent")

class Teacher(name: String, email: String, password: String, val homeroomNumber: String) : User(name, email, password, "Teacher")

class Admin(name: String, email: String, password: String) : User(name, email, password, "Admin")