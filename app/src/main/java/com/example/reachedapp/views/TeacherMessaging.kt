package com.example.reachedapp.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reachedapp.Models.Message
import com.example.reachedapp.Models.Parent
import com.example.reachedapp.Models.Student
import com.example.reachedapp.Models.Teacher
import com.example.reachedapp.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*

class TeacherMessaging : Fragment() {
    private lateinit var parent: Parent
    private lateinit var teacher: Teacher
    private val database = FirebaseDatabase.getInstance()
    val convoRef = database.getReference("Messaging")
    private lateinit var recyclerView: RecyclerView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var name: androidx.appcompat.widget.Toolbar
    private var messageList: MutableList<Message> = ArrayList<Message>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_teacher_messaging, container, false)

        parent = arguments?.getParcelable<Parent>("parent") ?: return view
        teacher = arguments?.getParcelable<Teacher>("teacher") ?: return view

        val convoId = teacher.userId + parent.userId
        val reference = convoRef.child(convoId)
        val sendBtn = view.findViewById<Button>(R.id.button_gchat_send)
        val messageInput = view.findViewById<EditText>(R.id.edit_gchat_message)


        messageAdapter = MessageAdapter()
        recyclerView = view.findViewById(R.id.recycler_gchat)
        recyclerView.adapter = messageAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        name = view.findViewById(R.id.toolbar_gchannel)
        name.setTitle(parent.name)
        name.setBackgroundColor(resources.getColor(R.color.gray))
        name.setTitleTextColor(resources.getColor(R.color.white))
        reference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messageList.clear()
                for (dsp in snapshot.children) {
                    val m = dsp.getValue(Message::class.java)
                    if (m != null) {
                        messageList.add(m)
                        println(m)
                    }
                }

                messageAdapter.setData(messageList)
                messageAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        sendBtn.setOnClickListener{
            val messageText = messageInput.text.toString()
            println(messageText)
            if (messageText.isNotEmpty()) {
                val date = Calendar.getInstance().time
                val dateFormat = SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.getDefault())
                val formattedDate = dateFormat.format(date)
                val message = Message(teacher.name, messageText, formattedDate)
                val reference = convoRef.child(convoId)
                val messageId = reference.push().key
                reference.child(messageId!!).setValue(message)
                        .addOnSuccessListener {
                            Toast.makeText(requireContext(), "Message sent!", Toast.LENGTH_SHORT).show()
                            messageInput.setText("")
                        }
                        .addOnFailureListener {
                            Toast.makeText(requireContext(), "Failed to send message!", Toast.LENGTH_SHORT).show()
                        }

            } else {
                println("here")
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}
