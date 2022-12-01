package com.example.reachedapp.Repositories;

import androidx.lifecycle.MutableLiveData;

import com.example.reachedapp.Models.Student;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class StudentRepository {
    MutableLiveData<List<Student>> studentListMutableData;
    FirebaseDatabase mFirebase;
    MutableLiveData<Student> studentMutableLiveData;

    public StudentRepository(){
        this.studentListMutableData = new MutableLiveData<>();
        mFirebase = FirebaseDatabase.getInstance();
        studentMutableLiveData = new MutableLiveData<>();
    }

}
