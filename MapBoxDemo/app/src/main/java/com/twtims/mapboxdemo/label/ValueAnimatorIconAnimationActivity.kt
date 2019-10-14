package com.twtims.mapboxdemo.label

import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AccelerateInterpolator
import android.view.animation.BounceInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.mapboxsdk.utils.BitmapUtils
import com.twtims.mapboxdemo.R
import kotlinx.android.synthetic.main.activity_value_animator_icon_animation.*

class ValueAnimatorIconAnimationActivity : AppCompatActivity() {

    companion object {
        private const val ICON_ID = "red-pin-icon-id"
        private const val DEFAULT_DESIRED_ICON_OFFSET = -16f
        private const val STARTING_DROP_HEIGHT = -100f
        private const val DROP_SPEED_MILLISECONDS = 1200L
        private const val SYMBOL_LAYER_ID = "symbol-layer-id"
    }

    private var style: Style? = null
    private var currentSelectedTimeInterpolator: TimeInterpolator = BounceInterpolator()
    private var animator: ValueAnimator? = null
    private var firstRunThrough = true
    private var animationHasStarted = false
    private var pinSymbolLayer: SymbolLayer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_value_animator_icon_animation)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { mapboxMap ->
            mapboxMap.setStyle(
                Style.Builder().fromUri(Style.LIGHT).withSource(
                    GeoJsonSource("source-id", FeatureCollection.fromFeatures(arrayOf(
                        Feature.fromGeometry(Point.fromLngLat(119.86083984375, -1.834403324493515)),
                        Feature.fromGeometry(Point.fromLngLat(116.06637239456177, 5.970619502704659)),
                        Feature.fromGeometry(Point.fromLngLat(114.58740234375, 4.54357027937176)),
                        Feature.fromGeometry(Point.fromLngLat(118.19091796875, 5.134714634014467)),
                        Feature.fromGeometry(Point.fromLngLat(110.36865234374999, 1.4500404973608074)),
                        Feature.fromGeometry(Point.fromLngLat(109.40185546874999, 0.3076157096439005)),
                        Feature.fromGeometry(Point.fromLngLat(115.79589843749999, 1.5159363834516861)),
                        Feature.fromGeometry(Point.fromLngLat(113.291015625, -0.9667509997666298)),
                        Feature.fromGeometry(Point.fromLngLat(116.40083312988281, -0.3392008994314591))
                    )))
                ).withImage(ICON_ID, BitmapUtils.getBitmapFromDrawable(resources.getDrawable(R.mipmap.map_marker_push_pin_pink))!!)
            ){
                this.style = it
                mapView.addOnDidFinishRenderingMapListener {
                    initAnimation(currentSelectedTimeInterpolator)
                    initInterpolatorButtons()
                }
            }

        }
    }

    private fun initInterpolatorButtons() {
        fab_bounce_interpolator.setOnClickListener {
            currentSelectedTimeInterpolator = BounceInterpolator()
            resetIcons()
        }
        fab_linear_interpolator.setOnClickListener {
            currentSelectedTimeInterpolator = LinearInterpolator()
            firstRunThrough = false
            resetIcons()
        }
        fab_accelerate_interpolator.setOnClickListener {
            currentSelectedTimeInterpolator = AccelerateInterpolator()
            firstRunThrough = false
            resetIcons()
        }
        fab_decelerate_interpolator.setOnClickListener {
            currentSelectedTimeInterpolator = DecelerateInterpolator()
            firstRunThrough = false
            resetIcons()
        }
    }

    private fun resetIcons() {
        if (!firstRunThrough) {
            animationHasStarted = false
            style?.removeLayer(SYMBOL_LAYER_ID)
            initAnimation(currentSelectedTimeInterpolator)
        }
    }

    private fun initAnimation(currentSelectedTimeInterpolator: TimeInterpolator) {
        animator?.cancel()
        animator = ValueAnimator.ofFloat(STARTING_DROP_HEIGHT, -17f)
        animator!!.apply {
            duration = DROP_SPEED_MILLISECONDS
            interpolator = currentSelectedTimeInterpolator
            startDelay = 1000
            start()
        }.addUpdateListener {
            if (!animationHasStarted) {
                initSymbolLayer()
                animationHasStarted = true
            }
            pinSymbolLayer?.setProperties(PropertyFactory.iconOffset(arrayOf(0f, it.animatedValue as Float)))
        }
    }

    private fun initSymbolLayer() {
        pinSymbolLayer = SymbolLayer(SYMBOL_LAYER_ID, "source-id")
        pinSymbolLayer!!.setProperties(
            PropertyFactory.iconImage(ICON_ID),
            PropertyFactory.iconIgnorePlacement(true),
            PropertyFactory.iconAllowOverlap(true),
            PropertyFactory.iconOffset(arrayOf(0f, DEFAULT_DESIRED_ICON_OFFSET))
        )
        style?.addLayer(pinSymbolLayer!!)
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
