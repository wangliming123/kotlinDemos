package com.wlm.mvvm_wanandroid.bean

import androidx.room.PrimaryKey

data class ArticleList(
    val curPage: Int,
    val datas: MutableList<Article>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
)

data class Article(
    val apkLink: String,
    val audit: Int,
    val author: String,
    val chapterId: Int,
    val chapterName: String,
    val collect: Boolean,
    val courseId: Int,
    val desc: String?,
    val envelopePic: String,
    val fresh: Boolean,
    @PrimaryKey
    val id: Int,
    val link: String,
    val niceDate: String,
    val niceShareDate: String,
    val origin: String,
    val prefix: String,
    val projectLink: String,
    val publishTime: Long,
    val selfVisible: Int,
    val shareDate: Long,
    val shareUser: String,
    val superChapterId: Int,
    val superChapterName: String,
    val tags: List<Any>,
    val title: String,
    val type: Int,
    val userId: Int,
    val visible: Int,
    val zan: Int,
    var bannerList: List<BannerData>?
)
