package com.twtims.mapboxdemo.layer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngQuad
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.RasterLayer
import com.mapbox.mapboxsdk.style.sources.ImageSource
import com.twtims.mapboxdemo.R
import kotlinx.android.synthetic.main.activity_image_source_time_lapse.*

class ImageSourceTimeLapseActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        private val ID_IMAGE_SOURCE = "animated_image_source"
        private val ID_IMAGE_LAYER = "animated_image_layer"
    }


    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_source_time_lapse)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        mapboxMap.setStyle(Style.DARK, Style.OnStyleLoaded {
            it.addSource(
                ImageSource(
                    ID_IMAGE_SOURCE,
                    LatLngQuad(
                        //30.46     114.39
                        LatLng(31.0, 115.0),
                        LatLng(31.0, 114.0),
                        LatLng(30.0, 114.0),
                        LatLng(30.0, 115.0)
                    ), R.mipmap.southeast_radar_0
                )
            )
            it.addLayer(RasterLayer(
                ID_IMAGE_LAYER,
                ID_IMAGE_SOURCE
            ))
            handler = Handler()
            runnable =
                RefreshImageRunnable(
                    handler,
                    it
                )
            handler.postDelayed(runnable, 100)

        })
    }

    class RefreshImageRunnable(private val handler: Handler, private val loadedMapStyle: Style) : Runnable {

        private val drawables: IntArray
        private var drawableIndex: Int = 0


        init {
            drawables = intArrayOf(
                R.mipmap.southeast_radar_0,
                R.mipmap.southeast_radar_1,
                R.mipmap.southeast_radar_2,
                R.mipmap.southeast_radar_3
            )
            drawableIndex = 1
        }


        override fun run() {
            (loadedMapStyle.getSource(ID_IMAGE_SOURCE) as ImageSource).setImage(drawables[drawableIndex++])
            if (drawableIndex > 3) {
                drawableIndex = 0
            }
            handler.postDelayed(this, 1000)
        }

    }

    public override fun onResume() {
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
