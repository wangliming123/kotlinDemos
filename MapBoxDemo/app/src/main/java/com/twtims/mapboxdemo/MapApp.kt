package com.twtims.mapboxdemo

import android.app.Application
import com.mapbox.mapboxsdk.Mapbox

class MapApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Mapbox.getInstance(this, getString(R.string.str_access_token))
    }
}
