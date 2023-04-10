package com.example.reachedapp.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.reachedapp.models.Parent
import com.example.reachedapp.models.Teacher
import com.example.reachedapp.R
import com.example.reachedapp.interfaces.OnContactClickListener

class TeacherListAdapter(
    private val isTeacherView: Boolean = false,
    private val listener: OnContactClickListener,
    private val parent: Parent
) : RecyclerView.Adapter<TeacherListAdapter.ViewHolder>() {

    private var teacherList = emptyList<Teacher>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onTeacherItemClick(teacherList[position], parent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_teachers_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentTeacher = teacherList[position]
        holder.itemView.findViewById<TextView>(R.id.teacherNameTextView).text = currentTeacher.name
    }

    override fun getItemCount(): Int = teacherList.size

    fun setData(teacher: List<Teacher>) {
        this.teacherList = teacher
        notifyDataSetChanged()
    }
}
