package com.twtims.mapboxdemo.label

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.expressions.Expression
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.mapboxsdk.utils.BitmapUtils
import com.twtims.mapboxdemo.R
import kotlinx.android.synthetic.main.activity_missing_icon.*

class MissingIconActivity : AppCompatActivity() {

    companion object {
        private const val ICON_SOURCE_ID = "ICON_SOURCE_ID"
        private const val ICON_LAYER_ID = "ICON_LAYER_ID"
        private const val PROFILE_NAME = "PROFILE_NAME"
        private const val CARLOS = "Carlos"
        private const val ANTONY = "Antony"
        private const val MARIA = "Maria"
        private const val LUCIANA = "Luciana"
    }

    private var mapboxMap: MapboxMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_missing_icon)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { mapboxMap ->
            val carlosFeature = Feature.fromGeometry(Point.fromLngLat(-7.9760742, 41.2778064))
            carlosFeature.addStringProperty(PROFILE_NAME, CARLOS)
            val antonyFeature = Feature.fromGeometry(Point.fromLngLat(-8.0639648, 37.5445773))
            antonyFeature.addStringProperty(PROFILE_NAME, ANTONY)
            val mariaFeature = Feature.fromGeometry(Point.fromLngLat(-9.1845703, 38.9764924))
            mariaFeature.addStringProperty(PROFILE_NAME, MARIA)
            val lucianaFeature = Feature.fromGeometry(Point.fromLngLat(-7.5146484, 40.2459915))
            lucianaFeature.addStringProperty(PROFILE_NAME, LUCIANA)

            mapboxMap.setStyle(
                Style.Builder().fromUri(Style.LIGHT)
                    .withSource(
                        GeoJsonSource(
                            ICON_SOURCE_ID,
                            FeatureCollection.fromFeatures(
                                arrayOf(
                                    carlosFeature,
                                    antonyFeature,
                                    mariaFeature,
                                    lucianaFeature
                                )
                            )
                        )
                    )
            ) {
                this.mapboxMap = mapboxMap
                it.addLayer(
                    SymbolLayer(ICON_LAYER_ID, ICON_SOURCE_ID)
                        .withProperties(
                            PropertyFactory.iconImage(Expression.get(PROFILE_NAME)),
                            PropertyFactory.iconIgnorePlacement(true),
                            PropertyFactory.iconAllowOverlap(true),
                            PropertyFactory.textField(Expression.get(PROFILE_NAME)),
                            PropertyFactory.textIgnorePlacement(true),
                            PropertyFactory.textAllowOverlap(true),
                            PropertyFactory.textOffset(arrayOf(0f, 2f))
                        )
                )
            }
        }
        mapView.addOnStyleImageMissingListener { id ->
            when (id) {
                CARLOS -> addImage(id, R.mipmap.carlos)
                ANTONY -> addImage(id, R.mipmap.antony)
                MARIA -> addImage(id, R.mipmap.maria)
                LUCIANA -> addImage(id, R.mipmap.luciana)
                else -> addImage(id, R.mipmap.carlos)
            }
        }
    }

    private fun addImage(id: String, image: Int) {
        val style = mapboxMap?.style
        style?.addImageAsync(id, BitmapUtils.getBitmapFromDrawable(resources.getDrawable(image))!!)
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
