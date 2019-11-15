package com.wlm.roompagingdemo

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.wlm.roompagingdemo.db.Student

class StudentAdapter : PagedListAdapter<Student, StudentViewHolder>(diffCallback){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder =
        StudentViewHolder(parent)

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Student>() {
            override fun areItemsTheSame(oldItem: Student, newItem: Student): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Student, newItem: Student): Boolean =
                oldItem == newItem

        }
    }

}