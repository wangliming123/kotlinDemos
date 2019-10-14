package com.twtims.mapboxdemo.label

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.twtims.mapboxdemo.R
import kotlinx.android.synthetic.main.activity_basic_symbol_layer.*

class BasicSymbolLayerActivity : AppCompatActivity() {

    companion object {
        private const val SOURCE_ID = "SOURCE_ID"
        private const val ICON_ID = "ICON_ID"
        private const val LAYER_ID = "LAYER_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic_symbol_layer)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { mapboxMap ->
            val symbolLayerIconFeatureList = mutableListOf<Feature>(
                Feature.fromGeometry(Point.fromLngLat(-57.225365, -33.213144)),
                Feature.fromGeometry(Point.fromLngLat(-54.14164, -33.981818)),
                Feature.fromGeometry(Point.fromLngLat(-56.990533, -30.583266))
            )
            mapboxMap.setStyle(
                Style.Builder().fromUri("mapbox://styles/mapbox/cjf4m44iw0uza2spb3q0a7s41")
                    .withImage(
                        ICON_ID, BitmapFactory.decodeResource(
                            resources, R.mipmap.red_marker
                        )
                    )
                    .withSource(
                        GeoJsonSource(
                            SOURCE_ID,
                            FeatureCollection.fromFeatures(symbolLayerIconFeatureList)
                        )
                    )
                    .withLayer(
                        SymbolLayer(LAYER_ID, SOURCE_ID)
                            .withProperties(
                                PropertyFactory.iconImage(ICON_ID),
                                PropertyFactory.iconAllowOverlap(true),
                                PropertyFactory.iconOffset(arrayOf(0f, 0.9f))
                            )
                    )
            ) {

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
