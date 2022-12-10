package com.example.reachedapp.data

import androidx.collection.arrayMapOf

class FakeAttendance {
    private val att1 = HashMap<String,Boolean>()
    private val att2 = HashMap<String, Boolean>()
    private val fakeAttendance = HashMap<String,HashMap<String,Boolean>>()

    fun initializeFakeAttendance(): HashMap<String,HashMap<String,Boolean>>{

        //create two different attendance states
        att1["IsSubmitted"] = false
        att2["IsSubmitted"] = true

        //create a few attendance dates and assign attendance states to them
        fakeAttendance["04 December 2022"] = att1
        fakeAttendance["05 December 2022"] = att2
        fakeAttendance["06 December 2022"] = att1
        fakeAttendance["07 December 2022"] = att2
        fakeAttendance["08 December 2022"] = att1
        fakeAttendance["09 December 2022"] = att2
        fakeAttendance["10 December 2022"] = att2

        return  fakeAttendance
    }

}