package com.twtims.mapboxdemo.label

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.IntDef
import androidx.cardview.widget.CardView
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.recyclerview.widget.*
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.expressions.Expression
import com.mapbox.mapboxsdk.style.layers.*
import com.mapbox.mapboxsdk.style.sources.*
import com.squareup.picasso.Picasso
import com.twtims.mapboxdemo.R
import kotlinx.android.synthetic.main.activity_symbol_layer_mapillary.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.ref.WeakReference
import java.nio.charset.Charset


class SymbolLayerMapillaryActivity : AppCompatActivity(), MapboxMap.OnMapClickListener {

    companion object {
        private const val SOURCE_ID = "mapbox.poi"
        private const val MAKI_LAYER_ID = "mapbox.poi.maki"
        private const val LOADING_LAYER_ID = "mapbox.poi.loading"
        private const val CALLOUT_LAYER_ID = "mapbox.poi.callout"

        private const val PROPERTY_SELECTED = "selected"
        private const val PROPERTY_LOADING = "loading"
        private const val PROPERTY_LOADING_PROGRESS = "loading_progress"
        private const val PROPERTY_TITLE = "title"
        private const val PROPERTY_FAVOURITE = "favourite"
        private const val PROPERTY_DESCRIPTION = "description"
        private const val PROPERTY_POI = "poi"
        private const val PROPERTY_STYLE = "style"

        private const val CAMERA_ANIMATION_TIME: Long = 1950
        private const val LOADING_CIRCLE_RADIUS = 60f
        private const val LOADING_PROGRESS_STEPS = 25 //number of steps in a progress animation
        private const val LOADING_STEP_DURATION: Long = 50 //duration between each step


        private const val STEP_INITIAL = 0 //duration between each step
        private const val STEP_LOADING = 1 //duration between each step
        private const val STEP_READY = 2 //duration between each step

        private val stepZoomMap =
            mapOf(STEP_INITIAL to 11.0, STEP_LOADING to 13.5, STEP_READY to 18.0)

    }

    private var mapboxMap: MapboxMap? = null
    private var featureCollection: FeatureCollection? = null
    private var source: GeoJsonSource? = null
    private var viewMap: HashMap<String, View>? = null
    private var animatorSet: AnimatorSet? = null

    private var loadMapillaryDataTask: LoadMapillaryDataTask? = null

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(value = [STEP_INITIAL, STEP_LOADING, STEP_READY])
    annotation class ActivityStep

