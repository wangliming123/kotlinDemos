package com.twtims.mapboxdemo.layer

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.expressions.Expression
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.twtims.mapboxdemo.R
import kotlinx.android.synthetic.main.activity_variable_label_placement.*
import java.net.URI

class VariableLabelPlacementActivity : AppCompatActivity() {

    companion object {
        private const val GEOJSON_SRC_ID = "poi_source_id"
        private const val POI_LABELS_LAYER_ID = "poi_labels_layer_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_variable_label_placement)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { mapboxMap ->
            val geoJsonSource = GeoJsonSource(GEOJSON_SRC_ID, URI("asset://poi_places.geojson"))
            mapboxMap.setStyle(
                Style.Builder().fromUri(Style.LIGHT).withSource(geoJsonSource)
                    .withLayer(
                        SymbolLayer(POI_LABELS_LAYER_ID, GEOJSON_SRC_ID).withProperties(
                            PropertyFactory.textField(Expression.get("description")),
                            PropertyFactory.textSize(17f),
                            PropertyFactory.textColor(Color.RED),
                            PropertyFactory.textVariableAnchor(
                                arrayOf(
                                    Property.TEXT_ANCHOR_TOP,
                                    Property.TEXT_ANCHOR_BOTTOM,
                                    Property.TEXT_ANCHOR_LEFT,
                                    Property.TEXT_ANCHOR_RIGHT
                                )
                            ),
                            PropertyFactory.textJustify(Property.TEXT_JUSTIFY_AUTO),
                            PropertyFactory.textRadialOffset(0.5f)
                        )
                    )
            ) {
                Toast.makeText(
                    this,
                    getString(R.string.zoom_map_in_and_out_variable_label_instruction),
                    Toast.LENGTH_SHORT
                ).show()
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
