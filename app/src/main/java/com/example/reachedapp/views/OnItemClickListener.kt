package com.example.reachedapp.views

import com.example.reachedapp.Models.Parent
import com.example.reachedapp.Models.Teacher

interface OnItemClickListener {
    fun onParentItemClick(parent: Parent, teacher: Teacher)

    fun onTeacherItemClick(teacher: Teacher, parent: Parent)
}