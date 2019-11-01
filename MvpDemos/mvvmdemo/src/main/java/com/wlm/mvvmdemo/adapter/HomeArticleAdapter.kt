package com.wlm.mvvmdemo.adapter

import com.wlm.baselib.base.BaseBindAdapter
import com.wlm.mvvmdemo.BR
import com.wlm.mvvmdemo.R
import com.wlm.mvvmdemo.bean.Article

class HomeArticleAdapter(layoutId: Int = R.layout.item_article) : BaseBindAdapter<Article>(layoutId, BR.article) {

    private var showStar = true

    fun showStar(showStar: Boolean) {
        this.showStar = showStar
    }

    override fun convert(helper: BindViewHolder, item: Article) {
        super.convert(helper, item)
        helper.addOnClickListener(R.id.articleStar)
        if (showStar) helper.setImageResource(R.id.articleStar, if (item.collect) R.mipmap.timeline_like_pressed else R.mipmap.timeline_like_normal)
        else helper.setVisible(R.id.articleStar, false)
    }
}