package com.wlm.roompagingdemo

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wlm.roompagingdemo.db.Student

class StudentViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(
        R.layout.item_student,
        parent,
        false
    )
) {
    private val tvName = itemView.findViewById<TextView>(R.id.tvName)
    var student: Student? = null

    fun bind(student: Student?) {
        this.student = student
        tvName.text = student?.name
    }
}