    @ActivityStep
    private var currentStep: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_symbol_layer_mapillary)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { mapboxMap ->
            this.mapboxMap = mapboxMap
            mapboxMap.setStyle(Style.DARK) {
                mapboxMap.uiSettings.isCompassEnabled = false
                mapboxMap.uiSettings.isLogoEnabled = false
                mapboxMap.uiSettings.isAttributionEnabled = false
                LoadPoiDataTask(this).execute()
                mapboxMap.addOnMapClickListener(this)
            }

        }
    }

    override fun onMapClick(point: LatLng): Boolean {
        mapboxMap?.let { mapboxMap ->
            val screenPoint = mapboxMap.projection.toScreenLocation(point)
            val features = mapboxMap.queryRenderedFeatures(screenPoint, CALLOUT_LAYER_ID)
            if (features.isNotEmpty()) {
                val feature = features[0]
                val symbolScreenPoint =
                    mapboxMap.projection.toScreenLocation(convertToLatLng(feature))
                handleClickCallout(feature, screenPoint, symbolScreenPoint)
            } else {
                return handleClickIcon(screenPoint)
            }
        }
        return true
    }

    internal class LoadPoiDataTask constructor(activity: SymbolLayerMapillaryActivity) :
        AsyncTask<Unit, Unit, FeatureCollection>() {
        private val activityRef: WeakReference<SymbolLayerMapillaryActivity> =
            WeakReference(activity)

        override fun doInBackground(vararg params: Unit?): FeatureCollection? {
            val activity = activityRef.get()
            activity?.let {
                val geoJson = loadGeoJsonFromAsset(activity, "sf_poi.geojson")
                return FeatureCollection.fromJson(geoJson)
            }
            return null
        }

        override fun onPostExecute(result: FeatureCollection?) {
            super.onPostExecute(result)
            val activity = activityRef.get()
            if (activity == null || result == null) {
                return
            }
            activity.setupData(result)
            GenerateViewIconTask(activity).execute(result)

        }

        private fun loadGeoJsonFromAsset(context: Context, fileName: String): String {
            try {
                // Load GeoJSON file from local asset folder
                val inputStream = context.assets.open(fileName)
                val size = inputStream.available()
                val buffer = ByteArray(size)
                inputStream.read(buffer)
                inputStream.close()
                return String(buffer, Charset.forName("UTF-8"))
            } catch (exception: Exception) {
                throw RuntimeException(exception)
            }

        }
    }

    class GenerateViewIconTask constructor(
        activity: SymbolLayerMapillaryActivity,
        private val refreshSource: Boolean = false
    ) : AsyncTask<FeatureCollection, Unit, MutableMap<String, Bitmap>?>() {

        private val viewMap = mutableMapOf<String, View>()
        private val activityRef = WeakReference<SymbolLayerMapillaryActivity>(activity)

        override fun doInBackground(vararg params: FeatureCollection): MutableMap<String, Bitmap>? {
            activityRef.get()?.let {
                val imageMap = mutableMapOf<String, Bitmap>()
                val inflater = LayoutInflater.from(it)
                val featureCollection = params[0]

                featureCollection.features()?.forEach { feature ->
                    val view = inflater.inflate(R.layout.mapillary_layout_callout, null)

                    val name = feature.getStringProperty(PROPERTY_TITLE)
                    val tvTitle = view.findViewById<TextView>(R.id.title)
                    tvTitle.text = name

                    val style = feature.getStringProperty(PROPERTY_STYLE)
                    val tvStyle = view.findViewById<TextView>(R.id.style)
                    tvStyle.text = style

                    val favourite = feature.getBooleanProperty(PROPERTY_FAVOURITE)
                    val imageView = view.findViewById<ImageView>(R.id.logoView)
                    imageView.setImageResource(if (favourite) R.drawable.ic_favorite else R.drawable.ic_favorite_border)

                    val bitmap = SymbolGenerator.generate(view)
                    imageMap[name] = bitmap
                    viewMap[name] = view

                }
                return imageMap
            }
            return null
        }

        override fun onPostExecute(result: MutableMap<String, Bitmap>?) {
            super.onPostExecute(result)
            val activity = activityRef.get()
            if (activity != null && result != null) {
                activity.setImageGenResults(viewMap, result)
                if (refreshSource) {
                    activity.refreshSource()
                }
            }
        }

    }

    private fun setImageGenResults(
        viewMap: MutableMap<String, View>,
        imageMap: MutableMap<String, Bitmap>
    ) {
        mapboxMap?.getStyle {
            it.addImages(imageMap as HashMap)
        }
        this.viewMap = viewMap as HashMap<String, View>
    }


    class LoadMapillaryDataTask constructor(
        activity: SymbolLayerMapillaryActivity,
        private val map: MapboxMap?,
        private val picasso: Picasso,
        private val progressHandler: Handler,
        private val feature: Feature
    ) : AsyncTask<Int, Unit, MapillaryDataLoadResult>() {
        companion object {
            private const val URL_IMAGE_PLACEHOLDER =
                "https://d1cuyjsrcm0gby.cloudfront.net/%s/thumb-320.jpg"
            private const val KEY_UNIQUE_FEATURE = "key"
            private const val TOKEN_UNIQUE_FEATURE = "{$KEY_UNIQUE_FEATURE}"
            private const val ID_SOURCE = "cluster_source"
            private const val ID_LAYER_UNCLUSTERED = "unclustered_layer"
            private const val IMAGE_SIZE = 128
            private const val API_URL = ("https://a.mapillary.com/v3/images/"
                    + "?lookat=%f,%f&closeto=%f,%f&radius=%d"
                    + "&client_id=bjgtc1FDTnFPaXpxeTZuUDNabmJ5dzozOGE1ODhkMmEyYTkyZTI4")
        }

        private val activityRef = WeakReference<SymbolLayerMapillaryActivity>(activity)
        private var loadingProgress = 0
        private var loadingIncrease = true

        override fun onPreExecute() {
            super.onPreExecute()
            loadingProgress = 0
            setLoadingState(isLoading = true, isSuccess = false)
        }

        override fun doInBackground(vararg params: Int?): MapillaryDataLoadResult? {
            progressHandler.post(progressRunnable)
            try {
                Thread.sleep(2500)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            val client = OkHttpClient()
            try {
                val poiPosition = feature.geometry() as Point?
                poiPosition?.let {
                    val request = Request.Builder().url(
                        String.format(
                            API_URL,
                            it.longitude(), it.latitude(),
                            it.longitude(), it.latitude(),
                            params[0]
                        )
                    ).build()
                    val response = client.newCall(request).execute()
                    response.body()?.let { responseBody ->
                        val featureCollection = FeatureCollection.fromJson(responseBody.string())
                        val mapillaryDataLoadResult = MapillaryDataLoadResult(featureCollection)
                        featureCollection.features()?.forEach { feature1 ->
                            val imageId = feature1.getStringProperty(KEY_UNIQUE_FEATURE)
                            val imageUrl = String.format(URL_IMAGE_PLACEHOLDER, imageId)
                            var bitmap = picasso.load(imageUrl).resize(IMAGE_SIZE, IMAGE_SIZE).get()
                            bitmap = getCroppedBitmap(bitmap)

                            mapillaryDataLoadResult.add(feature1, bitmap)

                        }
                        return mapillaryDataLoadResult
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: MapillaryDataLoadResult?) {
            super.onPostExecute(result)
            setLoadingState(isLoading = false, isSuccess = true)
            if (result == null) {
                activityRef.get()?.let {
                    Toast.makeText(it, "Error. Unable to load Mapillary data.", Toast.LENGTH_SHORT)
                        .show()
                }
                return
            }

            val featureCollection = result.mapillaryFeatureCollection
            val bitmapMap = result.bitmapHashMap
            bitmapMap.forEach {
                val feature = it.key
                val key = feature.getStringProperty(KEY_UNIQUE_FEATURE)
                map?.style?.addImage(key, it.value)
            }

            val mapillarySource = map?.style?.getSourceAs<GeoJsonSource>(ID_SOURCE)
            if (mapillarySource == null) {
                map?.style?.addSource(
                    GeoJsonSource(
                        ID_SOURCE, featureCollection, GeoJsonOptions()
                            .withCluster(true)
                            .withClusterMaxZoom(17)
                            .withClusterRadius(IMAGE_SIZE / 3)
                    )
                )
                map?.style?.addLayerBelow(
                    SymbolLayer(ID_LAYER_UNCLUSTERED, ID_SOURCE).withProperties(
                        PropertyFactory.iconImage(TOKEN_UNIQUE_FEATURE),
                        PropertyFactory.iconAllowOverlap(true),
                        PropertyFactory.iconSize(
                            Expression.interpolate(
                                Expression.exponential(1f),
                                Expression.zoom(),
                                Expression.stop(12, 0.0f),
                                Expression.stop(15, 0.8f),
                                Expression.stop(16, 1.1f),
                                Expression.stop(17, 1.4f),
                                Expression.stop(18, 1.7f)
                            )
                        )
                    ), MAKI_LAYER_ID
                )

                val layers = Array(3) { IntArray(2) }
                layers[0] = intArrayOf(20, Color.RED)
                layers[1] = intArrayOf(10, Color.BLUE)
                layers[2] = intArrayOf(0, Color.GREEN)

                for (i in layers.indices) {
                    val pointCount = Expression.toNumber(Expression.get("point_count"))
                    val clusterLayer = CircleLayer("cluster-$i", ID_SOURCE)
                    clusterLayer.setProperties(
                        PropertyFactory.circleColor(layers[i][1]),
                        PropertyFactory.circleRadius(
                            Expression.interpolate(
                                Expression.exponential(1f),
                                Expression.zoom(),
                                Expression.stop(12, 10f),
                                Expression.stop(14, 16f),
                                Expression.stop(15, 18f),
                                Expression.stop(16, 20f)
                            )
                        ),
                        PropertyFactory.circleOpacity(0.6f)
                    )
                    clusterLayer.maxZoom = 17f
                    clusterLayer.setFilter(
                        if (i == 0) Expression.gte(
                            pointCount,
                            Expression.literal(layers[i][0])
                        ) else
                            Expression.all(
                                Expression.gte(pointCount, Expression.literal(layers[i][0])),
                                Expression.lt(pointCount, Expression.literal(layers[i - 1][0]))
                            )
                    )
                    map?.style?.addLayerBelow(clusterLayer, MAKI_LAYER_ID)
                }
                val count = SymbolLayer("count", ID_SOURCE)
                count.setProperties(
                    PropertyFactory.textField("{point_count}"),
                    PropertyFactory.textSize(8f),
                    PropertyFactory.textOffset(arrayOf(0.0f, 0.0f)),
                    PropertyFactory.textColor(Color.WHITE),
                    PropertyFactory.textIgnorePlacement(true)
                )
                map?.style?.addLayerBelow(count, MAKI_LAYER_ID)

            } else {
                mapillarySource.setGeoJson(featureCollection)
            }
        }

        private fun getCroppedBitmap(bitmap: Bitmap): Bitmap {
            val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(output)
            val color = 0xff424242.toInt()
            val paint = Paint()
            val rect = Rect(0, 0, bitmap.width, bitmap.height)

            paint.isAntiAlias = true
            canvas.drawARGB(0, 0, 0, 0)
            paint.color = color
            canvas.drawCircle(
                (bitmap.width / 2).toFloat(),
                (bitmap.height / 2).toFloat(), (bitmap.width / 2).toFloat(), paint
            )
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            canvas.drawBitmap(bitmap, rect, rect, paint)
            return output
        }

        private val progressRunnable = object : Runnable {
            override fun run() {
                if (isCancelled) {
                    setLoadingState(isLoading = false, isSuccess = false)
                    return
                }
                if (loadingIncrease) {
                    if (loadingProgress >= LOADING_PROGRESS_STEPS) {
                        loadingIncrease = false
                    }
                } else {
                    if (loadingProgress <= 0) {
                        loadingIncrease = true
                    }
                }
                loadingProgress = if (loadingIncrease) loadingProgress + 1 else loadingProgress - 1

                feature.addNumberProperty(PROPERTY_LOADING_PROGRESS, loadingProgress)
                activityRef.get()?.refreshSource()
                progressHandler.postDelayed(this, LOADING_STEP_DURATION)
            }

        }

        private fun setLoadingState(isLoading: Boolean, isSuccess: Boolean) {
            progressHandler.removeCallbacksAndMessages(null)
            feature.addBooleanProperty(PROPERTY_LOADING, isLoading)
            val activity = activityRef.get()
            activity?.apply {
                refreshSource()
                if (isLoading) {
                    setActivityStep(STEP_LOADING)
                } else if (isSuccess) {
                    setActivityStep(STEP_READY)
                }
            }
        }

    }

    private fun setActivityStep(@ActivityStep activityStep: Int) {
        val selectedFeature = getSelectedFeature()
        val zoom = stepZoomMap.getValue(activityStep)
        selectedFeature?.let {
            animateCameraToSelection(it, zoom)
            currentStep = activityStep
        }

    }

    private fun getSelectedFeature(): Feature? {
        featureCollection?.let { collection ->
            collection.features()?.forEach { feature ->
                if (feature.getBooleanProperty(PROPERTY_SELECTED)) {
                    return feature
                }
            }
        }
        return null
    }

    class MapillaryDataLoadResult constructor(val mapillaryFeatureCollection: FeatureCollection) {
        val bitmapHashMap = HashMap<Feature, Bitmap>().toMutableMap()

        fun add(feature: Feature, bitmap: Bitmap) {
            bitmapHashMap[feature] = bitmap
        }

    }

    private fun setupData(featureCollection: FeatureCollection) {
        mapboxMap?.let {
            this.featureCollection = featureCollection
            it.getStyle { style ->
                source = GeoJsonSource(SOURCE_ID, featureCollection)
                style.addSource(source!!)
                setupMakiLayer(style)
                setupLoadingLayer(style)
                setupCalloutLayer(style)
                setupRecyclerView()
                hideLabelLayers(style)
                setupMapillaryTiles(style)
            }
        }
    }

    private fun setupMapillaryTiles(style: Style) {
        style.addSource(MapillaryTiles.createSource())
        style.addLayerBelow(MapillaryTiles.createLineLayer(), LOADING_LAYER_ID)
    }

    private fun hideLabelLayers(style: Style) {

        style.layers.forEach {
            val id = it.id
            if (id.startsWith("place") || id.startsWith("poi") ||
                id.startsWith("marine") || id.startsWith("road-label")
            ) {
                it.setProperties(PropertyFactory.visibility(Property.NONE))
            }
        }
    }


    private fun setupMakiLayer(style: Style) {
        style.addLayer(
            SymbolLayer(MAKI_LAYER_ID, SOURCE_ID).withProperties(
                PropertyFactory.iconImage("{poi}-15"),
                PropertyFactory.iconAllowOverlap(true),
                PropertyFactory.iconSize(
                    Expression.match(
                        Expression.toString(
                            Expression.get(
                                PROPERTY_SELECTED
                            )
                        ), Expression.literal(1.0f), Expression.stop("true", 1.5f)
                    )
                )
            )
        )
    }

    private fun setupLoadingLayer(style: Style) {
        style.addLayerBelow(
            CircleLayer(LOADING_LAYER_ID, SOURCE_ID).withProperties(
                PropertyFactory.circleRadius(
                    Expression.interpolate(
                        Expression.exponential(1), Expression.get(
                            PROPERTY_LOADING_PROGRESS
                        ), *getLoadingAnimationStops()//*操作符将array转为变长参数（vararg）
                    )
                ),
                PropertyFactory.circleColor(Color.GRAY),
                PropertyFactory.circleOpacity(0.6f)
            ).withFilter(Expression.eq(Expression.get(PROPERTY_LOADING), Expression.literal(true))),
            MAKI_LAYER_ID
        )
    }


    private fun setupCalloutLayer(style: Style) {
        style.addLayer(
            SymbolLayer(CALLOUT_LAYER_ID, SOURCE_ID).withProperties(
                PropertyFactory.iconImage("{title}"),
                PropertyFactory.iconAnchor(Property.ICON_ANCHOR_BOTTOM_LEFT),
                PropertyFactory.iconOffset(arrayOf(-20.0f, -10.0f))
            ).withFilter(Expression.eq(Expression.get(PROPERTY_SELECTED), Expression.literal(true)))
        )
    }

    private fun setupRecyclerView() {
        val adapter = LocationRecyclerViewAdapter(this, featureCollection!!)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_on_top_of_map.layoutManager = layoutManager
        rv_on_top_of_map.itemAnimator = DefaultItemAnimator()
        rv_on_top_of_map.adapter = adapter
        rv_on_top_of_map.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val index = layoutManager.findFirstVisibleItemPosition()
                    setSelected(index, false)
                }
            }
        })
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(rv_on_top_of_map)

    }

    private fun setSelected(index: Int, withScroll: Boolean) {
        if (rv_on_top_of_map.visibility == View.GONE) {
            rv_on_top_of_map.visibility = View.VISIBLE
        }
        deselectAll(false)
        val feature = featureCollection?.features()?.get(index)
        feature?.let {
            it.properties()?.addProperty(PROPERTY_SELECTED, true)
            animateCameraToSelection(it)
            refreshSource()
            loadMapillaryData(feature)
        }

        if (withScroll) {
            rv_on_top_of_map.scrollToPosition(index)
        }
    }

    private fun loadMapillaryData(feature: Feature) {
        loadMapillaryDataTask?.cancel(true)
        loadMapillaryDataTask = LoadMapillaryDataTask(
            this,
            mapboxMap,
            Picasso.with(applicationContext),
            Handler(),
            feature
        )
        loadMapillaryDataTask!!.execute(50)
    }

    private fun animateCameraToSelection(feature: Feature) {
        val zoom = feature.getNumberProperty("zoom").toDouble()
        animateCameraToSelection(feature, zoom)
    }

    private fun animateCameraToSelection(feature: Feature, newZoom: Double) {
        val cameraPosition = mapboxMap?.cameraPosition

        cameraPosition?.let {
            animatorSet?.cancel()

            animatorSet = AnimatorSet()
            animatorSet!!.playTogether(
                createLatLngAnimator(it.target, convertToLatLng(feature)),
                createZoomAnimator(it.zoom, newZoom),
                createBearingAnimator(it.bearing, feature.getNumberProperty("bearing").toDouble()),
                createTiltAnimator(it.tilt, feature.getNumberProperty("tilt").toDouble())
            )
            animatorSet!!.start()
        }

    }

    private fun createTiltAnimator(currentTilt: Double, targetTilt: Double): Animator {
        val tiltAnimator = ValueAnimator.ofFloat(currentTilt.toFloat(), targetTilt.toFloat())
        tiltAnimator.apply {
            duration = CAMERA_ANIMATION_TIME
            interpolator = FastOutSlowInInterpolator()
            addUpdateListener {
                mapboxMap?.moveCamera(CameraUpdateFactory.tiltTo((it.animatedValue as Float).toDouble()))
            }
        }
        return tiltAnimator
    }

    private fun createBearingAnimator(currentBearing: Double, targetBearing: Double): Animator {
        val bearingAnimator =
            ValueAnimator.ofFloat(currentBearing.toFloat(), targetBearing.toFloat())
        bearingAnimator.apply {
            duration = CAMERA_ANIMATION_TIME
            interpolator = FastOutSlowInInterpolator()
            addUpdateListener {
                mapboxMap?.moveCamera(CameraUpdateFactory.bearingTo((it.animatedValue as Float).toDouble()))
            }
        }
        return bearingAnimator
    }

    private fun createZoomAnimator(currentZoom: Double, targetZoom: Double): Animator {
        val zoomAnimator = ValueAnimator.ofFloat(currentZoom.toFloat(), targetZoom.toFloat())
        zoomAnimator.apply {
            duration = CAMERA_ANIMATION_TIME
            interpolator = FastOutSlowInInterpolator()
            addUpdateListener {
                mapboxMap?.moveCamera(CameraUpdateFactory.zoomTo((it.animatedValue as Float).toDouble()))
            }
        }
        return zoomAnimator
    }

    private fun createLatLngAnimator(currentPosition: LatLng, targetPosition: LatLng): Animator {
        val latLngAnimator =
            ValueAnimator.ofObject(LatLngEvaluator(), currentPosition, targetPosition)
        return latLngAnimator.apply {
            duration = CAMERA_ANIMATION_TIME
            interpolator = FastOutSlowInInterpolator()
            addUpdateListener {
                mapboxMap?.moveCamera(CameraUpdateFactory.newLatLng(it.animatedValue as LatLng))
            }
        }

    }

    class LatLngEvaluator : TypeEvaluator<LatLng> {
        private val latLng = LatLng()
        override fun evaluate(fraction: Float, startValue: LatLng?, endValue: LatLng?): LatLng {
            latLng.latitude =
                startValue?.latitude ?: 0.0 +
                        ((endValue?.latitude ?: 0.0) - (startValue?.latitude ?: 0.0)) * fraction
            latLng.longitude = startValue?.longitude ?: 0.0 +
                    ((endValue?.longitude ?: 0.0) - (startValue?.longitude ?: 0.0)) * fraction
            return latLng
        }

    }

    private fun deselectAll(hideRecycler: Boolean) {
        featureCollection!!.features()?.forEach {
            it.properties()!!.addProperty(PROPERTY_SELECTED, false)
        }
        if (hideRecycler) {
            rv_on_top_of_map.visibility = View.GONE
        }
    }

    private fun getLoadingAnimationStops(): Array<Expression.Stop> {
        val stops = mutableListOf<Expression.Stop>()
        for (i in 0..LOADING_PROGRESS_STEPS) {
            stops.add(Expression.stop(i, LOADING_CIRCLE_RADIUS * i / LOADING_PROGRESS_STEPS))
        }
        return stops.toTypedArray()
    }

    private fun refreshSource() {
        if (source != null && featureCollection != null) {
            source?.setGeoJson(featureCollection!!)
        }
    }

    private fun handleClickIcon(screenPoint: PointF): Boolean {
        val features = mapboxMap?.queryRenderedFeatures(screenPoint, MAKI_LAYER_ID)
        features?.let {
            if (it.isNotEmpty()) {
                val title = it[0].getStringProperty(PROPERTY_TITLE)
                val featureList = featureCollection?.features()
                featureList?.forEachIndexed { index, feature ->
                    if (feature.getStringProperty(PROPERTY_TITLE) == title) {
                        setSelected(index, true)
                    }
                }
                return true
            }
        }
        return false

    }

    private fun handleClickCallout(
        feature: Feature,
        screenPoint: PointF,
        symbolScreenPoint: PointF
    ) {
        val view = viewMap?.get(feature.getStringProperty(PROPERTY_TITLE))

        view?.let {
            val textContainer = it.findViewById<View>(R.id.text_container)
            val hitTextRect = Rect()
            textContainer.getHitRect(hitTextRect)
            hitTextRect.offset(symbolScreenPoint.x.toInt(), symbolScreenPoint.y.toInt())
            hitTextRect.offset(0, it.measuredHeight)

            if (hitTextRect.contains(screenPoint.x.toInt(), screenPoint.y.toInt())) {
                val callout = feature.getStringProperty("call-out")
                Toast.makeText(this, callout, Toast.LENGTH_SHORT).show()
            } else {
                val featureList = featureCollection?.features()
                featureList?.forEachIndexed { index, feature1 ->
                    if (feature1.getStringProperty(PROPERTY_TITLE) ==
                        feature.getStringProperty(PROPERTY_TITLE)
                    ) {
                        toggleFavourite(index)
                    }
                }
            }
        }
    }

    private fun convertToLatLng(feature: Feature): LatLng {
        val symbolPoint = feature.geometry() as Point
        return LatLng(symbolPoint.latitude(), symbolPoint.longitude())
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
        loadMapillaryDataTask?.cancel(true)
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
        mapboxMap?.removeOnMapClickListener(this)
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        if (currentStep == STEP_LOADING || currentStep == STEP_READY) {
            loadMapillaryDataTask?.cancel(true)
            setActivityStep(STEP_INITIAL)
            deselectAll(true)
            refreshSource()
        } else {
            super.onBackPressed()
        }
    }


    class LocationRecyclerViewAdapter constructor(
        private val activity: SymbolLayerMapillaryActivity?,
        featureCollection: FeatureCollection
    ) :
        RecyclerView.Adapter<LocationRecyclerViewAdapter.MyViewHolder>() {

        private val featureList = featureCollection.features()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.cardview_symbol_layer, parent, false)
            return MyViewHolder(itemView)
        }

        override fun getItemCount(): Int {
            return featureList?.size ?: 0
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val feature = featureList?.get(position)
            feature?.let {
                holder.title.text = it.getStringProperty(PROPERTY_TITLE)
                holder.description.text = it.getStringProperty(PROPERTY_DESCRIPTION)
                holder.poi.text = it.getStringProperty(PROPERTY_POI)
                holder.style.text = it.getStringProperty(PROPERTY_STYLE)
            }

            holder.itemClickListener = object : ItemClickListener {
                override fun onClick(view: View, position: Int) {
                    activity?.toggleFavourite(position)
                }

            }

        }

        class MyViewHolder constructor(view: View) : RecyclerView.ViewHolder(view),
            View.OnClickListener {

            val title: TextView = view.findViewById(R.id.textview_title)
            val poi: TextView = view.findViewById(R.id.textview_poi)
            val style: TextView = view.findViewById(R.id.textview_style)
            val description: TextView = view.findViewById(R.id.textview_description)
            private val singleCard: CardView = view.findViewById(R.id.single_location_cardview)
            var itemClickListener: ItemClickListener? = null

            init {
                singleCard.setOnClickListener(this)
            }

            override fun onClick(v: View) {
                itemClickListener?.onClick(v, layoutPosition)
            }
        }

        interface ItemClickListener {
            fun onClick(view: View, position: Int)
        }

    }

    private fun toggleFavourite(index: Int) {
        val feature = featureCollection?.features()?.get(index)
        feature?.let {
            val title = it.getStringProperty(PROPERTY_TITLE)
            val currentState = it.getBooleanProperty(PROPERTY_FAVOURITE)
            it.properties()?.addProperty(PROPERTY_FAVOURITE, !currentState)
            viewMap?.get(title)?.let { view ->
                val imageView: ImageView = view.findViewById(R.id.logoView)
                imageView.setImageResource(if (currentState) R.drawable.ic_favorite else R.drawable.ic_favorite_border)
                val bitmap = SymbolGenerator.generate(view)
                mapboxMap?.getStyle { style ->
                    style.addImage(title, bitmap)
                    refreshSource()
                }

            }
        }
    }
}

