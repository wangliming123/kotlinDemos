package com.twtims.mapboxdemo.label

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.twtims.mapboxdemo.R
import com.twtims.mapboxdemo.model.IssModel
import kotlinx.android.synthetic.main.activity_space_station_location.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap

class SpaceStationLocationActivity : AppCompatActivity() {
    companion object {
        val TAG = this::class.java.name
        const val apiCallTime = 2000L
    }

    interface IssApiService {
        @GET("iss-now")
        fun loadLocation(): Call<IssModel>
    }


    private lateinit var call: Call<IssModel>
    private lateinit var mapboxMap: MapboxMap
    private var spaceStationSource : GeoJsonSource? = null
    val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_space_station_location)

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { mapboxMap ->
            mapboxMap.setStyle(Style.SATELLITE_STREETS, Style.OnStyleLoaded {
                this.mapboxMap = mapboxMap
                initSpaceStationSymbolLayer(it)
                callApi()
            })
        }
    }

    private fun initSpaceStationSymbolLayer(style: Style) {
        style.addImage(
            "space-station-icon-id",
            BitmapFactory.decodeResource(resources, R.drawable.iss)
        )
        style.addSource(GeoJsonSource("source-id"))
        style.addLayer(
            SymbolLayer("layer-id", "source-id").withProperties(
                PropertyFactory.iconImage("space-station-icon-id"),
                PropertyFactory.iconIgnorePlacement(true),
                PropertyFactory.iconAllowOverlap(true),
                PropertyFactory.iconSize(.7f)
            )
        )
    }

    private fun callApi() {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://api.open-notify.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(IssApiService::class.java)
        val runnable = object : Runnable {
            override fun run() {
                call = service.loadLocation();
                call.enqueue(object : Callback<IssModel> {
                    override fun onResponse(call: Call<IssModel>, response: Response<IssModel>) {
                        val longitude = response.body()?.issPosition?.longitude
                        val latitude = response.body()?.issPosition?.latitude

                        updateMarkerPosition(latitude?.let { longitude?.let { it1 -> LatLng(it, it1) } }!!)
                    }

                    override fun onFailure(call: Call<IssModel>, throwable: Throwable) {
                        if (throwable.message == null) {
                            Log.e(TAG, "Http connection failed");
                        } else {
                            Log.e(TAG, throwable.message!!);
                        }
                    }
                })
                handler.postDelayed(this,
                    apiCallTime
                );
            }
        }
        handler.post(runnable)
    }

    private fun updateMarkerPosition(position: LatLng) {
        spaceStationSource = mapboxMap.style?.getSourceAs("source-id")
        spaceStationSource?.setGeoJson(FeatureCollection.fromFeature(
            Feature.fromGeometry(Point.fromLngLat(position.longitude, position.latitude))
        ))
        mapboxMap.animateCamera(CameraUpdateFactory.newLatLng(position))

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
