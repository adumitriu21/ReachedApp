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

class TeacherMessaging : Fragment(), MessagingController.MessageListener {
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

        parent = arguments?.getParcelable<Parent>("parent") ?: return view
        teacher = arguments?.getParcelable<Teacher>("teacher") ?: return view

        messageAdapter = MessageAdapter()
        recyclerView = view.findViewById(R.id.recycler_gchat)
        recyclerView.adapter = messageAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        name = view.findViewById(R.id.toolbar_gchannel)
        name.setTitle(parent.name)
        name.setBackgroundColor(resources.getColor(R.color.gray))
        name.setTitleTextColor(resources.getColor(R.color.white))

        val sendBtn = view.findViewById<Button>(R.id.button_gchat_send)
        messageInput = view.findViewById<EditText>(R.id.edit_gchat_message)

        sendBtn.setOnClickListener {
            val messageText = messageInput.text.toString()
            controller.sendMessage(messageText)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize any views or components here

        controller = MessagingController(parent, teacher, this, this)
    }

    override fun onMessagesLoaded(messages: List<Message>) {
        messageAdapter.setData(messages)
        messageAdapter.notifyDataSetChanged()
    }

    override fun onMessageSent() {
        Toast.makeText(requireContext(), "Message sent!", Toast.LENGTH_SHORT).show()
        messageInput.setText("")
    }

    override fun onMessageFailed() {
        Toast.makeText(requireContext(), "Failed to send message!", Toast.LENGTH_SHORT).show()
    }
}