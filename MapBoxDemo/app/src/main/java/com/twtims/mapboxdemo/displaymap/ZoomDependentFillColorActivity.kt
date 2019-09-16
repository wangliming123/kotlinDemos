package com.twtims.mapboxdemo.displaymap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.expressions.Expression
import com.mapbox.mapboxsdk.style.layers.FillLayer
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.twtims.mapboxdemo.R
import kotlinx.android.synthetic.main.activity_zoom_dependent_fill_color.*

class ZoomDependentFillColorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zoom_dependent_fill_color)

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { mapboxMap ->
            mapboxMap.setStyle(Style.DARK) { style->
                val layer = style.getLayerAs<FillLayer>("water")
                layer?.let {
                    it.setProperties(
                        PropertyFactory.fillColor(Expression.interpolate(
                            Expression.exponential(1f),
                            Expression.zoom(),
                            Expression.stop(1f, Expression.rgb(0,209,22)),
                            Expression.stop(8.5f, Expression.rgb(10,88,255)),
                            Expression.stop(10f, Expression.rgb(255,10,10)),
                            Expression.stop(18f, Expression.rgb(251,255,0))
                        ))
                    )
                    mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        LatLng(40.73581, -73.99155), 12.0),
                        12000)
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
