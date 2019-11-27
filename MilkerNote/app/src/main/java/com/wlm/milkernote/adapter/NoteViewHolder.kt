package com.wlm.milkernote.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wlm.milkernote.CreateActivity
import com.wlm.milkernote.R
import com.wlm.milkernote.db.Note
import com.wlm.milkernote.ext.startKtxActivity
import com.wlm.milkernote.utils.DateUtils

class NoteViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
) {

    private val context = parent.context
    private val tvTitle = itemView.findViewById<TextView>(R.id.tv_note_title)
    private val tvTime = itemView.findViewById<TextView>(R.id.tv_note_time)
    private val tvContent = itemView.findViewById<TextView>(R.id.tv_note_content)
    private var note: Note? = null
    fun bind(note: Note?) {
        this.note = note
        note?.run {
            tvTitle.text = title
            tvContent.text = content
            tvTime.text = DateUtils.format(updateTime)
        }

        itemView.setOnClickListener {
            note?.let {
                context.startKtxActivity<CreateActivity>(value = CreateActivity.KEY_NOTE to it.id)
            }
        }
    }

}