package com.example.reachedapp.Models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Attendance {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int AttendanceId;
    private Boolean Present;
    private Date Attendance_date;
    private int Student_Id;
    private int Homeroom_Id;

    public int getAttendanceId() {
        return AttendanceId;
    }

    public void setAttendanceId(int attendanceId) {
        AttendanceId = attendanceId;
    }

    public Boolean getPresent() {
        return Present;
    }

    public void setPresent(Boolean present) {
        Present = present;
    }

    public Date getAttendance_date() {
        return Attendance_date;
    }

    public void setAttendance_date(Date attendance_date) {
        Attendance_date = attendance_date;
    }

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
