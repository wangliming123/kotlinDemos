package com.twtims.mapboxdemo.layer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.RasterLayer
import com.mapbox.mapboxsdk.style.sources.RasterSource
import com.mapbox.mapboxsdk.style.sources.TileSet
import com.twtims.mapboxdemo.R
import kotlinx.android.synthetic.main.activity_add_wms_source.*

class AddWmsSourceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_wms_source)

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { mapboxMap ->
            mapboxMap.setStyle(Style.LIGHT, Style.OnStyleLoaded {
                it.addSource(RasterSource(
                    "web-map-source",
                    TileSet("tileset", "https://geodata.state.nj.us/imagerywms/Natural2015?bbox={\"\n" +
                            "+ \"bbox-epsg-3857}&format=image/png&service=WMS&version=1.1.1&request=GetMap&\"\n" +
                            "+ \"srs=EPSG:3857&width=256&height=256&layers=Natural2015"), 256)
                )
                it.addLayerBelow(RasterLayer(
                    "web-map-layer", "web-map-source"), "aeroway-taxiway")

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
