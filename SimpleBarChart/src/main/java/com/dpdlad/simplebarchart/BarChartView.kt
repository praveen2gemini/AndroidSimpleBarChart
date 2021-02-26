package com.dpdlad.simplebarchart

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import kotlin.math.min


/**
 * @author Praveen Kumar on 12/06/2017
 */
class BarChartView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var chartDataValues: ArrayList<ChartInfo>? = null
    private var maximumAmountRange: Int = DEFAULT_MAX_AMOUNT_BOUND

    /**
     * It provides Default [Paint] object.
     *
     * @return loaded [Paint] instance.
     */
    private fun getDefaultPaint(style: Paint.Style): Paint {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.style = style
        return paint
    }

    /**
     * It provides [Paint] instance for draw an arc with progressive feature.
     *
     * @param color - optional to set according to progressing level.
     * @return - It returns the [Paint] instance to indicating progressing level to user by given value.
     */
    private fun outerCirclePaint(
        @ColorInt color: Int,
        style: Paint.Style = Paint.Style.FILL_AND_STROKE
    ): Paint {
        val paint = getDefaultPaint(style)
        paint.color = color
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeWidth = 5f
        paint.isFilterBitmap = true
        return paint
    }

    private fun getDottedPaint(
        @ColorInt color: Int,
        style: Paint.Style = Paint.Style.STROKE
    ): Paint {
        val paint = getDefaultPaint(style)
        paint.isDither = true
        paint.color = color
//        paint.setARGB(255, 0, 0, 0)
        paint.pathEffect = DashPathEffect(floatArrayOf(5f, 5f), 0f)
//        paint.strokeWidth = 12f
        return paint
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var minimumSize = min(widthMeasureSpec, heightMeasureSpec)
        if (minimumSize <= 0) {
            val res = resources
            minimumSize = res.getDimensionPixelSize(R.dimen.default_chart_height_width)
        }
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val width: Int
        val height: Int

        //Measure Width
        width = when (widthMode) {
            MeasureSpec.EXACTLY -> {
                //Must be this size
                widthSize
            }
            MeasureSpec.AT_MOST -> {
                //Can't be bigger than...
                min(minimumSize, widthSize)
            }
            else -> {
                //Be whatever you want
                minimumSize
            }
        }

        //Measure Height
        height = when (heightMode) {
            MeasureSpec.EXACTLY -> {
                //Must be this size
                heightSize
            }
            MeasureSpec.AT_MOST -> {
                //Can't be bigger than...
                min(minimumSize, heightSize)
            }
            else -> {
                //Be whatever you want
                minimumSize
            }
        }

        //MUST CALL THIS
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(Color.WHITE) // Background color
//        val minSize = Math.min(width, height).toFloat()
//        val half = minSize / 2
        drawAmountBoundaries(canvas)
        drawDottedLine(canvas)
//        drawBarChart(canvas)
        drawBarDynamicChart(canvas)
        drawCrossOnView(canvas) // Just to inspect the view coordinates x,y
        super.onDraw(canvas)
    }

    private fun drawAmountBoundaries(canvas: Canvas) {
        val chartBottomAxis = (screenRectPx.width() * 0.75f)
        val maxBound = maximumAmountRange
        val boundDiff = DEFAULT_AMOUNT_BOUND_DIFF
        val variation = maxBound / boundDiff
        val viewDiff = chartBottomAxis / (variation + 1)
        val lineStartXPoint = (viewDiff * 0.25f)
        val textPaint = TextPaint()
        textPaint.isAntiAlias = true
        textPaint.textSize = 16 * resources.displayMetrics.density
        textPaint.color = ContextCompat.getColor(context, R.color.colorTextAmount)
        chartDataValues?.forEachIndexed { index, chartInfo ->
            val lineAxis =
                chartBottomAxis - (index * viewDiff) - 10 // 10 pixel move away from line
            canvas.drawText((index * boundDiff).toString(), lineStartXPoint, lineAxis, textPaint)
        }
    }

    private fun drawBarDynamicChart(canvas: Canvas) {
        val chartBottomAxis = (screenRectPx.width() * 0.75f)

        val startingPoint = DEFAULT_AMOUNT_BOUND_DIFF
        val barWidth = 15
        val barSpace = 10

        chartDataValues?.forEachIndexed { index, i ->
            val colorBarXAxis = startingPoint.toFloat() + (index * DEFAULT_AMOUNT_BOUND_DIFF)
            val colorBarYAxis = (colorBarXAxis + barWidth)

            val greyedBarXAxis = colorBarYAxis + barSpace
            val greyedBarYAxis = greyedBarXAxis + barWidth

            Log.e("@@@@@@@@", " $i")
            Log.e("@@@@@@@@", " $index")
            canvas.drawRoundRect(
                getRectBar(
                    colorBarXAxis,
                    colorBarYAxis,
                    (chartBottomAxis - i.primaryBarValue.toFloat())
                ),
                DEFAULT_CORNER_RADIUS,
                DEFAULT_CORNER_RADIUS,
                outerCirclePaint(ContextCompat.getColor(context, R.color.colorPrimaryBar))
            )
            canvas.drawRoundRect(
                getRectBar(
                    greyedBarXAxis,
                    greyedBarYAxis,
                    (chartBottomAxis - i.secondaryBarValue.toFloat())
                ),
                DEFAULT_CORNER_RADIUS,
                DEFAULT_CORNER_RADIUS,
                outerCirclePaint(
                    ContextCompat.getColor(context, R.color.colorSecondaryBar)
                )
            )

            if (index == (chartDataValues?.size?.minus(1))) {
                layoutParams =
                    LinearLayout.LayoutParams(
                        colorBarXAxis.toInt() - barWidth,
                        screenRectPx.width()
                    )
            }
        }
    }

    private fun drawBarChart(canvas: Canvas) {

        val chartBottomAxis = (width * 0.75f)
        canvas.drawRoundRect(
            getRectBar(140f, 155f),
            DEFAULT_CORNER_RADIUS,
            DEFAULT_CORNER_RADIUS,
            outerCirclePaint(Color.GREEN)
        )
        canvas.drawRoundRect(
            getRectBar(165f, 180f, chartBottomAxis / 2),
            DEFAULT_CORNER_RADIUS,
            DEFAULT_CORNER_RADIUS,
            outerCirclePaint(Color.DKGRAY)
        )

        canvas.drawRoundRect(
            getRectBar(260f, 275f),
            DEFAULT_CORNER_RADIUS,
            DEFAULT_CORNER_RADIUS,
            outerCirclePaint(ContextCompat.getColor(context, R.color.colorPrimaryBar))
        )
        canvas.drawRoundRect(
            getRectBar(285f, 300f, chartBottomAxis / 3),
            DEFAULT_CORNER_RADIUS,
            DEFAULT_CORNER_RADIUS,
            outerCirclePaint(ContextCompat.getColor(context, R.color.colorSecondaryBar))
        )
    }

    private fun drawCrossOnView(canvas: Canvas) {

        val chartBottomAxis = (screenRectPx.width() * 0.75f)
        val maxBound = maximumAmountRange
        val boundDiff = DEFAULT_AMOUNT_BOUND_DIFF
        val variation = maxBound / boundDiff
        val viewDiff = chartBottomAxis / (variation + 1)
        val paint = getDefaultPaint(Paint.Style.STROKE)
        paint.color = ContextCompat.getColor(context, R.color.colorDottedLine)
        paint.strokeWidth = 2f
//        val chartBottomAxis = (width * 0.75f)

//        canvas.drawLine(
//            screenRectPx.width() * 0.5f,
//            0f,
//            screenRectPx.width() * 0.5f,
//            screenRectPx.width().toFloat(),
//            paint
//        )
        val lineStartPoint = (viewDiff * 0.25f)
        canvas.drawLine(
            lineStartPoint,
            chartBottomAxis,
            screenRectPx.width().toFloat() * 20f,
            chartBottomAxis,
            paint
        )
//        canvas.drawLine(width * 0.5f, 0f, width * 0.5f, height.toFloat(), paint)
//        canvas.drawLine(0f, chartBottomAxis, height.toFloat() * 2f, chartBottomAxis, paint)
    }

    private fun drawDottedLine(canvas: Canvas) {
        val chartBottomAxis = (screenRectPx.width() * 0.75f)
        val maxBound = maximumAmountRange
        val boundDiff = DEFAULT_AMOUNT_BOUND_DIFF
        val variation = maxBound / boundDiff
        val viewDiff = chartBottomAxis / (variation + 1)
        for (x in 1..variation) {
            val lineAxis = chartBottomAxis - (x * viewDiff)
            val lineStartPoint = (viewDiff * 0.25f)
            canvas.drawLine(
                lineStartPoint,
                lineAxis,
                height.toFloat() * 10f,
                lineAxis,
                getDottedPaint(ContextCompat.getColor(context, R.color.colorTextAmount))
            )
        }
    }

    private fun getRectBar(
        left: Float,
        right: Float,
        top: Float = 50f,
        bottom: Float = (screenRectPx.width() * 0.748f)
    ): RectF {
        return RectF(
            left,  // left
            top,  // top
            right,  // right
            bottom // bottom
        )
    }

    internal class ChartInfo {
        internal var monthName: String? = null
        internal var primaryBarValue = 0
        internal var secondaryBarValue = 0

        companion object {
            fun createChartInfo(
                monthName: String?,
                primaryBarValue: Int,
                secondaryBarValue: Int
            ): ChartInfo {
                val chartInfo = ChartInfo()
                chartInfo.monthName = monthName
                chartInfo.primaryBarValue = primaryBarValue
                chartInfo.secondaryBarValue = secondaryBarValue
                return chartInfo
            }
        }
    }

    fun setMaximumAmountRange(maximumAmountRange: Int) {
        this.maximumAmountRange = maximumAmountRange
    }

    inner class DataBuilder {

        init {
            chartDataValues = ArrayList()
        }


        fun setMaximumAmount(maximumAmountRange: Int): DataBuilder? {
            setMaximumAmountRange(maximumAmountRange)
            return this
        }

        fun addDataValue(chartName: String?, data: Int, @ColorInt arcColor: Int): DataBuilder {
            chartDataValues?.add(ChartInfo.createChartInfo(chartName, data, arcColor))
            return this
        }

        fun create() {
            requestLayout()
        }
    }

    fun dataBuilder(): DataBuilder? {
        return DataBuilder()
    }

    companion object {
        //log tag
        private val TAG = BarChartView::class.java.simpleName
        private const val DEFAULT_CORNER_RADIUS = 3f
        private const val DEFAULT_MAX_AMOUNT_BOUND = 450
        private const val DEFAULT_AMOUNT_BOUND_DIFF = 100
    }
}