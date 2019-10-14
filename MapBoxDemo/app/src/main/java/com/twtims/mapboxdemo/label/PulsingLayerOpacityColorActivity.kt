package com.twtims.mapboxdemo.label

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.twtims.mapboxdemo.R
import android.animation.ValueAnimator
import android.graphics.Color
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.CircleLayer
import com.mapbox.mapboxsdk.style.layers.FillLayer
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import kotlinx.android.synthetic.main.activity_pulsing_layer_opacity_color.*
import java.io.IOException
import java.nio.charset.Charset

class PulsingLayerOpacityColorActivity : AppCompatActivity() {

    private var parkColorAnimator: ValueAnimator? = null
    private var hotelColorAnimator: ValueAnimator? = null
    private var attractionsColorAnimator: ValueAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pulsing_layer_opacity_color)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { mapboxMap ->
            mapboxMap.setStyle(Style.LIGHT) { style->
                val hotelSource = GeoJsonSource("hotels", loadJsonFromAsset("la_hotels.geojson"))
                style.addSource(hotelSource)
                val hotelLayer = FillLayer("hotels", "hotels").withProperties(
                    PropertyFactory.fillColor(Color.parseColor("#5a9fcf")),
                    PropertyFactory.visibility(Property.NONE)
                )
                style.addLayer(hotelLayer)
                val hotels = style.getLayer("hotels") as FillLayer?
                hotelColorAnimator = ValueAnimator.ofObject(
                    ArgbEvaluator(),
                    Color.parseColor("#5a9fcf"),
                    Color.parseColor("#2c6b97")
                )
                hotelColorAnimator?.apply {
                    duration = 1000
                    repeatCount = ValueAnimator.INFINITE
                    repeatMode = ValueAnimator.REVERSE
                    addUpdateListener { animator ->
                        hotels?.setProperties(PropertyFactory.fillColor(animator.animatedValue as Int))
                    }
                }

                val attractionsSource =
                    GeoJsonSource("attractions", loadJsonFromAsset("la_attractions.geojson"))
                style.addSource(attractionsSource)
                val attractionsLayer = CircleLayer("attractions", "attractions").withProperties(
                    PropertyFactory.circleColor(Color.parseColor("#5a9fcf")),
                    PropertyFactory.visibility(Property.NONE)
                )
                style.addLayer(attractionsLayer)

                val attractions = style.getLayer("attractions") as CircleLayer?
                attractionsColorAnimator = ValueAnimator.ofObject(
                    ArgbEvaluator(),
                    Color.parseColor("#ec8a8a"),
                    Color.parseColor("#de3232")
                )
                attractionsColorAnimator?.apply {
                    duration = 1000
                    repeatCount = ValueAnimator.INFINITE
                    repeatMode = ValueAnimator.REVERSE
                    addUpdateListener { animator ->
                        attractions?.setProperties(
                            PropertyFactory.circleColor(animator.animatedValue as Int)
                        )
                    }
                }

                val parks = style.getLayer("landuse") as FillLayer?
                parks?.setProperties(PropertyFactory.visibility(Property.NONE))
                parkColorAnimator = ValueAnimator.ofObject(
                    ArgbEvaluator(),
                    Color.parseColor("#7ac79c"),
                    Color.parseColor("#419a68")
                )
                parkColorAnimator?.apply {
                    duration = 1000
                    repeatCount = ValueAnimator.INFINITE
                    repeatMode = ValueAnimator.REVERSE
                    addUpdateListener { animator ->
                        parks?.setProperties(
                            PropertyFactory.fillColor(animator.animatedValue as Int)
                        )
                    }
                }

                fab_toggle_hotels.setOnClickListener {
                    setLayerVisible("hotels", style)
                }
                fab_toggle_parks.setOnClickListener{
                    setLayerVisible("landuse", style)
                }
                fab_toggle_attractions.setOnClickListener {
                    setLayerVisible("attractions", style)
                }
                parkColorAnimator?.start()
                hotelColorAnimator?.start()
                attractionsColorAnimator?.start()
            }
        }
    }

    private fun setLayerVisible(layerId: String, style: Style) {
        val layer = style.getLayer(layerId);
        layer?.let {
            if (Property.VISIBLE == layer.visibility.value){
                layer.setProperties(PropertyFactory.visibility(Property.NONE))
            }else{
                layer.setProperties(PropertyFactory.visibility(Property.VISIBLE))
            }
        }
    }

    private fun loadJsonFromAsset(fileName: String): String? {
        return try {
            val inputStream = assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charset.forName("utf-8"))
        } catch (e: IOException) {
            e.printStackTrace()
            null
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
