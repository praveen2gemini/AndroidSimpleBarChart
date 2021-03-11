package com.dpdlad.simplebarchart

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.core.content.ContextCompat

class MultiColorThumbSeekBar @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.seekBarStyle
) : AppCompatSeekBar(
    context!!, attrs, defStyleAttr
) {
    private val mThumbSize: Int = resources.getDimensionPixelSize(R.dimen.thumb_size)
    private val mTextPaint = TextPaint()

    @SuppressLint("DrawAllocation")
    @Synchronized
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val progressText = progress.toString()
        val bounds = Rect()
        mTextPaint.getTextBounds(progressText, 0, progressText.length, bounds)
        val leftPadding = paddingLeft - thumbOffset
        val rightPadding = paddingRight - thumbOffset
        val drawWidth = width - leftPadding - rightPadding
        val progressRatio = progress.toFloat() / max
        val thumbOffset = mThumbSize * (.5f - progressRatio)
        val thumbX = progressRatio * drawWidth + leftPadding + thumbOffset
//        val thumbY = height / 2f + bounds.height() / 2f
//        canvas.drawText(progressText, thumbX, thumbY, mTextPaint)
        val seekerLinePaint: Int
        val grayedLeftPadding: Int
        if (thumbX <= ((width * DEFAULT_TEST_PROGRESS) / 100).toFloat()) {
            seekerLinePaint = R.color.colorLimitation
            grayedLeftPadding = 0
        } else {
            seekerLinePaint = R.color.colorUnLimited
            grayedLeftPadding = 33
        }

        changeThumbColor(seekerLinePaint)
        val isHalfPercentage = ((progress / max) * 100 <= 50)
        val progressPercentage = ((width * (progress * 10)) / 100)
        val grayedPercentage = ((width * DEFAULT_TEST_PROGRESS) / 100)

        canvas.drawLine(
            DEFAULT_START_SPACE_X,
            (height * .5f),
            (grayedPercentage + (if (isHalfPercentage) 0 else grayedLeftPadding)).toFloat(),
            (height * .5f),
            getDefaultPaint(if (progressPercentage == 0) R.color.colorLimitationDimmed else R.color.colorLimitation)
        )

        if (grayedPercentage < progressPercentage) {
            val stopX =
                ((width * (progress * 10)) / 100).toFloat() - (if (isHalfPercentage) 0 else rightPadding)
            canvas.drawLine(
                ((width * DEFAULT_TEST_PROGRESS) / 100).toFloat(),
                (height * .5f),
                stopX,
                (height * .5f),
                getDefaultPaint(R.color.colorUnLimited)
            )
        } else {
            val stopX =
                ((width * (10 * 10)) / 100).toFloat() - (if (isHalfPercentage) 0 else rightPadding) - DEFAULT_START_SPACE_X
            canvas.drawLine(
                ((width * DEFAULT_TEST_PROGRESS) / 100).toFloat(),
                (height * .5f),
                stopX,
                (height * .5f),
                getDefaultPaint(R.color.colorUnLimitedDimmed)
            )
        }
//        alpha = if (progressPercentage == 0) 0.5f else 1f
    }

    private fun loadSeekBarChangeListener() {
        super.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                requestLayout()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                Log.e("", "")
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                Log.e("", "")
            }

        })
    }

    private fun changeThumbColor(color: Int) {
        thumb.setTintMode(PorterDuff.Mode.SRC_ATOP)
        thumb.setTint(ContextCompat.getColor(context, color))
    }

    private fun getDefaultPaint(color: Int, style: Paint.Style = Paint.Style.STROKE): Paint {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.style = style
        paint.color = ContextCompat.getColor(context, color)
        paint.strokeWidth = 4f
        return paint
    }

    init {
        mTextPaint.color = Color.WHITE
        mTextPaint.textSize = resources.getDimensionPixelSize(R.dimen.thumb_text_size).toFloat()
        mTextPaint.typeface = Typeface.DEFAULT_BOLD
        mTextPaint.textAlign = Paint.Align.CENTER
        loadSeekBarChangeListener()
    }

    companion object {
        private const val DEFAULT_START_SPACE_X = 33f
        private const val DEFAULT_TEST_PROGRESS = 40
    }
}