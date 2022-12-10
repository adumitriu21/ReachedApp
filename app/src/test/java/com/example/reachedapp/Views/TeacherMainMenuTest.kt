package com.example.reachedapp.Views

import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.reachedapp.R
import com.example.reachedapp.data.FakeAttendance
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.reflect.typeOf

class TeacherMainMenuTest{

    private lateinit var fakeAttendance: FakeAttendance
    private lateinit var attDates: HashMap<String, kotlin.collections.HashMap<String, Boolean>>
    private lateinit var currentDate: String

    @Before
    fun setUp(){
        fakeAttendance = FakeAttendance()
        attDates = fakeAttendance.initializeFakeAttendance()
        val formatter = SimpleDateFormat("dd MMMM yyyy")
        val attendanceDate = Date()
        currentDate = formatter.format(attendanceDate)
    }

    @Test
    fun `test if attendance is submitted`() {
        val datesList = arrayListOf<String>()

        for (date in attDates) {
            date.key?.let { it1 -> datesList.add(it1) }
        }

        if (datesList.contains(currentDate)) {
            val checkSubmitted = attDates.getValue(currentDate)
            val trueOrFalse = checkSubmitted.getValue("IsSubmitted")

            assertTrue(trueOrFalse is Boolean)

        }
    }
}