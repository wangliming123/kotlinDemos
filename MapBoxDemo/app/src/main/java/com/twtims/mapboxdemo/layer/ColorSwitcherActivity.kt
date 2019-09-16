package com.twtims.mapboxdemo.layer

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.SeekBar
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.FillLayer
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.twtims.mapboxdemo.R
import kotlinx.android.synthetic.main.activity_color_switcher.*

class ColorSwitcherActivity : AppCompatActivity() {

    var building: FillLayer? = null
    var water: FillLayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_switcher)
        initView()
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { mapboxMap ->
            mapboxMap.setStyle(Style.LIGHT, Style.OnStyleLoaded {
                water = it.getLayer("water") as FillLayer?
                building = it.getLayer("building") as FillLayer?
            })
        }
    }

    private fun initView() {
        layerPicker.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (layerPicker.selectedItem.toString() == "Building"){
                    building?.let {
                        redSeekBar.progress = Color.red(it.fillColorAsInt)
                        greenSeekBar.progress = Color.green(it.fillColorAsInt)
                        blueSeekBar.progress = Color.blue(it.fillColorAsInt)
                    }
                } else if (layerPicker.selectedItem.toString() == "Water") {
                    water?.let {
                        redSeekBar.progress = Color.red(it.fillColorAsInt)
                        greenSeekBar.progress = Color.green(it.fillColorAsInt)
                        blueSeekBar.progress = Color.blue(it.fillColorAsInt)
                    }
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {

            }
        }

        redSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (layerPicker.selectedItem.toString() == "Water" && fromUser){
                    water?.setProperties(
                        PropertyFactory.fillColor(Color.rgb(progress, greenSeekBar.progress, blueSeekBar.progress))
                    )
                } else if (layerPicker.selectedItem.toString() == "Building" && fromUser) {
                    building?.setProperties(
                        PropertyFactory.fillColor(Color.rgb(progress, greenSeekBar.progress, blueSeekBar.progress))
                    )
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

        greenSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (layerPicker.selectedItem.toString() == "Water" && fromUser){
                    water?.setProperties(
                        PropertyFactory.fillColor(Color.rgb(redSeekBar.progress, progress, blueSeekBar.progress))
                    )
                } else if (layerPicker.selectedItem.toString() == "Building" && fromUser) {
                    building?.setProperties(
                        PropertyFactory.fillColor(Color.rgb(redSeekBar.progress, progress, blueSeekBar.progress))
                    )
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

        blueSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (layerPicker.selectedItem.toString() == "Water" && fromUser){
                    water?.setProperties(
                        PropertyFactory.fillColor(Color.rgb(redSeekBar.progress, greenSeekBar.progress, progress))
                    )
                } else if (layerPicker.selectedItem.toString() == "Building" && fromUser) {
                    building?.setProperties(
                        PropertyFactory.fillColor(Color.rgb(redSeekBar.progress, greenSeekBar.progress, progress))
                    )
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
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
