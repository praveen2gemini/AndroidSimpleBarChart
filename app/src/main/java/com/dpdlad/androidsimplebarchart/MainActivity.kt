package com.dpdlad.androidsimplebarchart

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.dpdlad.simplebarchart.BarChartView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet


class MainActivity : AppCompatActivity() {

    private var barChartView: BarChartView? = null
    private var mChart: BarChart? = null
    private var primaryBarColor: Int = com.dpdlad.simplebarchart.R.color.colorPrimaryBar
    private var secondaryBarColor: Int = com.dpdlad.simplebarchart.R.color.colorSecondaryBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        barChartView = findViewById(R.id.symmetricStarViewOriginal1)
        mChart = findViewById(R.id.mpBarChartView)
        barChartView?.dataBuilder()
            ?.setPrimaryBarColor(com.dpdlad.simplebarchart.R.color.colorPrimaryBar)
            ?.setSecondaryBarColor(com.dpdlad.simplebarchart.R.color.colorSecondaryBar)
            ?.setAmountLabelText("(MUR X 1M)")
            ?.setMaximumAmount(450)
            ?.setAmountBoundDifference(150)
            ?.addDataValue("Jan", 150, 100)
            ?.addDataValue("Feb", 200, 250)
            ?.addDataValue("Mar", 300, 250)
            ?.addDataValue("Apr", 350, 300)
            ?.addDataValue("May", 400, 350)
            ?.addDataValue("Jun", 450, 400)
            ?.create()
        loadGroupBarChart()
    }

    private fun loadGroupBarChart() {
        mChart!!.setDrawBarShadow(false)
        mChart!!.description.isEnabled = false
        mChart!!.setPinchZoom(false)
        mChart!!.setDrawGridBackground(true)
        // empty labels so that the names are spread evenly
        val labels = arrayOf("", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "")
        val xAxis = mChart!!.xAxis
        xAxis.setCenterAxisLabels(true)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
//        xAxis.isGranularityEnabled =  false
        xAxis.granularity = 0.75f // only intervals of 1 day
        xAxis.textColor = Color.BLACK
        xAxis.textSize = 12f
        xAxis.axisLineColor = Color.WHITE
        xAxis.axisMinimum = 1f
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)

        val leftAxis = mChart!!.axisLeft
        leftAxis.textColor = Color.BLACK
        leftAxis.textSize = 12f
        leftAxis.axisLineColor = Color.WHITE
        leftAxis.setDrawGridLines(true)
        leftAxis.granularity = 3f
        leftAxis.setLabelCount(4, true)
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        mChart!!.axisRight.isEnabled = false
        mChart!!.legend.isEnabled = true
        mChart!!.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        mChart!!.legend.form = Legend.LegendForm.CIRCLE
        mChart!!.legend.formSize = 12f
        mChart!!.legend.textSize = 12f
        mChart!!.legend.yOffset = 10f
        val valOne = floatArrayOf(10f, 20f, 30f, 40f, 50f)
        val valTwo = floatArrayOf(60f, 50f, 40f, 30f, 20f)

        val barOne: ArrayList<BarEntry> = ArrayList()
        val barTwo: ArrayList<BarEntry> = ArrayList()
        for (i in valOne.indices) {
            barOne.add(BarEntry(i.toFloat(), valOne[i]))
            barTwo.add(BarEntry(i.toFloat(), valTwo[i]))
        }
        val set1 = BarDataSet(barOne, "Incoming")
        set1.color = ContextCompat.getColor(this, primaryBarColor)
        val set2 = BarDataSet(barTwo, "Outgoing")
        set2.color = ContextCompat.getColor(this, secondaryBarColor)
        set1.isHighlightEnabled = false
        set2.isHighlightEnabled = false
        set1.setDrawValues(false)
        set2.setDrawValues(false)
        val dataSets = ArrayList<IBarDataSet>()
        dataSets.add(set1)
        dataSets.add(set2)
//        dataSets.add(set3)
        val data = BarData(dataSets)
        val groupSpace = 0.6f
        val barSpace = 0.05f
        val barWidth = 0.18f
        // (barSpace + barWidth) * 2 + groupSpace = 1
        data.barWidth = barWidth
        // so that the entire chart is shown when scrolled from right to left
        xAxis.axisMaximum = labels.size - 1.1f
        mChart!!.data = data
        mChart!!.setScaleEnabled(false)
        mChart!!.setVisibleXRangeMaximum(6f)
        mChart!!.groupBars(1f, groupSpace, barSpace)


        mChart!!.invalidate()
    }
}