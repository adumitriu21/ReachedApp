package com.example.reachedapp.Models

open class User(
    val userId: String,
    val name: String,
    val email: String,
    val password: String,
    val userRole: UserRole
    )

class Parent(
    userId: String,
    name: String,
    email: String,
    password: String,
    val children: List<String>
    ) : User(userId, name, email, password, UserRole.PARENT)

class Teacher(
    userId: String,
    name: String,
    email: String,
    password: String,
    val homeroomNumber: String
    ) : User(userId, name, email, password, UserRole.TEACHER)

class Admin(
    userId: String,
    name: String,
    email: String,
    password: String
    ) : User(userId, name, email, password, UserRole.ADMIN)

enum class UserRole {
    PARENT,
    TEACHER,
    ADMIN
}



