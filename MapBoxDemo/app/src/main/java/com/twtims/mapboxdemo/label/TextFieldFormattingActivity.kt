package com.twtims.mapboxdemo.label

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.PropertyValue
import com.twtims.mapboxdemo.R
import kotlinx.android.synthetic.main.activity_text_field_formatting.*


class TextFieldFormattingActivity : AppCompatActivity() {

    companion object {
        private val textFonts = arrayOf("Roboto Black", "Arial Unicode MS Regular")
        private val textSizes = arrayOf(25f, 13f)
        private val textColors = arrayOf("#00ff08", "#ffd43a")
        private const val WATER_RELATED_LAYER = "water-"
        private const val WATER_SHADOW_LAYER_ID = "water-shadow"
    }

    private var mapboxMap: MapboxMap? = null
    private var fontChanged = false
    private var sizeChanged = false
    private var colorChanged = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_field_formatting)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { mapboxMap ->
            this.mapboxMap = mapboxMap
            mapboxMap.setStyle(Style.DARK) {
                Toast.makeText(
                    this, getString(R.string.instruction_toast),
                    Toast.LENGTH_SHORT
                ).show()

            }
        }
        fab_toggle_font.setOnClickListener {
            adjustText(
                PropertyFactory.textFont(
                    if (fontChanged) arrayOf(textFonts[1]) else arrayOf(
                        textFonts[0]
                    )
                )
            )
            fontChanged = !fontChanged
        }

        fab_toggle_text_size.setOnClickListener {
            adjustText(PropertyFactory.textSize(if (sizeChanged) textSizes[1] else textSizes[0]))
            sizeChanged = !sizeChanged
        }

        fab_toggle_text_color.setOnClickListener {
            adjustText(PropertyFactory.textColor(if (colorChanged) textColors[1] else textColors[0]))
            colorChanged = !colorChanged
        }
    }

    private fun adjustText(propertyValue: PropertyValue<*>) {
        if (mapboxMap?.style != null) {
            mapboxMap?.style?.layers?.forEach {
                if (it.id.contains(WATER_RELATED_LAYER) && !it.id.equals(WATER_SHADOW_LAYER_ID)) {
                    it.setProperties(propertyValue)
                }
            }
        } else {
            Toast.makeText(this, R.string.map_style_not_loaded_yet, Toast.LENGTH_SHORT).show();
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
