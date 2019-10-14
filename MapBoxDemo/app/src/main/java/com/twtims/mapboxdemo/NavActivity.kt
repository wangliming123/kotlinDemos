package com.twtims.mapboxdemo

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.app.ActivityCompat
import com.twtims.mapboxdemo.dataVisualization.*
import com.twtims.mapboxdemo.displaymap.*
import com.twtims.mapboxdemo.label.*
import com.twtims.mapboxdemo.layer.*
import kotlinx.android.synthetic.main.activity_nav.*

class NavActivity : AppCompatActivity() {

    private var currentMenu = CURRENT_MENU_FIRST

    companion object {
        const val CURRENT_MENU_FIRST = 1//一级菜单
        const val CURRENT_MENU_SECOND = 2//二级菜单

        private val mapArray = arrayOf(
            Nav("Simple Map", MainActivity::class.java),
            Nav("Dynamically MapView", DynamicMapActivity::class.java),
            Nav("Fragment Map", FragmentMapActivity::class.java),
            Nav("Color dependent on zoom level", ZoomDependentFillColorActivity::class.java),
            Nav("Indoor Map", IndoorMapActivity::class.java),
            Nav("Opacity fade", SatelliteOpacityOnZoomActivity::class.java),
            Nav("Multiple text formats", TextFieldMultipleFormatsActivity::class.java),
            Nav("Transparent render surface", TransparentBackgroundActivity::class.java),
            Nav("Use an image source", ImageSourceActivity::class.java)
        )

        private val layerArray = arrayOf(
            Nav("Add a Hill Shade Layer", HillShadeActivity::class.java),
            Nav("Add a new layer below labels", GeojsonLayerInStackActivity::class.java),
            Nav("Add a vector tile source", VectorLayerActivity::class.java),
            Nav("Add a WMS source(WMS地址无法访问)", AddWmsSourceActivity::class.java),
            Nav("Add an image source with time lapse", ImageSourceTimeLapseActivity::class.java),
            Nav("Adjust a layer's opacity", AdjustLayerOpacityActivity::class.java),
            Nav("Change a layer's color", ColorSwitcherActivity::class.java),
            Nav("Show and hide layers", ShowHideLayersActivity::class.java),
            Nav("Variable label placement", VariableLabelPlacementActivity::class.java)
        )

        private val labelArray = arrayOf(
            Nav("Animate marker position", AnimatedMarkerActivity::class.java),
            Nav("Icon update based on API response", SpaceStationLocationActivity::class.java),
            Nav("Change a map's language", LanguageSwitchActivity::class.java),
            Nav("Click to add photo", ClickAddPhotoActivity::class.java),
            Nav("Marker following route", MarkerFollowingRouteActivity::class.java),
            Nav("Symbol layer icons", BasicSymbolLayerActivity::class.java),
            Nav("Style with missing icon", MissingIconActivity::class.java),
            Nav("Pulsing layer opacity", PulsingLayerOpacityColorActivity::class.java),
            Nav("Text anchor position", RotatingTextAnchorPositionActivity::class.java),
            Nav("Adjust text labels", TextFieldFormattingActivity::class.java),
            Nav("SymbolLayer icons", SymbolLayerMapillaryActivity::class.java),
            Nav("Animated icon movement", ValueAnimatorIconAnimationActivity::class.java)
        )
        private val dataVisualizationArray = arrayOf(
            Nav("Styling heat maps", MultipleHeatmapStylingActivity::class.java),
            Nav("Display water depth", BathymetryActivity::class.java),
            Nav("Calendar integration", CalendarIntegrationActivity::class.java),
            Nav("Create a line layer", LineLayerActivity::class.java),
            Nav("Create hotspots from points", CreateHotspotsActivity::class.java),
            Nav("Data time lapse", AddRainFallStyleActivity::class.java)
        )

        val navTabArray = arrayOf(
            NavTab("map", mapArray),
            NavTab("layer", layerArray),
            NavTab("label", labelArray),
            NavTab("Data Visualization", dataVisualizationArray)
        )
    }

    class NavTab(val tabName: String, val navArray: Array<Nav>)

    class Nav(val name: String, val activity: Class<out Activity>)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav)
        initButtons(navTabArray)

        applyPemission()

    }

    private val permissions =
        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)

    private fun applyPemission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, permissions, 1)
        }
    }

    private fun initButtons(navArray: Array<Nav>) {
        currentMenu = CURRENT_MENU_SECOND
        llNavigation.removeAllViews()
        navArray.forEach {
            val button = Button(this)
            button.text = it.name
            button.isAllCaps = false
            button.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            button.setOnClickListener { _ ->
                startActivity(Intent(this, it.activity))
            }
            llNavigation.addView(button)
        }
    }

    private fun initButtons(navTabArray: Array<NavTab>) {
        currentMenu = CURRENT_MENU_FIRST
        llNavigation.removeAllViews()
        navTabArray.forEach {
            val button = Button(this)
            button.text = it.tabName
            button.isAllCaps = false
            button.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            button.setOnClickListener { _ ->
                initButtons(it.navArray)
            }
            llNavigation.addView(button)
        }
    }

    override fun onBackPressed() {
        when (currentMenu) {
            CURRENT_MENU_FIRST -> super.onBackPressed()
            CURRENT_MENU_SECOND -> initButtons(navTabArray)
            else -> super.onBackPressed()
        }
    }


}
