package com.example.reachedapp.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reachedapp.models.Message
import com.example.reachedapp.models.Parent
import com.example.reachedapp.models.Teacher
import com.example.reachedapp.R
import com.example.reachedapp.controllers.MessagingController
import com.example.reachedapp.views.adapters.MessageAdapter
import com.example.reachedapp.interfaces.MessageListener

class TeacherMessaging : Fragment(), MessageListener {
    private lateinit var parent: Parent
    private lateinit var teacher: Teacher
    private lateinit var recyclerView: RecyclerView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var name: androidx.appcompat.widget.Toolbar

    private lateinit var controller: MessagingController
    private lateinit var messageInput: EditText

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_teacher_messaging, container, false)

        // Retrieve parent and teacher objects from arguments
        parent = arguments?.getParcelable<Parent>("parent") ?: return view
        teacher = arguments?.getParcelable<Teacher>("teacher") ?: return view

        // Initialize the message adapter and configure the recycler view
        messageAdapter = MessageAdapter()
        recyclerView = view.findViewById(R.id.recycler_gchat)
        recyclerView.adapter = messageAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Configure the toolbar with the parent's name
        name = view.findViewById(R.id.toolbar_gchannel)
        name.setTitle(parent.name)
        name.setBackgroundColor(resources.getColor(R.color.gray))
        name.setTitleTextColor(resources.getColor(R.color.white))

        val sendBtn = view.findViewById<Button>(R.id.button_gchat_send)
        messageInput = view.findViewById<EditText>(R.id.edit_gchat_message)
        sendBtn.setOnClickListener {
            val messageText = messageInput.text.toString()
            // Call the sendMessage method of the messaging controller
            controller.sendMessage(messageText)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Create an instance of the messaging controller
        controller = MessagingController(parent, teacher, this, this)
    }

    // Callback method called when messages are loaded
    override fun onMessagesLoaded(messages: List<Message>) {
        messageAdapter.setData(messages)
        messageAdapter.notifyDataSetChanged()
    }

    // Callback method called when a message is successfully sent
    override fun onMessageSent() {
        Toast.makeText(requireContext(), "Message sent!", Toast.LENGTH_SHORT).show()
        messageInput.setText("")
    }

    // Callback method called when sending a message fails
    override fun onMessageFailed() {
        Toast.makeText(requireContext(), "Failed to send message!", Toast.LENGTH_SHORT).show()
    }
}