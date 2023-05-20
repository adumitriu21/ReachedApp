package com.example.reachedapp.interfaces

import com.example.reachedapp.models.Message

interface MessageListener {
    // Interface for handling message-related events
    fun onMessagesLoaded(messages: List<Message>)

    fun onMessageSent()

    fun onMessageFailed()
}