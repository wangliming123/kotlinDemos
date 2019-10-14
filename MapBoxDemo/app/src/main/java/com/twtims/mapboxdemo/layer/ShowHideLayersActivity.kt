package com.twtims.mapboxdemo.layer

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.CircleLayer
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.sources.VectorSource
import com.twtims.mapboxdemo.R
import kotlinx.android.synthetic.main.activity_show_hide_layers.*

class ShowHideLayersActivity : AppCompatActivity() {

    private var map: MapboxMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_hide_layers)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { mapboxMap ->
            this.map = mapboxMap
            mapboxMap.setStyle(Style.LIGHT) { style ->
                style.addSource(VectorSource("museums_source", "mapbox://mapbox.2opop9hr"))
                val museumsLayer = CircleLayer("museums", "museums_source")
                museumsLayer.sourceLayer = "museum-cusco"
                museumsLayer.setProperties(
                    PropertyFactory.visibility(Property.VISIBLE),
                    PropertyFactory.circleRadius(8f),
                    PropertyFactory.circleColor(Color.argb(255, 55, 148, 179))
                )
                style.addLayer(museumsLayer)
            }
        }
        fab_layer_toggle.setOnClickListener {
            toggleLayer();
        }

    }

    private fun toggleLayer() {
        map?.getStyle { style ->
            val layer = style.getLayer("museums")
            layer?.let {
                if (Property.VISIBLE == it.visibility.value) {
                    it.setProperties(PropertyFactory.visibility(Property.NONE))
                } else {
                    it.setProperties(PropertyFactory.visibility(Property.VISIBLE))
                }
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
