package com.example.reachedapp.models

import android.os.Parcel
import android.os.Parcelable

open class User(
    val userId: String,
    val name: String,
    val email: String,
    val password: String,
    val userRole: UserRole,
    var deviceToken: String? = null
    ) : Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        UserRole.valueOf(parcel.readString()!!)
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
        userId: String = "",
        name: String = "",
        email: String = "",
        password: String = "",
    var children: List<String> = emptyList(),
        deviceToken: String? = null // Add this line
    ) : User(userId, name, email, password, UserRole.Parent, deviceToken){

    constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.createStringArrayList()!!
    )

    constructor() : this("", "", "", "", emptyList(), null)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
        parcel.writeString(userId)
    }

    companion object {
        @JvmField val CREATOR = object : Parcelable.Creator<Parent> {
            override fun createFromParcel(source: Parcel): Parent {
                return Parent(
                        source.readString()!!,
                        source.readString()!!,
                        source.readString()!!,
                        source.readString()!!,
                        source.createStringArrayList()!!
                )
            }

            override fun newArray(size: Int): Array<Parent?> {
                return arrayOfNulls(size)
            }
        }
    }
}


class Teacher(
        userId: String = "",
        name: String = "",
        email: String = "",
        password: String = "",
        var classId: String = "",
        deviceToken: String? = null // Add this line
)
    : User(userId, name, email, password, UserRole.Teacher, deviceToken), Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!

    )
    constructor() : this("", "", "", "", "", null)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
        parcel.writeString(classId)
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
    password: String,
    deviceToken: String? = null // Add this line
    ) : User(userId, name, email, password, UserRole.Admin, deviceToken){
    constructor() : this("", "", "", "", null)
}

enum class UserRole {
    Default,
    Parent,
    Teacher,
    Admin
}



