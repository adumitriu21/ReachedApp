package com.example.reachedapp.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.reachedapp.models.Message
import com.example.reachedapp.R

class MessageAdapter () :
        RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    private var messageList = emptyList<Message>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_messages, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = messageList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messageList[position]
        holder.bind(message)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageTextView = itemView.findViewById<TextView>(R.id.messageText)
        private val timeTextView = itemView.findViewById<TextView>(R.id.messageTimestamp)
        private val senderTextView = itemView.findViewById<TextView>(R.id.messageSender)

        fun bind(message: Message) {
            messageTextView.text = message.message
            timeTextView.text = message.timestamp
            senderTextView.text = message.sender
        }
    }

    fun setData(message: List<Message>) {
        this.messageList = message
        notifyDataSetChanged()
    }
}