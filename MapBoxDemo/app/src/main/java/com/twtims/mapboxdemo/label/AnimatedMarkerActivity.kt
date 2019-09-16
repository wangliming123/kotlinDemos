package com.twtims.mapboxdemo.label

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.mapbox.geojson.Feature
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.twtims.mapboxdemo.R
import kotlinx.android.synthetic.main.activity_animated_marker.*
import android.animation.TypeEvaluator

class AnimatedMarkerActivity : AppCompatActivity(), OnMapReadyCallback, MapboxMap.OnMapClickListener {

    private var currentPosition: LatLng = LatLng(64.900932, -18.167040)
    private lateinit var geoJsonSource: GeoJsonSource
    private var animator: ValueAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animated_marker)
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this)
    }


    override fun onMapReady(mapboxMap: MapboxMap) {
        geoJsonSource = GeoJsonSource("source-id",
            Feature.fromGeometry(Point.fromLngLat(currentPosition.longitude, currentPosition.latitude)))
        mapboxMap.setStyle(Style.SATELLITE_STREETS, Style.OnStyleLoaded {
            it.addImage("marker_icon", BitmapFactory.decodeResource(resources, R.mipmap.red_marker))
            it.addSource(geoJsonSource)
            it.addLayer(SymbolLayer("layer-id", "source-id").withProperties(
                PropertyFactory.iconImage("marker_icon"),
                PropertyFactory.iconIgnorePlacement(true),
                PropertyFactory.iconAllowOverlap(true)
            ))
            Toast.makeText(
                this,
                getString(R.string.tap_on_map_instruction),
                Toast.LENGTH_LONG
            ).show();
            mapboxMap.addOnMapClickListener(this)
        })
    }

    override fun onMapClick(point: LatLng): Boolean {
        if (animator != null && animator!!.isStarted()) {
            currentPosition = animator?.getAnimatedValue() as LatLng
            animator?.cancel()
        }
        animator = ObjectAnimator
            .ofObject(latLngEvaluator, currentPosition, point)
            .setDuration(4000)

        animator?.addUpdateListener(animatorUpdateListener)
        animator?.start()
        return true
    }

    private val animatorUpdateListener = object : ValueAnimator.AnimatorUpdateListener {
        override fun onAnimationUpdate(valueAnimator: ValueAnimator?) {
            if (valueAnimator == null) {
                return
            }
            currentPosition = valueAnimator.getAnimatedValue() as LatLng
            geoJsonSource.setGeoJson(Point.fromLngLat(currentPosition.longitude, currentPosition.latitude))
        }

    }

    private val latLngEvaluator = object : TypeEvaluator<LatLng> {

        private val latLng = LatLng()

        override fun evaluate(fraction: Float, startValue: LatLng, endValue: LatLng): LatLng {
            latLng.latitude = startValue.latitude + (endValue.latitude - startValue.latitude) * fraction
            latLng.longitude = startValue.longitude + (endValue.longitude - startValue.longitude) * fraction
            return latLng
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
