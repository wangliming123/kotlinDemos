package com.twtims.mapboxdemo.layer

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.HillshadeLayer
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.sources.RasterDemSource
import com.twtims.mapboxdemo.R
import kotlinx.android.synthetic.main.activity_hill_shade.*

class HillShadeActivity : AppCompatActivity(), OnMapReadyCallback {
    private val LAYER_ID = "hillshade-layer"
    private val SOURCE_ID = "hillshade-source"
    private val SOURCE_URL = "mapbox://mapbox.terrain-rgb"
    private val HILLSHADE_HIGHLIGHT_COLOR = "#008924"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hill_shade)

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        mapboxMap.setStyle(Style.OUTDOORS, Style.OnStyleLoaded {
            val rasterDemSource = RasterDemSource(SOURCE_ID, SOURCE_URL)
            it.addSource(rasterDemSource)
            val hillshadeLayer = HillshadeLayer(LAYER_ID, SOURCE_ID).withProperties(
                PropertyFactory.hillshadeHighlightColor(Color.parseColor(HILLSHADE_HIGHLIGHT_COLOR)),
                PropertyFactory.hillshadeShadowColor(Color.BLACK)
            )
            it.addLayerBelow(hillshadeLayer, "aerialway")
        })
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
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
