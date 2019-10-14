package com.twtims.mapboxdemo

import android.app.Application
import com.mapbox.mapboxsdk.Mapbox
import com.squareup.picasso.OkHttpDownloader
import com.squareup.picasso.Picasso

class MapApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Mapbox.getInstance(applicationContext, getString(R.string.str_access_token))

//        setUpPicasso()
    }

    private fun setUpPicasso() {
        val picasso = Picasso.Builder(this)
            .downloader(OkHttpDownloader(this, Int.MAX_VALUE.toLong()))
            .build()
        Picasso.setSingletonInstance(picasso)
    }
}
