package com.dpdlad.androidsimplebarchart

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.dpdlad.simplebarchart.BarChartView
import com.dpdlad.simplebarchart.MultiColorThumbSeekBar


class MainActivity : AppCompatActivity() {

    private var barChartView: BarChartView? = null
    private var multiColorThumbSeekBarView: MultiColorThumbSeekBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        barChartView = findViewById(R.id.symmetricStarViewOriginal1)
        multiColorThumbSeekBarView = findViewById(R.id.textThumbSeekBar)
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
        findAnagram()
    }

    private fun findAnagram() {
        val anagramList = ArrayList<String>()
        anagramList.add("dog")
        anagramList.add("cat")
        anagramList.add("god")
        anagramList.forEachIndexed { index, content ->
            val singleItem = content.toCharArray()
            Log.e("************", "SELECTED ITEM $content")
            anagramList.forEachIndexed { index, content ->
                var isAnagramDeteted = false
                singleItem.forEach singleItem@{ c ->
                    isAnagramDeteted = content.contains(c)
                    if (!isAnagramDeteted) {
                        return@singleItem
                    }
                }
                if (isAnagramDeteted) {
                    Log.e(
                        "************",
                        "found anagram $content and singleItem->${singleItem[0]}${singleItem[1]}${singleItem[2]}"
                    )
                } else {
                    Log.e("************", "found Non-anagram $content")
                }
            }
        }
    }
}