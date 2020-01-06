package com.wlm.mvvm_wanandroid.common.net

import com.wlm.mvvm_wanandroid.base.HttpResponse
import com.wlm.mvvm_wanandroid.common.*
import retrofit2.http.*


interface ApiService {

    companion object {
        const val BASE_URL = "https://www.wanandroid.com/"
//        const val TOOL_URL = "http://www.wanandroid.com/tools"
    }


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


    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(@Field("username") username: String,
              @Field("password") password: String): HttpResponse<User>


    @FormUrlEncoded
    @POST("user/register")
    suspend fun register(@Field("username") username: String,
                 @Field("password") password: String,
                 @Field("repassword") rePassword: String): HttpResponse<User>


    @GET("user/logout/json")
    suspend fun logout() : HttpResponse<Any>

    @POST("lg/collect/{id}/json")
    suspend fun collect(@Path("id") id: Int): HttpResponse<Any>


    @POST("lg/uncollect_originId/{origin_id}/json")
    suspend fun unCollect(@Path("origin_id") id: Int): HttpResponse<Any>

    @GET("lg/collect/list/{page}/json")
    suspend fun getCollectArticles(@Path("page") page: Int): HttpResponse<ArticleList>

//    @POST("lg/uncollect/{id}/json")
//    suspend fun unCollectById(@Path("id") id: Int): HttpResponse<Any>

//    @POST("lg/collect/{id}/json")
//    suspend fun collectArticle(@Path("id") id: Int): CustomResponse<ArticleList>
//
//    @POST("lg/uncollect_originId/{id}/json")
//    suspend fun unCollectArticle(@Path("id") id: Int): CustomResponse<ArticleList>
//
//
//    @GET("user_article/list/{page}/json")
//    suspend fun getSquareArticleList(@Path("page") page: Int): CustomResponse<ArticleList>
}