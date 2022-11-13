package com.example.reachedapp.Models;

import androidx.room.Entity;

@Entity(primaryKeys = {"Student_Id", "Homeroom_Id"})
public class Enrollment {
    private int Student_Id;
    private int Homeroom_Id;

    public int getStudent_Id() {
        return Student_Id;
    }

    public void setStudent_Id(int student_Id) {
        Student_Id = student_Id;
    }

    public int getHomeroom_Id() {
        return Homeroom_Id;
    }

    public void setHomeroom_Id(int homeroom_Id) {
        Homeroom_Id = homeroom_Id;
    }


}
