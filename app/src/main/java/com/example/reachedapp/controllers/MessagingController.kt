package com.example.reachedapp.controllers

import com.example.reachedapp.models.Message
import com.example.reachedapp.models.Parent
import com.example.reachedapp.models.Teacher
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import androidx.fragment.app.Fragment
import com.example.reachedapp.views.ParentMessaging
import java.util.*

class MessagingController(
        private val parent: Parent,
        private val teacher: Teacher,
        private val messageListener: MessageListener,
        private val fragment: Fragment
) {
    private val database = FirebaseDatabase.getInstance()
    private val convoRef = database.getReference("Messaging")

    private var messageList: MutableList<Message> = ArrayList<Message>()

    init {
        val convoId = teacher.userId + parent.userId
        val reference = convoRef.child(convoId)

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messageList.clear()
                for (dsp in snapshot.children) {
                    val m = dsp.getValue(Message::class.java)
                    if (m != null) {
                        messageList.add(m)
                    }
                }

                messageListener.onMessagesLoaded(messageList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    fun sendMessage(messageText: String) {
        if (messageText.isNotEmpty()) {
            val date = Calendar.getInstance().time
            val dateFormat = SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.getDefault())
            val formattedDate = dateFormat.format(date)
            val message = if (fragment is ParentMessaging) {
                Message(parent.name, parent.userId, messageText, formattedDate)
            } else {
                Message(teacher.name, teacher.userId, messageText, formattedDate)
            }
            val convoId = teacher.userId + parent.userId
            val reference = convoRef.child(convoId)
            val messageId = reference.push().key

            reference.child(messageId!!).setValue(message)
                    .addOnSuccessListener {
                        messageListener.onMessageSent()
                    }
                    .addOnFailureListener {
                        messageListener.onMessageFailed()
                    }
        }
    }

    interface MessageListener {
        fun onMessagesLoaded(messages: List<Message>)
        fun onMessageSent()
        fun onMessageFailed()
    }
}