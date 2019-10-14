package com.twtims.mapboxdemo.dataVisualization

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.PointF
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.twtims.mapboxdemo.R
import kotlinx.android.synthetic.main.activity_calendar_integration.*
import java.util.*
import android.content.Context
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import com.mapbox.api.geocoding.v5.GeocodingCriteria
import com.mapbox.api.geocoding.v5.MapboxGeocoding
import com.mapbox.api.geocoding.v5.models.GeocodingResponse
import com.mapbox.core.exceptions.ServicesException
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CalendarIntegrationActivity : AppCompatActivity() {

    companion object {
        private const val MY_CAL_REQ = 0
        private const val TITLE_INDEX = 1
        private const val EVENT_LOCATION_INDEX = 2
        private const val MARKER_IMAGE_ID = "MARKER_IMAGE_ID"
        private const val MARKER_LAYER_ID = "MARKER_LAYER_ID"
        private const val PROPERTY_TITLE = "title"
        private const val PROPERTY_LOCATION = "location"
        private const val geojsonSourceId = "geojsonSourceId"
    }

    private var featureList: MutableList<Feature>? = null
    private var featureCollection: FeatureCollection? = null
    private var mapboxMap: MapboxMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_integration)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { mapboxMap ->
            this.mapboxMap = mapboxMap
            mapboxMap.setStyle(Style.MAPBOX_STREETS) { style ->
                featureCollection = FeatureCollection.fromFeatures(arrayOf())
                getCalendarData(style)
                mapboxMap.addOnMapClickListener { point ->
                    handleClickIcon(mapboxMap.projection.toScreenLocation(point))
                }
            }
        }

    }

    private fun handleClickIcon(screenPoint: PointF): Boolean {
        val features = mapboxMap?.queryRenderedFeatures(screenPoint, MARKER_LAYER_ID)
        features?.let {
            if (it.isNotEmpty()) {
                val calendarEventTitle = it[0].getStringProperty(PROPERTY_TITLE)
                val calendarEventLocation = it[0].getStringProperty(PROPERTY_LOCATION)
                Toast.makeText(
                    this,
                    "$calendarEventTitle – $calendarEventLocation",
                    Toast.LENGTH_SHORT
                ).show()
                return true
            }
        }
        return false
    }

    private fun getCalendarData(style: Style) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.READ_CALENDAR), MY_CAL_REQ
            )
        } else {
//            val calendarUri = CalendarContract.CONTENT_URI
            val calendarUri = CalendarContract.Events.CONTENT_URI
            val startTime = Calendar.getInstance()
            startTime.set(2019, 2, 1, 0, 0)
            val endTime = Calendar.getInstance()
            endTime.set(2019, 6, 1, 0, 0)
            val selection =
                "((${CalendarContract.Events.DTSTART} >= ${startTime.timeInMillis}) AND (${CalendarContract.Events.DTSTART} <= ${endTime.timeInMillis}))"

            val projection = arrayOf(
                CalendarContract.Events.CALENDAR_ID,
                CalendarContract.Events.TITLE,
                CalendarContract.Events.EVENT_LOCATION,
                CalendarContract.Events.DTSTART
            )
            val cursor = contentResolver.query(calendarUri, projection, selection, null, null)
            featureList = mutableListOf()
            var index = 0
            cursor?.let {
                if (!deviceHasInternetConnection()) {
                    Toast.makeText(this, R.string.no_connectivity, Toast.LENGTH_LONG).show()
                } else {
                    while (it.moveToNext()) {
                        if (index <= 80) {
                            if (it.getString(EVENT_LOCATION_INDEX) != null
                                && it.getString(EVENT_LOCATION_INDEX).isNotEmpty()
                            ) {
                                makeMapboxGeocodingRequest(
                                    it.getString(TITLE_INDEX),
                                    it.getString(EVENT_LOCATION_INDEX)!!
                                )
                            }
                        }
                        index++
                    }
                }
                setUpData(style)
            }
            cursor?.close()

        }
    }

    private fun setUpData(style: Style) {
        style.addSource(GeoJsonSource(geojsonSourceId, featureCollection))
        val icon = BitmapFactory.decodeResource(this.resources, R.mipmap.calendar_event_icon)
        style.addImage(MARKER_IMAGE_ID, icon)
        setUpCalendarIconLayer(style)
        Toast.makeText(this, R.string.click_on_calendar_icon_instruction, Toast.LENGTH_SHORT).show()
    }

    private fun setUpCalendarIconLayer(style: Style) {
        val eventSymbolLayer = SymbolLayer(MARKER_LAYER_ID, geojsonSourceId)
        eventSymbolLayer.withProperties(
            PropertyFactory.iconImage(MARKER_IMAGE_ID),
            PropertyFactory.iconSize(1.8f),
            PropertyFactory.iconAllowOverlap(true),
            PropertyFactory.iconIgnorePlacement(true)
        )
        style.addLayer(eventSymbolLayer)
    }

    private fun makeMapboxGeocodingRequest(eventTitle: String?, eventLocation: String) {
        try {
            val client = MapboxGeocoding.builder()
                .accessToken(getString(R.string.str_access_token))
                .query(eventLocation)
                .geocodingTypes(GeocodingCriteria.TYPE_ADDRESS)
                .mode(GeocodingCriteria.MODE_PLACES)
                .build()
            client.enqueueCall(object : Callback<GeocodingResponse> {
                override fun onFailure(call: Call<GeocodingResponse>, t: Throwable) {
                    Toast.makeText(
                        this@CalendarIntegrationActivity,
                        "Geocoding Failure: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onResponse(
                    call: Call<GeocodingResponse>,
                    response: Response<GeocodingResponse>
                ) {
                    val results = response.body()?.features()
                    if (results != null && results.size > 0) {
                        val feature = results[0]
                        feature?.let {
                            mapboxMap?.getStyle { style ->
                                val featureLatLng = LatLng(
                                    feature.center()!!.latitude(),
                                    feature.center()!!.longitude()
                                )
                                val singleFeature = Feature.fromGeometry(
                                    Point.fromLngLat(
                                        featureLatLng.longitude,
                                        featureLatLng.latitude
                                    )
                                )
                                singleFeature.addStringProperty(PROPERTY_TITLE, eventTitle)
                                singleFeature.addStringProperty(PROPERTY_LOCATION, eventLocation)
                                featureList!!.add(singleFeature)
                                featureCollection = FeatureCollection.fromFeatures(featureList!!)
                                val source = style.getSourceAs<GeoJsonSource>(geojsonSourceId)
                                source?.setGeoJson(featureCollection)
                            }
                        }
                    }
                }

            })
        } catch (e: ServicesException) {
            Toast.makeText(this, "Error geocoding: $e", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    private fun deviceHasInternetConnection(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected

    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_CAL_REQ) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mapboxMap?.getStyle{
                    getCalendarData(it)
                }
            } else {
                Toast.makeText(
                    this,
                    R.string.user_calendar_permission_explanation,
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
