package com.example.reachedapp.Models

import android.os.Parcel
import android.os.Parcelable

open class User(
    val userId: String,
    val name: String,
    val email: String,
    val password: String,
    val userRole: UserRole
    ) : Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readSerializable() as UserRole
    )
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userId)
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeString(password)
        parcel.writeSerializable(userRole)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField val CREATOR = object : Parcelable.Creator<User> {
            override fun createFromParcel(source: Parcel): User {
                return User(
                    source.readString()!!,
                    source.readString()!!,
                    source.readString()!!,
                    source.readString()!!,
                    UserRole.values()[source.readInt()]
                )
            }

            override fun newArray(size: Int): Array<User?> {
                return arrayOfNulls(size)
            }
        }
    }
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
        userId: String = "",
        name: String = "",
        email: String = "",
        password: String = "",
        private val homeroomNumber: String = "" )

    : User(userId, name, email, password, UserRole.TEACHER), Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!
    )
    constructor() : this("", "", "", "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
        parcel.writeString(homeroomNumber)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField val CREATOR = object : Parcelable.Creator<Teacher> {
            override fun createFromParcel(source: Parcel): Teacher {
                return Teacher(
                    source.readString()!!,
                    source.readString()!!,
                    source.readString()!!,
                    source.readString()!!,
                    source.readString()!!
                )
            }

            override fun newArray(size: Int): Array<Teacher?> {
                return arrayOfNulls(size)
            }
        }
    }
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



