package com.example.reachedapp.controllers

import com.example.reachedapp.models.Message
import com.example.reachedapp.models.Parent
import com.example.reachedapp.models.Teacher
import com.example.reachedapp.interfaces.MessageListener
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
        // Create a conversation reference based on the user IDs of the teacher and parent
        val convoId = teacher.userId + parent.userId

        // Get the reference to the conversation in the Firebase database
        val reference = convoRef.child(convoId)

        // Add a ValueEventListener to listen for changes in the conversation data
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Clear the message list before adding new messages
                messageList.clear()

                // Iterate over the children of the snapshot to retrieve the messages
                for (dsp in snapshot.children) {
                    // Deserialize the message object from the DataSnapshot
                    val m = dsp.getValue(Message::class.java)
                    if (m != null) {
                        messageList.add(m)
                    }
                }
                // Notify the message listener that the messages have been loaded
                messageListener.onMessagesLoaded(messageList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    // this function sends a message
    fun sendMessage(messageText: String) {
        if (messageText.isNotEmpty()) {
            // Get the current date and format it
            val date = Calendar.getInstance().time
            val dateFormat = SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.getDefault())
            val formattedDate = dateFormat.format(date)

            // Create a Message object based on the sender type (parent or teacher)
            val message = if (fragment is ParentMessaging) {
                Message(parent.name, parent.userId, messageText, formattedDate)
            } else {
                Message(teacher.name, teacher.userId, messageText, formattedDate)
            }

            // Create a conversation reference based on the user IDs of the teacher and parent
            val convoId = teacher.userId + parent.userId

            // Get the reference to the conversation in the Firebase database
            val reference = convoRef.child(convoId)

            // Generate a unique message ID using the push() method and get the key
            val messageId = reference.push().key

            // Set the message value in the conversation reference
            reference.child(messageId!!).setValue(message)
                    .addOnSuccessListener {
                        // Notify the message listener that the message has been sent successfully
                        messageListener.onMessageSent()
                    }
                    .addOnFailureListener {
                        // Notify the message listener that sending the message has failed
                        messageListener.onMessageFailed()
                    }
        }
    }
}