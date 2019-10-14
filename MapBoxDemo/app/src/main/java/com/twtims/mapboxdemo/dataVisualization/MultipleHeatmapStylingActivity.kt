package com.twtims.mapboxdemo.dataVisualization

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.expressions.Expression
import com.mapbox.mapboxsdk.style.expressions.Expression.*
import com.mapbox.mapboxsdk.style.layers.HeatmapLayer
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.twtims.mapboxdemo.R
import kotlinx.android.synthetic.main.activity_multiple_heatmap_styling.*
import java.net.URI
import java.net.URISyntaxException

class MultipleHeatmapStylingActivity : AppCompatActivity() {

    companion object {
        private const val HEATMAP_SOURCE_ID = "HEATMAP_SOURCE_ID"
        private const val HEATMAP_LAYER_ID = "HEATMAP_LAYER_ID "
    }

    private var index = 0
    private lateinit var listOfHeatMapColors: Array<Expression>
    private lateinit var listOfHeatMapRadiusStops: Array<Expression>
    private lateinit var listOfHeatMapIntensityStops: Array<Float>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multiple_heatmap_styling)
        index = 0
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { mapboxMap ->
            mapboxMap.setStyle(Style.LIGHT) { style ->
                val cameraPosition = CameraPosition.Builder()
                    .target(LatLng(34.056684, -118.254002))
                    .zoom(11.047)
                    .build()
                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2600)
                try {
                    style.addSource(
                        GeoJsonSource(
                            HEATMAP_SOURCE_ID,
                            URI("asset://la_heatmap_styling_points.geojson")
                        )
                    )
                } catch (e: URISyntaxException) {
                    e.printStackTrace()
                }
                initHeatMapColors()
                initHeatMapRadiusStops()
                initHeatMapIntensityStops()
                addHeatMapLayer(style)
                switch_heatmap_style_fab.setOnClickListener {
                    index++
                    if (index == listOfHeatMapColors.size) {
                        index = 0
                    }
                    val heatMapLayer = style.getLayer(HEATMAP_LAYER_ID)
                    heatMapLayer?.setProperties(
                        PropertyFactory.heatmapColor(listOfHeatMapColors[index]),
                        PropertyFactory.heatmapRadius(listOfHeatMapRadiusStops[index]),
                        PropertyFactory.heatmapIntensity(listOfHeatMapIntensityStops[index])
                    )
                }
            }
        }
    }

    private fun addHeatMapLayer(style: Style) {
        val layer = HeatmapLayer(HEATMAP_LAYER_ID, HEATMAP_SOURCE_ID)
        layer.maxZoom = 18f
        layer.setProperties(
            PropertyFactory.heatmapColor(listOfHeatMapColors[index]),
            PropertyFactory.heatmapIntensity(listOfHeatMapIntensityStops[index]),
            PropertyFactory.heatmapRadius(listOfHeatMapRadiusStops[index]),
            PropertyFactory.heatmapOpacity(1f)
        )
        style.addLayerAbove(layer, "waterway-label")
    }

    private fun initHeatMapIntensityStops() {
        listOfHeatMapIntensityStops = arrayOf(
            // 0
            0.6f,
// 1
            0.3f,
// 2
            1f,
// 3
            1f,
// 4
            1f,
// 5
            1f,
// 6
            1.5f,
// 7
            0.8f,
// 8
            0.25f,
// 9
            0.8f,
// 10
            0.25f,
// 11
            0.5f
        )
    }

    private fun initHeatMapRadiusStops() {
        listOfHeatMapRadiusStops = arrayOf(
            interpolate(
                linear(), zoom(),
                literal(6), literal(50),
                literal(20), literal(100)
            ),
// 1
            interpolate(
                linear(), zoom(),
                literal(12), literal(70),
                literal(20), literal(100)
            ),
// 2
            interpolate(
                linear(), zoom(),
                literal(1), literal(7),
                literal(5), literal(50)
            ),
// 3
            interpolate(
                linear(), zoom(),
                literal(1), literal(7),
                literal(5), literal(50)
            ),
// 4
            interpolate(
                linear(), zoom(),
                literal(1), literal(7),
                literal(5), literal(50)
            ),
// 5
            interpolate(
                linear(), zoom(),
                literal(1), literal(7),
                literal(15), literal(200)
            ),
// 6
            interpolate(
                linear(), zoom(),
                literal(1), literal(10),
                literal(8), literal(70)
            ),
// 7
            interpolate(
                linear(), zoom(),
                literal(1), literal(10),
                literal(8), literal(200)
            ),
// 8
            interpolate(
                linear(), zoom(),
                literal(1), literal(10),
                literal(8), literal(200)
            ),
// 9
            interpolate(
                linear(), zoom(),
                literal(1), literal(10),
                literal(8), literal(200)
            ),
// 10
            interpolate(
                linear(), zoom(),
                literal(1), literal(10),
                literal(8), literal(200)
            ),
// 11
            interpolate(
                linear(), zoom(),
                literal(1), literal(10),
                literal(8), literal(200)
            )
        )
    }

    private fun initHeatMapColors() {
        listOfHeatMapColors = arrayOf(
            interpolate(
                linear(), heatmapDensity(),
                literal(0.01), rgba(0, 0, 0, 0.01),
                literal(0.25), rgba(224, 176, 63, 0.5),
                literal(0.5), rgb(247, 252, 84),
                literal(0.75), rgb(186, 59, 30),
                literal(0.9), rgb(255, 0, 0)
            ),
            interpolate(
                linear(), heatmapDensity(),
                literal(0.01), rgba(255, 255, 255, 0.4),
                literal(0.25), rgba(4, 179, 183, 1.0),
                literal(0.5), rgba(204, 211, 61, 1.0),
                literal(0.75), rgba(252, 167, 55, 1.0),
                literal(1), rgba(255, 78, 70, 1.0)
            ),
            interpolate(
                linear(), heatmapDensity(),
                literal(0.01), rgba(12, 182, 253, 0.0),
                literal(0.25), rgba(87, 17, 229, 0.5),
                literal(0.5), rgba(255, 0, 0, 1.0),
                literal(0.75), rgba(229, 134, 15, 0.5),
                literal(1), rgba(230, 255, 55, 0.6)
            ),
            interpolate(
                linear(), heatmapDensity(),
                literal(0.01), rgba(135, 255, 135, 0.2),
                literal(0.5), rgba(255, 99, 0, 0.5),
                literal(1), rgba(47, 21, 197, 0.2)
            ),
// 4
            interpolate(
                linear(), heatmapDensity(),
                literal(0.01), rgba(4, 0, 0, 0.2),
                literal(0.25), rgba(229, 12, 1, 1.0),
                literal(0.30), rgba(244, 114, 1, 1.0),
                literal(0.40), rgba(255, 205, 12, 1.0),
                literal(0.50), rgba(255, 229, 121, 1.0),
                literal(1), rgba(255, 253, 244, 1.0)
            ),
// 5
            interpolate(
                linear(), heatmapDensity(),
                literal(0.01), rgba(0, 0, 0, 0.01),
                literal(0.05), rgba(0, 0, 0, 0.05),
                literal(0.4), rgba(254, 142, 2, 0.7),
                literal(0.5), rgba(255, 165, 5, 0.8),
                literal(0.8), rgba(255, 187, 4, 0.9),
                literal(0.95), rgba(255, 228, 173, 0.8),
                literal(1), rgba(255, 253, 244, .8)
            ),
//6
            interpolate(
                linear(), heatmapDensity(),
                literal(0.01), rgba(0, 0, 0, 0.01),
                literal(0.3), rgba(82, 72, 151, 0.4),
                literal(0.4), rgba(138, 202, 160, 1.0),
                literal(0.5), rgba(246, 139, 76, 0.9),
                literal(0.9), rgba(252, 246, 182, 0.8),
                literal(1), rgba(255, 255, 255, 0.8)
            ),

//7
            interpolate(
                linear(), heatmapDensity(),
                literal(0.01), rgba(0, 0, 0, 0.01),
                literal(0.1), rgba(0, 2, 114, .1),
                literal(0.2), rgba(0, 6, 219, .15),
                literal(0.3), rgba(0, 74, 255, .2),
                literal(0.4), rgba(0, 202, 255, .25),
                literal(0.5), rgba(73, 255, 154, .3),
                literal(0.6), rgba(171, 255, 59, .35),
                literal(0.7), rgba(255, 197, 3, .4),
                literal(0.8), rgba(255, 82, 1, 0.7),
                literal(0.9), rgba(196, 0, 1, 0.8),
                literal(0.95), rgba(121, 0, 0, 0.8)
            ),
// 8
            interpolate(
                linear(), heatmapDensity(),
                literal(0.01), rgba(0, 0, 0, 0.01),
                literal(0.1), rgba(0, 2, 114, .1),
                literal(0.2), rgba(0, 6, 219, .15),
                literal(0.3), rgba(0, 74, 255, .2),
                literal(0.4), rgba(0, 202, 255, .25),
                literal(0.5), rgba(73, 255, 154, .3),
                literal(0.6), rgba(171, 255, 59, .35),
                literal(0.7), rgba(255, 197, 3, .4),
                literal(0.8), rgba(255, 82, 1, 0.7),
                literal(0.9), rgba(196, 0, 1, 0.8),
                literal(0.95), rgba(121, 0, 0, 0.8)
            ),
// 9
            interpolate(
                linear(), heatmapDensity(),
                literal(0.01), rgba(0, 0, 0, 0.01),
                literal(0.1), rgba(0, 2, 114, .1),
                literal(0.2), rgba(0, 6, 219, .15),
                literal(0.3), rgba(0, 74, 255, .2),
                literal(0.4), rgba(0, 202, 255, .25),
                literal(0.5), rgba(73, 255, 154, .3),
                literal(0.6), rgba(171, 255, 59, .35),
                literal(0.7), rgba(255, 197, 3, .4),
                literal(0.8), rgba(255, 82, 1, 0.7),
                literal(0.9), rgba(196, 0, 1, 0.8),
                literal(0.95), rgba(121, 0, 0, 0.8)
            ),
// 10
            interpolate(
                linear(), heatmapDensity(),
                literal(0.01), rgba(0, 0, 0, 0.01),
                literal(0.1), rgba(0, 2, 114, .1),
                literal(0.2), rgba(0, 6, 219, .15),
                literal(0.3), rgba(0, 74, 255, .2),
                literal(0.4), rgba(0, 202, 255, .25),
                literal(0.5), rgba(73, 255, 154, .3),
                literal(0.6), rgba(171, 255, 59, .35),
                literal(0.7), rgba(255, 197, 3, .4),
                literal(0.8), rgba(255, 82, 1, 0.7),
                literal(0.9), rgba(196, 0, 1, 0.8),
                literal(0.95), rgba(121, 0, 0, 0.8)
            ),
// 11
            interpolate(
                linear(), heatmapDensity(),
                literal(0.01), rgba(0, 0, 0, 0.25),
                literal(0.25), rgba(229, 12, 1, .7),
                literal(0.30), rgba(244, 114, 1, .7),
                literal(0.40), rgba(255, 205, 12, .7),
                literal(0.50), rgba(255, 229, 121, .8),
                literal(1), rgba(255, 253, 244, .8)
            )
        )
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
