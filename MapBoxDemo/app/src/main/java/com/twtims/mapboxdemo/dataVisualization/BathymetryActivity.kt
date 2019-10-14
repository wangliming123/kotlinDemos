package com.twtims.mapboxdemo.dataVisualization

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.expressions.Expression
import com.mapbox.mapboxsdk.style.expressions.Expression.*
import com.mapbox.mapboxsdk.style.layers.FillLayer
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.twtims.mapboxdemo.R
import kotlinx.android.synthetic.main.activity_bathymetry.*
import java.net.URI
import java.net.URISyntaxException

class BathymetryActivity : AppCompatActivity() {

    companion object {
        private const val GEOJSON_SOURCE_ID = "GEOJSON_SOURCE_ID"
        private val LAKE_BOUNDS = LatLngBounds.Builder()
            .include(LatLng(44.936236, -85.673450))
            .include(LatLng(44.932955, -85.669272))
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bathymetry)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { mapboxMap ->
            mapboxMap.setStyle(Style.OUTDOORS) { style ->
                mapboxMap.setLatLngBoundsForCameraTarget(LAKE_BOUNDS)
                style.removeLayer("water-label")
                try {
                    style.addSource(GeoJsonSource(GEOJSON_SOURCE_ID, URI("asset://bathymetry-data.geojson")))
                } catch (e: URISyntaxException) {
                    e.printStackTrace()
                }
                setUpDepthFillLayers(style)
                setUpDepthNumberSymbolLayer(style)



            }

        }

    }

    private fun setUpDepthNumberSymbolLayer(style: Style) {
        val depthNumberSymbolLayer = SymbolLayer("DEPTH_NUMBER_SYMBOL_LAYER_ID", GEOJSON_SOURCE_ID)
        depthNumberSymbolLayer.withProperties(
            PropertyFactory.textField("{depth}"),
            PropertyFactory.textSize(17f),
            PropertyFactory.textColor(Color.WHITE),
            PropertyFactory.textAllowOverlap(true)
        )
        depthNumberSymbolLayer.setFilter(eq(geometryType(), literal("Point")))
        style.addLayerAbove(depthNumberSymbolLayer, "DEPTH_POLYGON_FILL_LAYER_ID")
    }

    private fun setUpDepthFillLayers(style: Style) {
        val depthPolygonFillLayer = FillLayer("DEPTH_POLYGON_FILL_LAYER_ID", GEOJSON_SOURCE_ID)
        depthPolygonFillLayer.withProperties(
            PropertyFactory.fillColor(interpolate(
                linear(), get("depth"),
                stop(5, rgb(16, 158, 210)),
                stop(10, rgb(37, 116, 145)),
                stop(15, rgb(69, 102, 124)),
                stop(30, rgb(31, 52, 67))
            )),
            PropertyFactory.fillOpacity(0.7f)
        )
        depthPolygonFillLayer.setFilter(eq(geometryType(), literal("Polygon")))
        style.addLayer(depthPolygonFillLayer)
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
