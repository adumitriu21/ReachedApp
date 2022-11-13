package com.example.reachedapp.Models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Homeroom {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int HomeroomId;
    private String HomeroomName;
    private String TeacherName;

    public int getId() {
        return HomeroomId;
    }

    public void setId(int homeroomId) {
        this.HomeroomId = homeroomId;
    }

    public String getHomeroomName() {
        return HomeroomName;
    }

    public void setHomeroomName(String homeroomName) {
        HomeroomName = homeroomName;
    }

    public String getTeacherName() {
        return TeacherName;
    }

    public void setTeacherName(String teacherName) {
        TeacherName = teacherName;
    }

}
