package com.example.reachedapp.interfaces
import com.example.reachedapp.models.Parent
import com.example.reachedapp.models.Teacher

interface OnContactClickListener {

    fun onParentItemClick(parent: Parent, teacher: Teacher)

    fun onTeacherItemClick(teacher: Teacher, parent: Parent)

}