package com.dpdlad.simplebarchart

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import kotlin.math.min


/**
 * @author Praveen Kumar on 12/06/2017
 */
class BarChartView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

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
    private fun outerCirclePaint(@ColorInt color: Int, style: Paint.Style = Paint.Style.FILL_AND_STROKE): Paint {
        val paint = getDefaultPaint(style)
        paint.color = color
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeWidth = 5f
        paint.isFilterBitmap = true
        return paint
    }

    private fun getDottedPaint(@ColorInt color: Int, style: Paint.Style = Paint.Style.STROKE): Paint {
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
        canvas.drawColor(Color.LTGRAY) // Background color
//        val minSize = Math.min(width, height).toFloat()
//        val half = minSize / 2
        drawAmountBoundaries(canvas)
        drawDottedLine(canvas)
        drawCrossOnView(canvas) // Just to inspect the view coordinates x,y
//        drawBarChart(canvas)
        drawBarDynamicChart(canvas)
        super.onDraw(canvas)
    }

    private fun drawAmountBoundaries(canvas: Canvas) {
        val chartBottomAxis = (screenRectPx.width() * 0.75f)
        val maxBound = DEFAULT_MAX_AMOUNT_BOUND
        val boundDiff = DEFAULT_AMOUNT_BOUND_DIFF
        val variation = maxBound / boundDiff
        val viewDiff = chartBottomAxis / (variation + 1)
        val lineStartXPoint = (viewDiff * 0.25f)
        val text = "test"
        val textPaint = TextPaint()
        textPaint.isAntiAlias = true
        textPaint.textSize = 16 * resources.displayMetrics.density
        textPaint.color = Color.RED
        for (x in 1..variation) {
            val lineAxis = chartBottomAxis - (x * viewDiff) - 10 // 10 pixel move away from line
            canvas.drawText("Text", lineStartXPoint, lineAxis, textPaint)
        }
    }

    private fun drawBarDynamicChart(canvas: Canvas) {
        val chartBottomAxis = (screenRectPx.width() * 0.75f)

        val startingPoint = DEFAULT_AMOUNT_BOUND_DIFF
        val barWidth = 15
        val barSpace = 10
        for (i in 0..15) {
            val colorBarXAxis = startingPoint.toFloat() + (i * DEFAULT_AMOUNT_BOUND_DIFF)
            val colorBarYAxis = (colorBarXAxis + barWidth)

            val greyedBarXAxis = colorBarYAxis + barSpace
            val greyedBarYAxis = greyedBarXAxis + barWidth

            canvas.drawRoundRect(getRectBar(colorBarXAxis, colorBarYAxis), DEFAULT_CORNER_RADIUS, DEFAULT_CORNER_RADIUS, outerCirclePaint(Color.GREEN))
            canvas.drawRoundRect(getRectBar(greyedBarXAxis, greyedBarYAxis, chartBottomAxis / 2), DEFAULT_CORNER_RADIUS, DEFAULT_CORNER_RADIUS, outerCirclePaint(Color.DKGRAY))
            if (i == 15) {
                layoutParams = LinearLayout.LayoutParams(greyedBarYAxis.toInt(), screenRectPx.width())
            }
        }
//        canvas.drawRoundRect(getRectBar(140f, 155f), DEFAULT_CORNER_RADIUS, DEFAULT_CORNER_RADIUS, outerCirclePaint(Color.GREEN))
//        canvas.drawRoundRect(getRectBar(165f, 180f, chartBottomAxis / 2), DEFAULT_CORNER_RADIUS, DEFAULT_CORNER_RADIUS, outerCirclePaint(Color.DKGRAY))
//
//        canvas.drawRoundRect(getRectBar(260f, 275f), DEFAULT_CORNER_RADIUS, DEFAULT_CORNER_RADIUS, outerCirclePaint(Color.GREEN))
//        canvas.drawRoundRect(getRectBar(285f, 300f, chartBottomAxis / 3), DEFAULT_CORNER_RADIUS, DEFAULT_CORNER_RADIUS, outerCirclePaint(Color.DKGRAY))
    }

    private fun drawBarChart(canvas: Canvas) {

        val chartBottomAxis = (width * 0.75f)
        canvas.drawRoundRect(getRectBar(140f, 155f), DEFAULT_CORNER_RADIUS, DEFAULT_CORNER_RADIUS, outerCirclePaint(Color.GREEN))
        canvas.drawRoundRect(getRectBar(165f, 180f, chartBottomAxis / 2), DEFAULT_CORNER_RADIUS, DEFAULT_CORNER_RADIUS, outerCirclePaint(Color.DKGRAY))

        canvas.drawRoundRect(getRectBar(260f, 275f), DEFAULT_CORNER_RADIUS, DEFAULT_CORNER_RADIUS, outerCirclePaint(Color.GREEN))
        canvas.drawRoundRect(getRectBar(285f, 300f, chartBottomAxis / 3), DEFAULT_CORNER_RADIUS, DEFAULT_CORNER_RADIUS, outerCirclePaint(Color.DKGRAY))
    }

    private fun drawCrossOnView(canvas: Canvas) {
        val paint = getDefaultPaint(Paint.Style.STROKE)
        paint.color = Color.RED
        paint.strokeWidth = 2f
//        val chartBottomAxis = (width * 0.75f)
        val chartBottomAxis = (screenRectPx.width()
                * 0.75f)
        canvas.drawLine(screenRectPx.width() * 0.5f, 0f, screenRectPx.width() * 0.5f, screenRectPx.width().toFloat(), paint)
        canvas.drawLine(0f, chartBottomAxis, screenRectPx.width().toFloat() * 2f, chartBottomAxis, paint)
//        canvas.drawLine(width * 0.5f, 0f, width * 0.5f, height.toFloat(), paint)
//        canvas.drawLine(0f, chartBottomAxis, height.toFloat() * 2f, chartBottomAxis, paint)
    }

    private fun drawDottedLine(canvas: Canvas) {
        val chartBottomAxis = (screenRectPx.width() * 0.75f)
        val maxBound = DEFAULT_MAX_AMOUNT_BOUND
        val boundDiff = DEFAULT_AMOUNT_BOUND_DIFF
        val variation = maxBound / boundDiff
        val viewDiff = chartBottomAxis / (variation + 1)
        for (x in 1..variation) {
            val lineAxis = chartBottomAxis - (x * viewDiff)
            val lineStartPoint = (viewDiff * 0.25f)
            canvas.drawLine(lineStartPoint, lineAxis, height.toFloat() * 2f, lineAxis, getDottedPaint(Color.BLUE))
        }
    }

    private fun getRectBar(left: Float, right: Float, top: Float = 50f, bottom: Float = (screenRectPx.width() * 0.75f)): RectF {
        return RectF(
                left,  // left
                top,  // top
                right,  // right
                bottom // bottom
        )
    }

    companion object {
        //log tag
        private val TAG = BarChartView::class.java.simpleName
        private const val DEFAULT_CORNER_RADIUS = 3f
        private const val DEFAULT_MAX_AMOUNT_BOUND = 450
        private const val DEFAULT_AMOUNT_BOUND_DIFF = 150
    }
}