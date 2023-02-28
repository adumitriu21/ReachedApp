package com.example.reachedapp.Models

open class User(
    val userId: String,
    val name: String,
    val email: String,
    val password: String,
    val userRole: UserRole
    ){
    constructor() : this("", "", "", "", UserRole.DEFAULT)
}

class Parent(
    userId: String,
    name: String,
    email: String,
    password: String,
    val children: List<String>
    ) : User(userId, name, email, password, UserRole.PARENT){
    constructor() : this("", "", "", "", emptyList())
}


class Teacher(
    userId: String,
    name: String,
    email: String,
    password: String,
    val homeroomNumber: String
    ) : User(userId, name, email, password, UserRole.TEACHER){
    constructor() : this("", "", "", "", "")
}

class Admin(
    userId: String,
    name: String,
    email: String,
    password: String
    ) : User(userId, name, email, password, UserRole.ADMIN){
    constructor() : this("", "", "", "")
}

enum class UserRole {
    DEFAULT,
    PARENT,
    TEACHER,
    ADMIN
}



