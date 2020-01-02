package com.wlm.mvvm_wanandroid.net

import com.wlm.mvvm_wanandroid.base.HttpResponse
import com.wlm.mvvm_wanandroid.bean.*
import retrofit2.http.*


interface ApiService {

    companion object {
        const val BASE_URL = "https://www.wanandroid.com/"
//        const val TOOL_URL = "http://www.wanandroid.com/tools"
    }

//    @FormUrlEncoded
//    @POST("user/login")
//    suspend fun login(@Field("username") username: String,
//              @Field("password") password: String): HttpResponse<User>
//
//    @FormUrlEncoded
//    @POST("user/register")
//    suspend fun register(@Field("username") username: String,
//                 @Field("password") password: String,
//                 @Field("repassword") rePassword: String): CustomResponse<User>

    ///page 页码，从1开始
    @GET("article/list/{page}/json")
    suspend fun getArticles(@Path("page") page: Int): HttpResponse<ArticleList>

    @GET("banner/json")
    suspend fun getBanners(): HttpResponse<List<BannerData>>

    @GET("article/top/json")
    suspend fun getTop(): HttpResponse<List<Article>>

    @GET("tree/json")
    suspend fun getKnowledgeTree(): HttpResponse<List<Knowledge>>

    ///page 页码，从0开始
    @GET("article/list/{page}/json")
    suspend fun getKnowledgeItem(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): HttpResponse<ArticleList>

    @GET("navi/json")
    suspend fun getNavigation(): HttpResponse<List<Navigation>>

    @GET("project/tree/json")
    suspend fun getProjectTree(): HttpResponse<List<Knowledge>>

    ///page 页码，从1开始
    @GET("project/list/{page}/json")
    suspend fun getProjectItem(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): HttpResponse<ArticleList>

    @GET("hotkey/json")
    suspend fun getHotKey(): HttpResponse<List<HotKey>>

    ///page 页码，从0开始
    @POST("article/query/{page}/json")
    suspend fun queryArticles(
        @Path("page") page: Int,
        @Query("k") queryKey: String
    ): HttpResponse<ArticleList>

    @GET("wxarticle/chapters/json")
    suspend fun getWxList(): HttpResponse<List<Knowledge>>

    ///page 页码，从1开始
    @GET("wxarticle/list/{id}/{page}/json")
    suspend fun getWxArticles(
        @Path("id") id: Int,
        @Path("page") page: Int
    ): HttpResponse<ArticleList>
//    @POST("lg/collect/{id}/json")
//    suspend fun collectArticle(@Path("id") id: Int): CustomResponse<ArticleList>
//
//    @POST("lg/uncollect_originId/{id}/json")
//    suspend fun unCollectArticle(@Path("id") id: Int): CustomResponse<ArticleList>
//
//    @GET("user/logout/json")
//    suspend fun logout() : CustomResponse<Any>
//
//    @GET("user_article/list/{page}/json")
//    suspend fun getSquareArticleList(@Path("page") page: Int): CustomResponse<ArticleList>
}