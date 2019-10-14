package com.twtims.mapboxdemo.dataVisualization

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.twtims.mapboxdemo.R
import kotlinx.android.synthetic.main.activity_line_layer.*

class LineLayerActivity : AppCompatActivity() {

    private lateinit var routeCoordinates: MutableList<Point>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_line_layer)
        mapView.onCreate(savedInstanceState)

        mapView.getMapAsync { mapboxMap ->
            mapboxMap.setStyle(Style.OUTDOORS) { style ->
                initRouteCoordinates()
                style.addSource(
                    GeoJsonSource("line-source",
                        FeatureCollection.fromFeatures(
                            arrayOf(
                                Feature.fromGeometry(
                                    LineString.fromLngLats(routeCoordinates)
                                )
                            )
                        )
                    )
                )
                style.addLayer(LineLayer("line-layer", "line-source").withProperties(
                    PropertyFactory.lineDasharray(arrayOf(0.01f, 2f)),
                    PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                    PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
                    PropertyFactory.lineWidth(5f),
                    PropertyFactory.lineColor(Color.parseColor("#e55e5e"))
                ))
            }
        }

    }

    private fun initRouteCoordinates() {
        routeCoordinates = mutableListOf(
            Point.fromLngLat(-118.39439114221236, 33.397676454651766),
            Point.fromLngLat(-118.39421054012902, 33.39769799454838),
            Point.fromLngLat(-118.39408583869053, 33.39761901490136),
            Point.fromLngLat(-118.39388373635917, 33.397328225582285),
            Point.fromLngLat(-118.39372033447427, 33.39728514560042),
            Point.fromLngLat(-118.3930882271826, 33.39756875508861),
            Point.fromLngLat(-118.3928216241072, 33.39759029501192),
            Point.fromLngLat(-118.39227981785722, 33.397234885594564),
            Point.fromLngLat(-118.392021814881, 33.397005125197666),
            Point.fromLngLat(-118.39090810203379, 33.396814854409186),
            Point.fromLngLat(-118.39040499623022, 33.39696563506828),
            Point.fromLngLat(-118.39005669221234, 33.39703025527067),
            Point.fromLngLat(-118.38953208616074, 33.39691896489222),
            Point.fromLngLat(-118.38906338075398, 33.39695127501678),
            Point.fromLngLat(-118.38891287901787, 33.39686511465794),
            Point.fromLngLat(-118.38898167981154, 33.39671074380141),
            Point.fromLngLat(-118.38984598978178, 33.396064537239404),
            Point.fromLngLat(-118.38983738968255, 33.39582400356976),
            Point.fromLngLat(-118.38955358640874, 33.3955978295119),
            Point.fromLngLat(-118.389041880506, 33.39578092284221),
            Point.fromLngLat(-118.38872797688494, 33.3957916930261),
            Point.fromLngLat(-118.38817327048618, 33.39561218978703),
            Point.fromLngLat(-118.3872530598711, 33.3956265500598),
            Point.fromLngLat(-118.38653065153775, 33.39592811523983),
            Point.fromLngLat(-118.38638444985126, 33.39590657490452),
            Point.fromLngLat(-118.38638874990086, 33.395737842093304),
            Point.fromLngLat(-118.38723155962309, 33.395027006653244),
            Point.fromLngLat(-118.38734766096238, 33.394441819579285),
            Point.fromLngLat(-118.38785936686516, 33.39403972556368),
            Point.fromLngLat(-118.3880743693453, 33.393616088784825),
            Point.fromLngLat(-118.38791956755958, 33.39331092541894),
            Point.fromLngLat(-118.3874852625497, 33.39333964672257),
            Point.fromLngLat(-118.38686605540683, 33.39387816940854),
            Point.fromLngLat(-118.38607484627983, 33.39396792286514),
            Point.fromLngLat(-118.38519763616081, 33.39346171215717),
            Point.fromLngLat(-118.38523203655761, 33.393196040109466),
            Point.fromLngLat(-118.3849955338295, 33.393023711860515),
            Point.fromLngLat(-118.38355931726203, 33.39339708930139),
            Point.fromLngLat(-118.38323251349217, 33.39305243325907),
            Point.fromLngLat(-118.3832583137898, 33.39244928189641),
            Point.fromLngLat(-118.3848751324406, 33.39108499551671),
            Point.fromLngLat(-118.38522773650804, 33.38926830725471),
            Point.fromLngLat(-118.38508153482152, 33.38916777794189),
            Point.fromLngLat(-118.38390332123025, 33.39012280171983),
            Point.fromLngLat(-118.38318091289693, 33.38941192035707),
            Point.fromLngLat(-118.38271650753981, 33.3896129783018),
            Point.fromLngLat(-118.38275090793661, 33.38902416443619),
            Point.fromLngLat(-118.38226930238106, 33.3889451769069),
            Point.fromLngLat(-118.38258750605169, 33.388420985121336),
            Point.fromLngLat(-118.38177049662707, 33.388083490107284),
            Point.fromLngLat(-118.38080728551597, 33.38836353925403),
            Point.fromLngLat(-118.37928506795642, 33.38717870977523),
            Point.fromLngLat(-118.37898406448423, 33.3873079646849),
            Point.fromLngLat(-118.37935386875012, 33.38816247841951),
            Point.fromLngLat(-118.37794345248027, 33.387810620840135),
            Point.fromLngLat(-118.37546662390886, 33.38847843095069),
            Point.fromLngLat(-118.37091717142867, 33.39114243958559)
        )

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
