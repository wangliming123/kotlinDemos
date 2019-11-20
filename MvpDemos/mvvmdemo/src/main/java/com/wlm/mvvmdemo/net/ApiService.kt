package com.wlm.mvvmdemo.net

import com.wlm.baselib.net.CustomResponse
import com.wlm.mvvmdemo.bean.ArticleList
import com.wlm.mvvmdemo.bean.BannerData
import com.wlm.mvvmdemo.bean.User
import retrofit2.http.*


interface ApiService {

    companion object {
        const val BASE_URL = "https://www.wanandroid.com/"
        const val TOOL_URL = "http://www.wanandroid.com/tools"
    }

    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(@Field("username") username: String,
              @Field("password") password: String): CustomResponse<User>

    @FormUrlEncoded
    @POST("user/register")
    suspend fun register(@Field("username") username: String,
                 @Field("password") password: String,
                 @Field("repassword") rePassword: String): CustomResponse<User>

    @GET("article/list/{page}/json")
    suspend fun getArticles(@Path("page") page: Int): CustomResponse<ArticleList>

    @GET("banner/json")
    suspend fun getBanners(): CustomResponse<List<BannerData>>

    @POST("lg/collect/{id}/json")
    suspend fun collectArticle(@Path("id") id: Int): CustomResponse<ArticleList>

    @POST("lg/uncollect_originId/{id}/json")
    suspend fun unCollectArticle(@Path("id") id: Int): CustomResponse<ArticleList>

    @GET("user/logout/json")
    suspend fun logout() : CustomResponse<Any>

    @GET("user_article/list/{page}/json")
    suspend fun getSquareArticleList(@Path("page") page: Int): CustomResponse<ArticleList>
}