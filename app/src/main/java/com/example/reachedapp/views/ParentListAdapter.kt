package com.example.reachedapp.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.reachedapp.Models.Parent
import com.example.reachedapp.Models.Teacher
import com.example.reachedapp.R

class ParentListAdapter(
        private val isTeacherView: Boolean = false,
        private val listener: OnItemClickListener,
        private val teacher: Teacher
) : RecyclerView.Adapter<ParentListAdapter.ViewHolder>() {

    private var parentList = emptyList<Parent>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onParentItemClick(parentList[position], teacher)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_parents_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentParent = parentList[position]
        holder.itemView.findViewById<TextView>(R.id.parentNameTextView).text = currentParent.name
    }

    override fun getItemCount(): Int = parentList.size

    fun setData(parent: List<Parent>) {
        this.parentList = parent
        notifyDataSetChanged()
    }
}
