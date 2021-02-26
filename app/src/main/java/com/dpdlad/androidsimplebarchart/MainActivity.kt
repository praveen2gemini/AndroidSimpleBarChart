package com.dpdlad.androidsimplebarchart

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dpdlad.simplebarchart.BarChartView

class MainActivity : AppCompatActivity() {

    private var barChartView: BarChartView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        barChartView = findViewById(R.id.symmetricStarViewOriginal1)
    }
}