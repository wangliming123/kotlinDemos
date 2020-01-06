package com.wlm.mvvm_wanandroid.adapter

import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.wlm.mvvm_wanandroid.R
import com.wlm.mvvm_wanandroid.common.Article

class DefaultArticleAdapter : PagedListAdapter<Article, ArticleViewHolder>(diffCallback){
    companion object {

        private val diffCallback = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean =
                oldItem == newItem

        }
    }

    private var listener : OnItemChildClickListener<Article>? = null

    fun setOnItemChildClickListener(listener: OnItemChildClickListener<Article>) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder =
        ArticleViewHolder(parent)

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.findViewById<ImageView>(R.id.iv_collect).setOnClickListener {
            getItem(position)?.run {
                listener?.onClick(it, this)
            }
        }
    }


}