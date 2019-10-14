package com.twtims.mapboxdemo.dataVisualization

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.expressions.Expression
import com.mapbox.mapboxsdk.style.layers.CircleLayer
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.twtims.mapboxdemo.R
import kotlinx.android.synthetic.main.activity_create_hotspots.*
import java.net.URI
import java.net.URISyntaxException

class CreateHotspotsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_hotspots)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { mapboxMap ->
            mapboxMap.setStyle(Style.DARK) { style ->
                addClusteredGeoJsonSource(style)
            }
        }
    }

    private fun addClusteredGeoJsonSource(style: Style) {
        try {
            style.addSource(GeoJsonSource("earthquakes",
                URI("https://www.mapbox.com/mapbox-gl-js/assets/earthquakes.geojson"),
                GeoJsonOptions()
                    .withCluster(true)
                    .withClusterMaxZoom(15)
                    .withClusterRadius(20)
            ))
        }catch (e: URISyntaxException) {
            e.printStackTrace()
        }
        val layers = Array(3) {IntArray(2)}
        layers[0] = intArrayOf(150, Color.parseColor("#E55E5E"))
        layers[1] = intArrayOf(20, Color.parseColor("#F9886C"))
        layers[2] = intArrayOf(0, Color.parseColor("#FBB03B"))
        val unClustered = CircleLayer("unclustered-points", "earthquakes")
        unClustered.setProperties(
            PropertyFactory.circleColor(Color.parseColor("#fbb03b")),
            PropertyFactory.circleRadius(20f),
            PropertyFactory.circleBlur(1f)
        )
        unClustered.setFilter(Expression.neq(Expression.get("cluster"), Expression.literal(true)))
        style.addLayerBelow(unClustered, "building")
        layers.forEachIndexed { index, layer ->
            val circles = CircleLayer("cluster-$index", "earthquakes")
            circles.setProperties(
                PropertyFactory.circleColor(layer[1]),
                PropertyFactory.circleRadius(70f),
                PropertyFactory.circleBlur(1f)
            )
            val pointCount = Expression.toNumber(Expression.get("point_count"))
            circles.setFilter(
                if (index == 0)
                    Expression.gte(pointCount, Expression.literal(layer[0]))else
                    Expression.all(
                        Expression.gte(pointCount, Expression.literal(layer[0])),
                        Expression.lt(pointCount, Expression.literal(layers[index - 1][0]))
                    )
            )
            style.addLayerBelow(circles, "building")
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
