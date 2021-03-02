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
            ?.addDataValue("JAN", 150, 100)
            ?.addDataValue("FEB", 250, 200)
            ?.addDataValue("MAR", 300, 250)
            ?.addDataValue("APR", 350, 300)
            ?.addDataValue("MAY", 400, 350)
            ?.addDataValue("JUN", 450, 400)
            ?.create()

    }
}