package com.twtims.mapboxdemo.displaymap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngQuad
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.RasterLayer
import com.mapbox.mapboxsdk.style.sources.ImageSource
import com.twtims.mapboxdemo.R
import kotlinx.android.synthetic.main.activity_image_source.*

class ImageSourceActivity : AppCompatActivity() {

    companion object {
        private const val ID_IMAGE_SOURCE = "animated_image_source"
        private const val ID_IMAGE_LAYER = "animated_image_layer"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_source)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { mapboxMap ->
            mapboxMap.setStyle(Style.DARK) {
                val quad = LatLngQuad(
                    LatLng(25.7836, -80.11725),
                    LatLng(25.783548, -80.1397431334),
                    LatLng(25.7680, -80.13964),
                    LatLng(25.76795, -80.11725)
                )
                it.addSource(ImageSource(ID_IMAGE_SOURCE, quad, R.mipmap.miami_beach))
                it.addLayer(RasterLayer(ID_IMAGE_LAYER, ID_IMAGE_SOURCE))
            }

        }

    }


    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    public override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}
