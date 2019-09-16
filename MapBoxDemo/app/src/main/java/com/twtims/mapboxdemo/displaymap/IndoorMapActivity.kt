package com.twtims.mapboxdemo.displaymap

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import com.mapbox.geojson.Point
import com.mapbox.geojson.Polygon
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.expressions.Expression
import com.mapbox.mapboxsdk.style.layers.FillLayer
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.turf.TurfJoins
import com.twtims.mapboxdemo.R
import kotlinx.android.synthetic.main.activity_indoor_map.*
import java.io.IOException

class IndoorMapActivity : AppCompatActivity() {

    private lateinit var indoorBuildingSource: GeoJsonSource

    private lateinit var boundingBoxList: List<List<Point>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_indoor_map)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { mapboxMap ->
            mapboxMap.setStyle(Style.MAPBOX_STREETS) {
                val boundingBox =  listOf(
                    Point.fromLngLat(-77.03791, 38.89715),
                    Point.fromLngLat(-77.03791, 38.89811),
                    Point.fromLngLat(-77.03532, 38.89811),
                    Point.fromLngLat(-77.03532, 38.89708)
                )
                boundingBoxList = listOf(boundingBox)
                mapboxMap.addOnCameraMoveListener {
                    if (mapboxMap.cameraPosition.zoom > 16) {
                        if (TurfJoins.inside(
                                Point.fromLngLat(mapboxMap.cameraPosition.target.longitude,
                                mapboxMap.cameraPosition.target.latitude),
                                Polygon.fromLngLats(boundingBoxList))){
                            if (levelButtons.visibility != View.VISIBLE){
                                showLevelButtons();
                            }
                        }else if (levelButtons.visibility == View.VISIBLE) {
                            hideLevelButtons();
                        }
                    }else if (levelButtons.visibility == View.VISIBLE) {
                        hideLevelButtons();
                    }
                }
                indoorBuildingSource = GeoJsonSource(
                    "indoor-building",
                    loadJsonFromAsset("white_house_lvl_0.geojson")
                )
                it.addSource(indoorBuildingSource)
                loadBuildingLayer(it)
            }
        }

        buttonSecondLevel.setOnClickListener {
            indoorBuildingSource.setGeoJson(loadJsonFromAsset("white_house_lvl_1.geojson"))
        }
        buttonGroupLevel.setOnClickListener {
            indoorBuildingSource.setGeoJson(loadJsonFromAsset("white_house_lvl_0.geojson"))
        }

    }

    private fun loadBuildingLayer(style: Style) {
        val indoorBuildingLayer = FillLayer(
            "indoor-building-fill", "indoor-building"
        ).withProperties(
            PropertyFactory.fillColor(Color.parseColor("#eeeeee")),
            PropertyFactory.fillOpacity(
                Expression.interpolate(
                    Expression.exponential(1f),
                    Expression.zoom(),
                    Expression.stop(16f, 0f),
                    Expression.stop(16.5f, 0.5f),
                    Expression.stop(17f, 1f)
                )
            )
        )
        style.addLayer(indoorBuildingLayer)

        val indoorBuildingLineLayer = LineLayer(
            "indoor-building-line", "indoor-building"
        ).withProperties(
            PropertyFactory.lineColor(Color.parseColor("#50667f")),
            PropertyFactory.lineWidth(0.5f),
            PropertyFactory.lineOpacity(
                Expression.interpolate(
                    Expression.exponential(1f),
                    Expression.zoom(),
                    Expression.stop(16f, 0f),
                    Expression.stop(16.5f, 0.5f),
                    Expression.stop(17f, 1f)
                )
            )
        )
        style.addLayer(indoorBuildingLineLayer)

    }

    private fun loadJsonFromAsset(fileName: String): String? {
        try {
            val inputStream = assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            return String(buffer)
        }catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }

    private fun showLevelButtons() {
        val animation = AlphaAnimation(0.0f, 1.0f)
        animation.duration = 500
        levelButtons.startAnimation(animation)
        levelButtons.visibility = View.VISIBLE
    }

    private fun hideLevelButtons() {
        val animation = AlphaAnimation(1.0f, 0.0f)
        animation.duration = 500
        levelButtons.startAnimation(animation)
        levelButtons.visibility = View.GONE
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
