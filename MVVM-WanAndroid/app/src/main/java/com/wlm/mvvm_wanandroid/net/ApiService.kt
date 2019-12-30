package com.wlm.mvvm_wanandroid.net

import com.wlm.mvvm_wanandroid.base.HttpResponse
import com.wlm.mvvm_wanandroid.bean.ArticleList
import com.wlm.mvvm_wanandroid.bean.BannerData
import com.wlm.mvvm_wanandroid.bean.Knowledge
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

    @GET("article/list/{page}/json")
    suspend fun getArticles(@Path("page") page: Int): HttpResponse<ArticleList>

    @GET("banner/json")
    suspend fun getBanners(): HttpResponse<List<BannerData>>

    @GET("tree/json")
    suspend fun getKnowledgeTree(): HttpResponse<List<Knowledge>>

    @GET("article/list/{page}/json")
    suspend fun searchArticles(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): HttpResponse<ArticleList>
//
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