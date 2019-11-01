package com.wlm.mvvmdemo.net

import com.wlm.baselib.base.BaseApp
import com.wlm.baselib.utils.AppUtils
import com.wlm.baselib.utils.PreferenceUtils
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit


/**
 * @author Milker
 * @since 2019.10.22
 * retrofit网络请求管理类
 */
object RetrofitManager {
    val service: ApiService by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        getRetrofit().create(ApiService::class.java)
    }

    private fun getRetrofit(): Retrofit {
        // 获取retrofit的实例
        return Retrofit.Builder()
            .baseUrl(ApiService.BASE_URL)  //自己配置
            .client(getOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun getOkHttpClient(): OkHttpClient {
        //添加一个log拦截器,打印所有的log
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        //可以设置请求过滤的水平,body,basic,headers
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val cacheFile = File(BaseApp.mContext.cacheDir, "cache")
        val cache = Cache(cacheFile, 1024 * 1024 * 50)//50Mb 缓存的大小

        return OkHttpClient.Builder()
            .addInterceptor(addQueryParameterInterceptor()) //参数添加
            .addInterceptor(addHeaderInterceptor()) //token过滤
            .addInterceptor(httpLoggingInterceptor)//日志,所有的请求响应度看到
            .cache(cache) //添加缓存
            .connectTimeout(60L, TimeUnit.SECONDS)
            .readTimeout(60L, TimeUnit.SECONDS)
            .writeTimeout(60L, TimeUnit.SECONDS)
            .build()
    }

    private fun addQueryParameterInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val modifiedUrl = originalRequest.url.newBuilder()
                // Provide your custom parameter here
                .addQueryParameter("udid", "d2807c895f0348a180148c9dfa6f2feeac0781b5")
                .addQueryParameter("deviceModel", AppUtils.getMobileModel())
                .build()
            val request = originalRequest.newBuilder().url(modifiedUrl).build()
            chain.proceed(request)
        }
    }

    private val token: String by PreferenceUtils("token", "")

    private fun addHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originRequest = chain.request()
            val requestBuilder = originRequest.newBuilder()
                .header("token", token)
                .method(originRequest.method, originRequest.body)
            val request = requestBuilder.build()
            chain.proceed(request)
        }
    }
}