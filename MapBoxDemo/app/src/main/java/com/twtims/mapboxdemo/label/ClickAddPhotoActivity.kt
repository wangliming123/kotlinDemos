package com.twtims.mapboxdemo.label

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngQuad
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.CircleLayer
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.RasterLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.mapboxsdk.style.sources.ImageSource
import com.twtims.mapboxdemo.R
import kotlinx.android.synthetic.main.activity_click_add_photo.*


class ClickAddPhotoActivity : AppCompatActivity(), MapboxMap.OnMapClickListener {

    companion object {
        const val ID_IMAGE_SOURCE = "source-id"
        const val CIRCLE_SOURCE_ID = "circle-source-id"
        const val ID_IMAGE_LAYER = "layer-id"
        const val PHOTO_PICK_CODE = 4
    }

    private var mapboxMap: MapboxMap? = null
    private lateinit var boundsFeatureList: MutableList<Feature>
    private lateinit var boundsCirclePointList: MutableList<Point>
    private var imageCountIndex = 0
    private lateinit var quad: LatLngQuad

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_click_add_photo)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { mapboxMap ->
            this.mapboxMap = mapboxMap
            mapboxMap.setStyle(Style.MAPBOX_STREETS) {
                boundsFeatureList = mutableListOf()
                boundsCirclePointList = mutableListOf()
                mapboxMap.addOnMapClickListener(this)
                imageCountIndex = 0
                it.addSource(
                    GeoJsonSource(
                        CIRCLE_SOURCE_ID,
                        FeatureCollection.fromFeatures(emptyArray<Feature>())
                    )
                )
                it.addLayer(
                    CircleLayer("circle-layer-bounds-corner-id",
                        CIRCLE_SOURCE_ID
                    )
                        .withProperties(
                            PropertyFactory.circleRadius(8f),
                            PropertyFactory.circleColor(Color.parseColor("#d00d43"))
                        )
                )
            }
        }

    }

    override fun onMapClick(point: LatLng): Boolean {
        if (boundsFeatureList.size == 4) {
            boundsFeatureList = mutableListOf()
            boundsCirclePointList = mutableListOf()
        }
        boundsFeatureList.add(
            Feature.fromGeometry(
                Point.fromLngLat(
                    point.longitude,
                    point.latitude
                )
            )
        )
        boundsCirclePointList.add(Point.fromLngLat(point.longitude, point.latitude))
        val style = mapboxMap?.style
        val circleSource = style?.getSourceAs<GeoJsonSource>(CIRCLE_SOURCE_ID)
        circleSource?.setGeoJson(FeatureCollection.fromFeatures(boundsFeatureList))
        if (boundsCirclePointList.size == 4) {
            val latLng1 =
                LatLng(boundsCirclePointList[0].latitude(), boundsCirclePointList[0].longitude())
            val latLng2 =
                LatLng(boundsCirclePointList[1].latitude(), boundsCirclePointList[1].longitude())
            val latLng3 =
                LatLng(boundsCirclePointList[2].latitude(), boundsCirclePointList[2].longitude())
            val latLng4 =
                LatLng(boundsCirclePointList[3].latitude(), boundsCirclePointList[3].longitude())
            quad = LatLngQuad(latLng1, latLng2, latLng3, latLng4)
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            startActivityForResult(intent,
                PHOTO_PICK_CODE
            )
        }
        return true;
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PHOTO_PICK_CODE && resultCode == Activity.RESULT_OK) {
//            if (data == null) {
//                return
//            }
            data?:return
            val imageUri = data.data ?: return
            mapboxMap?.let { mapboxMap ->
                mapboxMap.getStyle {
                    val imageStream = contentResolver.openInputStream(imageUri)
                    val imgBitmap = BitmapFactory.decodeStream(imageStream)
                    it.addSource(
                        ImageSource(ID_IMAGE_SOURCE + imageCountIndex, quad, imgBitmap)
                    )
                    it.addLayer(
                        RasterLayer(ID_IMAGE_LAYER + imageCountIndex, ID_IMAGE_SOURCE + imageCountIndex)
                    )
                    boundsFeatureList = mutableListOf()
                    boundsCirclePointList = mutableListOf()
                    imageCountIndex++

                    val circleSource = it.getSourceAs<GeoJsonSource>(CIRCLE_SOURCE_ID);
                    circleSource?.setGeoJson(FeatureCollection.fromFeatures(boundsFeatureList))
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
