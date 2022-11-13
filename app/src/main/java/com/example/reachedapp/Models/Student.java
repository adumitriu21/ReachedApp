package com.example.reachedapp.Models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Student {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int StudentHomeroom;
    private String StudentName;
    private String StudentParent1;
    private String StudentParent1Phone;
    private String StudentParent1Email;
    private String StudentParent2;
    private String StudentParent2Phone;
    private String StudentParent2Email;

    public Student() {}

    public Student(int studentHomeroom,
                   String studentName,
                   String studentParent1,
                   String studentParent1Phone,
                   String studentParent1Email){
        this.StudentHomeroom = studentHomeroom;
        this.StudentName = studentName;
        this.StudentParent1 = studentParent1;
        this.StudentParent1Phone = studentParent1Phone;
        this.StudentParent1Email = studentParent1Email;
    }

    @Override
    public String toString() {
        return "Homeroom: " + this.getStudentHomeroom() +
                "\nName: " + this.getStudentName() +
                "\nParent: " + this.getStudentParent1();
    }

    public int getStudentHomeroom() {
        return StudentHomeroom;
    }

    public void setStudentHomeroom(int studentHomeroom) {
        StudentHomeroom = studentHomeroom;
    }

    public String getStudentName() {
        return StudentName;
    }

    public void setStudentName(String studentName) {
        StudentName = studentName;
    }

    public String getStudentParent1() {
        return StudentParent1;
    }

    public void setStudentParent1(String studentParent1) {
        StudentParent1 = studentParent1;
    }

    public String getStudentParent1Phone() {
        return StudentParent1Phone;
    }

    public void setStudentParent1Phone(String studentParent1Phone) {
        StudentParent1Phone = studentParent1Phone;
    }

    public String getStudentParent1Email() {
        return StudentParent1Email;
    }

    public void setStudentParent1Email(String studentParent1Email) {
        StudentParent1Email = studentParent1Email;
    }

    public String getStudentParent2() {
        return StudentParent2;
    }

    public void setStudentParent2(String studentParent2) {
        StudentParent2 = studentParent2;
    }

    public String getStudentParent2Phone() {
        return StudentParent2Phone;
    }

    public void setStudentParent2Phone(String studentParent2Phone) {
        StudentParent2Phone = studentParent2Phone;
    }

    public String getStudentParent2Email() {
        return StudentParent2Email;
    }

    public void setStudentParent2Email(String studentParent2Email) {
        StudentParent2Email = studentParent2Email;
    }
}
