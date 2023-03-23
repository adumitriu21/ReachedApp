package com.example.reachedapp.data

import com.example.reachedapp.Models.Admin
import com.example.reachedapp.Models.Parent

class AdminList {

    fun initializeAdminList(): ArrayList<Admin> {
        val admins = listOf(
            Admin("A001", "Armin van Buuren", "arminvanbuuren@example.com", "password1"),
            Admin("A002", "Markus Schulz", "aboveandbeyond@example.com", "password2"),
            Admin("A003", "Ferry Corsten", "ferrycorsten@example.com", "password3")
        )

        return ArrayList(admins)

    }
}