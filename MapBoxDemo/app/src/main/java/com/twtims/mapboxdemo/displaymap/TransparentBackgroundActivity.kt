package com.twtims.mapboxdemo.displaymap

import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mapbox.mapboxsdk.maps.Style
import com.twtims.mapboxdemo.R
import kotlinx.android.synthetic.main.activity_transparent_background.*
import java.io.*
import java.nio.charset.Charset

class TransparentBackgroundActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transparent_background)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { mapboxMap ->
            try {
                mapboxMap.setStyle(Style.Builder().fromJson(readRawResource(this, R.raw.no_bg_style))) {
                    initVideoView()
                }
            }catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun initVideoView() {
        val path = "android.resource://${getPackageName()}/${R.raw.moving_background_water}"
        videoView.setVideoURI(Uri.parse(path))
        videoView.start()
        videoView.setOnCompletionListener {
            videoView.start()
        }
    }

    @Throws(IOException::class)
    private fun readRawResource(context: Context?, rawResource: Int): String {
        var json = ""
        context?.let {
            val writer = StringWriter()
            val buffer = CharArray(1024)
            val ins = context.resources.openRawResource(rawResource)
            val reader = BufferedReader(InputStreamReader(ins, Charset.forName("UTF-8")))
            var numRead = 0
            numRead = reader.read(buffer)
            while (numRead != -1) {
                writer.write(buffer, 0, numRead)
                numRead = reader.read(buffer)
            }
            json = writer.toString()
        }
        return json
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
        if (videoView != null) {
            videoView.stopPlayback()
        }
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}
