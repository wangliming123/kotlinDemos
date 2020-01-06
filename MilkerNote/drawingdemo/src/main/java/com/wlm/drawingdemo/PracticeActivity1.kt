package com.wlm.drawingdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import com.wlm.drawingdemo.ext.dp2px
import com.wlm.drawingdemo.view.practice.ColorFilterView
import kotlinx.android.synthetic.main.activity_practice1.*

class PracticeActivity1 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice1)

        addView()
    }

    private fun addView() {
        val colorFilterView = ColorFilterView(this)
        colorFilterView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            dp2px(500)
        )
        layout_practice1.addView(colorFilterView)
    }
}
