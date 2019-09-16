package com.twtims.mapboxdemo.label

import android.animation.ObjectAnimator
import android.animation.TypeEvaluator
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.twtims.mapboxdemo.R
import kotlinx.android.synthetic.main.activity_marker_following_route.*
import java.io.InputStream
import java.lang.ref.WeakReference
import java.util.*
import com.mapbox.mapboxsdk.geometry.LatLng
import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Handler
import android.view.animation.LinearInterpolator
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.mapboxsdk.utils.BitmapUtils

class MarkerFollowingRouteActivity : AppCompatActivity() {

    companion object {
        private const val DOT_SOURCE_ID = "dot-source-id"
        private const val LINE_SOURCE_ID = "line-source-id"
        private val latLngEvaluator = object : TypeEvaluator<LatLng> {
            private val latLng = LatLng()
            override fun evaluate(fraction: Float, startValue: LatLng, endValue: LatLng): LatLng {
                latLng.latitude =
                    startValue.latitude + ((endValue.latitude - startValue.latitude) * fraction)
                latLng.longitude =
                    startValue.longitude + ((endValue.longitude - startValue.longitude) * fraction)
                return latLng
            }

        }
    }

    private var mapboxMap: MapboxMap? = null
    private lateinit var routeCoordinateList: List<Point>
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private var dotGeoJsonSource: GeoJsonSource? = null
    private var markerIconAnimator: ValueAnimator? = null
    private var markerIconCurrentLocation: LatLng? = null
    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_marker_following_route)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { mapboxMap ->
            this.mapboxMap = mapboxMap
            mapboxMap.setStyle(Style.LIGHT) {
                LoadGeoJson(this).execute()
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

    fun initData(featureCollection: FeatureCollection) {
        val lineString = featureCollection.features()!![0].geometry() as LineString
        routeCoordinateList = lineString.coordinates();
        mapboxMap?.getStyle {
            initSources(it, featureCollection)
            initSymbolLayer(it)
            initDotLinePath(it)
            initRunnable()
        }
    }

    private fun initSources(style: Style, featureCollection: FeatureCollection) {
        dotGeoJsonSource = GeoJsonSource(DOT_SOURCE_ID, featureCollection)
        style.addSource(dotGeoJsonSource!!)
        style.addSource(GeoJsonSource(LINE_SOURCE_ID, featureCollection))
    }

    private fun initSymbolLayer(style: Style) {
        style.addImage(
            "moving-pink-dot",
            BitmapUtils.getBitmapFromDrawable(resources.getDrawable(R.mipmap.pink_dot))!!
        )
        style.addLayer(
            SymbolLayer(
                "symbol-layer-id",
                DOT_SOURCE_ID
            ).withProperties(
                PropertyFactory.iconImage("moving-pink-dot"),
                PropertyFactory.iconSize(1f),
                PropertyFactory.iconIgnorePlacement(true),
                PropertyFactory.iconAllowOverlap(true)
            )
        )
    }

    private fun initDotLinePath(style: Style) {
        style.addLayer(
            LineLayer(
                "line-layer-id",
                LINE_SOURCE_ID
            ).withProperties(
                PropertyFactory.lineColor(Color.parseColor("#f13c6e")),
                PropertyFactory.lineWidth(4f)
            )
        )
    }

    private fun initRunnable() {
        handler = Handler()
        runnable = Runnable {
            if (routeCoordinateList.size - 1 > count) {
                val nextLocation = routeCoordinateList[count + 1]
                markerIconAnimator?.let {
                    if (it.isStarted) {
                        markerIconCurrentLocation = it.animatedValue as LatLng
                        it.cancel()
                    }
                }
                markerIconAnimator = ObjectAnimator.ofObject(
                    latLngEvaluator,
                    if (count == 0 || markerIconCurrentLocation == null) LatLng(
                        37.61501,
                        -122.385374
                    ) else markerIconCurrentLocation,
                    LatLng(nextLocation.latitude(), nextLocation.longitude())
                ).setDuration(300)
                markerIconAnimator?.let {
                    it.interpolator = LinearInterpolator()
                    it.addUpdateListener(animatorUpdateListener)
                    it.start()
                }
                count++
                handler.postDelayed(runnable, 300)

            }
        }
        handler.post(runnable)
    }

    private val animatorUpdateListener =
        ValueAnimator.AnimatorUpdateListener { animation ->
            val animatedPosition = animation?.animatedValue as LatLng
            dotGeoJsonSource?.setGeoJson(
                Point.fromLngLat(
                    animatedPosition.longitude, animatedPosition.latitude
                )
            )
        }


}

class LoadGeoJson constructor(activity: MarkerFollowingRouteActivity) :
    AsyncTask<Void, Void, FeatureCollection>() {

    private val weakReference: WeakReference<MarkerFollowingRouteActivity> = WeakReference(activity)

    override fun doInBackground(vararg voids: Void): FeatureCollection? {
        try {
            val activity = weakReference.get()
            if (activity != null) {
                val inputStream = activity.assets.open("matched_route.geojson")
                return FeatureCollection.fromJson(
                    convertStreamToString(
                        inputStream
                    )
                )
            }
        } catch (exception: Exception) {
            Log.d("TAG", exception.toString())
        }

        return null
    }

    override fun onPostExecute(featureCollection: FeatureCollection?) {
        super.onPostExecute(featureCollection)
        val activity = weakReference.get()
        if (activity != null && featureCollection != null) {
            activity.initData(featureCollection)
        }
    }

    companion object {

        fun convertStreamToString(inputStream: InputStream): String {
            val scanner = Scanner(inputStream).useDelimiter("\\A")
            return if (scanner.hasNext()) scanner.next() else ""
        }
    }
}
