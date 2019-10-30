package com.wlm.mvpdemos

import com.wlm.mvpdemos.bean.ArticleBean
import com.wlm.mvpdemos.bean.LoginBean
import com.wlm.mvpdemos.bean.RegisterBean
import io.reactivex.Observable
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("user/login")
    fun login(@Field("username") username: String,
              @Field("password") password: String): Observable<LoginBean>

    @FormUrlEncoded
    @POST("user/register")
    fun register(@Field("username") username: String,
                 @Field("password") password: String,
                 @Field("repassword") rePassword: String): Observable<RegisterBean>

    @GET("/article/list/{page}/json")
    fun getArticles(@Path("page") page: Int): Observable<ArticleBean>
}