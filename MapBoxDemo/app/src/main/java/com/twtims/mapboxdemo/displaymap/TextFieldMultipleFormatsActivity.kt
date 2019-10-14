package com.twtims.mapboxdemo.displaymap

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.expressions.Expression
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.twtims.mapboxdemo.R
import kotlinx.android.synthetic.main.activity_text_field_multiple_formats.*

class TextFieldMultipleFormatsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_field_multiple_formats)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { mapboxMap ->
            mapboxMap.setStyle(Style.MAPBOX_STREETS) { style ->
                val bigLabel = Expression.formatEntry(
                    Expression.get("name_en"),
                    Expression.FormatOption.formatFontScale(1.5),
                    Expression.FormatOption.formatTextColor(Color.BLUE),
                    Expression.FormatOption.formatTextFont(
                        arrayOf(
                            "Ubuntu Medium",
                            "Arial Unicode MS Regular"
                        )
                    )
                )
                val newLine = Expression.formatEntry(
                    "\n",
                    Expression.FormatOption.formatFontScale(0.5)
                )

                val smallLabel = Expression.formatEntry(
                    Expression.get("name"),
                    Expression.FormatOption.formatTextColor(Color.parseColor("#d6550a")),
                    Expression.FormatOption.formatTextFont(
                        arrayOf(
                            "Caveat Regular",
                            "Arial Unicode MS Regular"
                        )
                    )
                )

                val format = Expression.format(bigLabel, newLine, smallLabel)

                style.layers.forEach {
                    if (it.id.contains("country-label")){
                        it.setProperties(PropertyFactory.textField(format))
                    }
                }

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
