package com.twtims.mapboxdemo.displaymap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.expressions.Expression
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.RasterLayer
import com.mapbox.mapboxsdk.style.sources.RasterSource
import com.twtims.mapboxdemo.R
import kotlinx.android.synthetic.main.activity_satellite_opacity_on_zoom.*

class SatelliteOpacityOnZoomActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_satellite_opacity_on_zoom)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { mapboxMap ->
            mapboxMap.setStyle(Style.MAPBOX_STREETS) { style ->
                style.addSource(
                    RasterSource(
                        "SATELLITE_RASTER_SOURCE_ID",
                        "mapbox://mapbox.satellite",
                        512
                    )
                )
                style.addLayer(
                    RasterLayer(
                        "SATELLITE_RASTER_LAYER_ID",
                        "SATELLITE_RASTER_SOURCE_ID"
                    ).withProperties(
                        PropertyFactory.rasterOpacity(
                            Expression.interpolate(
                                Expression.linear(),
                                Expression.zoom(),
                                Expression.stop(15, 0),
                                Expression.stop(18, 1)
                            )
                        )
                    )
                )
                mapboxMap.animateCamera(
                    CameraUpdateFactory.newCameraPosition(CameraPosition.Builder().zoom(19.0).build()), 9000
                )
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
