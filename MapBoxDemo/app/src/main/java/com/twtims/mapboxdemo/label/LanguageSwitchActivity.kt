package com.twtims.mapboxdemo.label

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.twtims.mapboxdemo.R
import kotlinx.android.synthetic.main.activity_language_switch.*

class LanguageSwitchActivity : AppCompatActivity() {

    private lateinit var mapboxMap: MapboxMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language_switch)

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { mapboxMap ->
            this.mapboxMap = mapboxMap
            mapboxMap.setStyle(Style.LIGHT) {
                for (layer in it.layers) {
                    Log.d("tag000", layer.id)
                }

                for (source in it.sources) {
                    Log.d("tag000", source.id)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_map_langauge, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val textLayer = mapboxMap.style?.getLayer("country-label")
        item?.let {
            when (it.itemId) {
                R.id.french -> textLayer?.setProperties(PropertyFactory.textField("{name_fr}"))
                R.id.russian -> textLayer?.setProperties(PropertyFactory.textField("{name_ru}"))
                R.id.german -> textLayer?.setProperties(PropertyFactory.textField("{name_de}"))
                R.id.spanish -> textLayer?.setProperties(PropertyFactory.textField("{name_es}"))
                else -> textLayer?.setProperties(PropertyFactory.textField("{name_en}"))
            }
        }


        return super.onOptionsItemSelected(item)
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
