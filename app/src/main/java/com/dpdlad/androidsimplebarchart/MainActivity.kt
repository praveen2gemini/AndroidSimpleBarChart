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
        barChartView?.dataBuilder()
            ?.setPrimaryBarColor(com.dpdlad.simplebarchart.R.color.colorPrimaryBar)
            ?.setSecondaryBarColor(com.dpdlad.simplebarchart.R.color.colorSecondaryBar)
            ?.setAmountLabelText("(MUR X 1M)")
            ?.setMaximumAmount(450)
            ?.addDataValue("Jan", 150, 100)
            ?.addDataValue("Feb", 200, 250)
            ?.addDataValue("Mar", 300, 250)
            ?.addDataValue("Apr", 350, 300)
            ?.addDataValue("May", 400, 350)
            ?.addDataValue("Jun", 450, 400)
            ?.create()

    }
}