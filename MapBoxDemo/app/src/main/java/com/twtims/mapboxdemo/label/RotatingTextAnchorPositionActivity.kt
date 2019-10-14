package com.twtims.mapboxdemo.label

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.Layer
import com.twtims.mapboxdemo.R
import kotlinx.android.synthetic.main.activity_rotating_text_anchor_position.*
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.PropertyFactory

class RotatingTextAnchorPositionActivity : AppCompatActivity() {

    companion object {
        private val anchorOptions = arrayOf(
            Property.TEXT_ANCHOR_CENTER, // The center of the text is placed closest to the anchor.
            Property.TEXT_ANCHOR_LEFT, // The left side of the text is placed closest to the anchor.
            Property.TEXT_ANCHOR_RIGHT, // The right side of the text is placed closest to the anchor.
            Property.TEXT_ANCHOR_TOP, // The top of the text is placed closest to the anchor.
            Property.TEXT_ANCHOR_BOTTOM, // The bottom of the text is placed closest to the anchor.
            Property.TEXT_ANCHOR_TOP_LEFT, // The top left corner of the text is placed closest to the anchor.
            Property.TEXT_ANCHOR_TOP_RIGHT, // The top right corner of the text is placed closest to the anchor.
            Property.TEXT_ANCHOR_BOTTOM_LEFT, // The bottom left corner of the text is placed closest to the anchor.
            Property.TEXT_ANCHOR_BOTTOM_RIGHT // The bottom right corner of the text is placed closest to the anchor.
        )
        private val anchorDescriptions = arrayOf(
            "The center of the text is placed closest to the anchor.",
            "The left side of the text is placed closest to the anchor.",
            "The right side of the text is placed closest to the anchor.",
            "The top of the text is placed closest to the anchor.",
            "The bottom of the text is placed closest to the anchor.",
            "The top left corner of the text is placed closest to the anchor.",
            "The top right corner of the text is placed closest to the anchor.",
            "The bottom left corner of the text is placed closest to the anchor.",
            "The bottom right corner of the text is placed closest to the anchor."
        )
    }
    private var stateLabelSymbolLayer: Layer? = null
    private var index = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rotating_text_anchor_position)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { mapboxMap ->
            mapboxMap.setStyle(Style.MAPBOX_STREETS) {
                stateLabelSymbolLayer = it.getLayer("state-label")
                stateLabelSymbolLayer?.setProperties(
                    PropertyFactory.textIgnorePlacement(true),
                    PropertyFactory.textAllowOverlap(true)
                )
                setTextView(anchorOptions[index], anchorDescriptions[index])
                switch_anchor_position_fab.setOnClickListener {
                    index++
                    if (index == anchorOptions.size - 1) {
                        index = 0
                    }
                    stateLabelSymbolLayer?.setProperties(PropertyFactory.textAnchor(anchorOptions[index]))
                    setTextView(anchorOptions[index], anchorDescriptions[index])
                }
            }

        }


    }

    private fun setTextView(anchorOption: String, anchorDescription: String) {
        anchor_position_textview.setText(String.format(getString(R.string.position_textview), anchorOption, anchorDescription))
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
