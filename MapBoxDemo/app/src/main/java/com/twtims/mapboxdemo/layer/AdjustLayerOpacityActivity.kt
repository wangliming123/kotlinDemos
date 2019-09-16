package com.twtims.mapboxdemo.layer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.Layer
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.RasterLayer
import com.mapbox.mapboxsdk.style.sources.RasterSource
import com.twtims.mapboxdemo.R
import kotlinx.android.synthetic.main.activity_add_wms_source.*
import kotlinx.android.synthetic.main.activity_adjust_layer_opacity.*
import kotlinx.android.synthetic.main.activity_adjust_layer_opacity.mapView


class AdjustLayerOpacityActivity : AppCompatActivity() {

    private var chicago: Layer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adjust_layer_opacity)

        opacitySeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                chicago?.setProperties(
                    PropertyFactory.rasterOpacity(progress.toFloat() / 100)
                )
                val progressPercentage = "$progress%"
                tvOpacityValue.setText(progressPercentage)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync { mapboxMap ->
            mapboxMap.setStyle(Style.LIGHT, Style.OnStyleLoaded {
                it.addSource(RasterSource("chicago-source", "mapbox://mapbox.u8yyzaor"))
                it.addLayer(RasterLayer("chicago", "chicago-source"))
                chicago = mapboxMap.style?.getLayer("chicago")
            })
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