object SymbolGenerator {
    fun generate(view: View): Bitmap {
        val measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        view.measure(measureSpec, measureSpec)
        val measuredWidth = view.measuredWidth
        val measuredHeight = view.measuredHeight
        view.layout(0, 0, measuredWidth, measuredHeight)
        val bitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
        bitmap.eraseColor(Color.TRANSPARENT)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }
}

object MapillaryTiles {
    private const val ID_SOURCE = "mapillary.source"
    private const val ID_LINE_LAYER = "mapillary.layer.line"
    private const val URL_TILESET = "https://d25uarhxywzl1j.cloudfront.net/v0.1/{z}/{x}/{y}.mvt"

    fun createSource(): Source {
        val mapillaryTileset = TileSet("2.1.0", URL_TILESET)
        mapillaryTileset.maxZoom = 14f
        mapillaryTileset.minZoom = 0f
        return VectorSource(ID_SOURCE, mapillaryTileset)
    }

    fun createLineLayer(): Layer {
        val lineLayer = LineLayer(ID_LINE_LAYER, ID_SOURCE)
        lineLayer.sourceLayer = "mapillary-sequences"
        lineLayer.setProperties(
            PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
            PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
            PropertyFactory.lineOpacity(0.6f),
            PropertyFactory.lineWidth(2.0f),
            PropertyFactory.lineColor(Color.GREEN)
        )
        return lineLayer
    }
}

