package com.wlm.mvvm_wanandroid.adapter

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wlm.mvvm_wanandroid.bean.Article

class HomeAdapter : PagedListAdapter<Article, RecyclerView.ViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_TYPE_HEADER -> BannerViewHolder(parent)
//            ITEM_TYPE_FOOTER -> FooterViewHolder(parent)
            else -> ArticleViewHolder(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ArticleViewHolder -> holder.bind(getItem(position))
//            is FooterViewHolder -> holder.bind()
            is BannerViewHolder -> holder.bindBanner(getItem(position)?.bannerList)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> ITEM_TYPE_HEADER
            itemCount - 1 -> ITEM_TYPE_FOOTER
            else -> super.getItemViewType(position)
        }
    }

//    override fun getItemCount(): Int {
//        return super.getItemCount() + 1
//    }

//    private fun getArticleItem(position: Int): Article? {
//        return getItem(position - 1)
//    }

//    override fun registerAdapterDataObserver(observer: RecyclerView.AdapterDataObserver) {
//        super.registerAdapterDataObserver(AdapterDataObserverProxy(observer, 1))
//    }

    companion object {
        private const val ITEM_TYPE_HEADER = 99
        private const val ITEM_TYPE_FOOTER = 100

        private val diffCallback = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean =
                oldItem == newItem

        }
    }
}