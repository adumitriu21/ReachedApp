package com.example.reachedapp.views

import com.example.reachedapp.models.Parent
import com.example.reachedapp.models.Teacher

interface OnItemClickListener {
    fun onParentItemClick(parent: Parent, teacher: Teacher)

    fun onTeacherItemClick(teacher: Teacher, parent: Parent)
}