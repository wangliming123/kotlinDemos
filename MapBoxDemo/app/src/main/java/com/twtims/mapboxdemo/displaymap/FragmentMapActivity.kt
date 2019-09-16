package com.twtims.mapboxdemo.displaymap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMapOptions
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.maps.SupportMapFragment
import com.twtims.mapboxdemo.R

class FragmentMapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_map)

        var mapFragment: SupportMapFragment
        if (savedInstanceState == null) {
            val transaction = supportFragmentManager.beginTransaction()
            val options = MapboxMapOptions.createFromAttributes(this, null)
                .camera(
                    CameraPosition.Builder()
                        .target(LatLng(30.46, 114.39))
                        .zoom(11.0)
                        .build()
                )
            mapFragment = SupportMapFragment.newInstance(options)
            transaction.add(R.id.fl_content, mapFragment, "com.mapbox.map")
            transaction.commit()
        } else {
            mapFragment = supportFragmentManager.findFragmentByTag("com.mapbox.map") as SupportMapFragment
        }
        mapFragment.getMapAsync { mapboxMap ->
            mapboxMap.setStyle(Style.SATELLITE, Style.OnStyleLoaded {

            })
        }
    }
}
