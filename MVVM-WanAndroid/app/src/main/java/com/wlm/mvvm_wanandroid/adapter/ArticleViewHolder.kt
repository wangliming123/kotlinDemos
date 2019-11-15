package com.wlm.mvvm_wanandroid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wlm.mvvm_wanandroid.R
import com.wlm.mvvm_wanandroid.bean.Article
import com.wlm.mvvm_wanandroid.startKtxActivity
import com.wlm.mvvm_wanandroid.ui.activity.BrowserActivity

class ArticleViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false)
) {
    private var article: Article? = null
    private val context = parent.context

    private val tvAuthor = itemView.findViewById<TextView>(R.id.tv_author)
    private val tvDate = itemView.findViewById<TextView>(R.id.tv_date)
    private val tvTitle = itemView.findViewById<TextView>(R.id.tv_title)
    private val tvDesc = itemView.findViewById<TextView>(R.id.tv_desc)
    private val tvChapter = itemView.findViewById<TextView>(R.id.tv_chapter)

    fun bind(article: Article?) {
        this.article = article
        article?.run {
            tvAuthor.text = author
            tvDate.text = niceDate
            tvTitle.text = title
            if (desc.isNullOrBlank()) tvDesc.visibility = View.GONE
            else tvDesc.text = desc
            tvChapter.text = when {
                superChapterName.isNotBlank() and chapterName.isNotBlank() ->
                    "${superChapterName}/ ${chapterName}"
                superChapterName.isNotBlank() -> superChapterName
                chapterName.isNotBlank() -> chapterName
                else -> ""
            }
            itemView.setOnClickListener {
                context.startKtxActivity<BrowserActivity>(value = BrowserActivity.KEY_URL to link)
            }
        }
    }

}