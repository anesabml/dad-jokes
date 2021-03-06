package com.anesabml.dadjokes.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import com.anesabml.dadjokes.R
import com.anesabml.dadjokes.extension.convertDpToPixel
import kotlin.math.floor
import kotlin.random.Random

class AudioWave @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attributeSet, defStyleAttr, defStyleRes) {

    companion object {
        private const val DEFAULT_BAR_COLOR = Color.WHITE
        private const val DEFAULT_BAR_MIN_HEIGHT = 4
        private const val DEFAULT_BAR_HEIGHT = 48
        private const val DEFAULT_BAR_WIDTH = 24
        private const val DEFAULT_BAR_MARGIN = 2
        private const val DEFAULT_BAR_CORNER_RADIUS = 4
    }

    private var barPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
    }
    private var _width: Int = 0
    private var _height: Int = 0
    private var _barColor = DEFAULT_BAR_COLOR
    private var _barHeight = convertDpToPixel(DEFAULT_BAR_HEIGHT)
    private var _barMinHeight = convertDpToPixel(DEFAULT_BAR_MIN_HEIGHT)
    private var _barWidth = convertDpToPixel(DEFAULT_BAR_WIDTH)
    private var _barMargin = convertDpToPixel(DEFAULT_BAR_MARGIN)
    private var _barCornerRadius = convertDpToPixel(DEFAULT_BAR_CORNER_RADIUS)
    private var _rawData = byteArrayOf()

    var barColor
        get() = _barColor
        set(value) {
            _barColor = value
            barPaint.color = _barColor
            invalidate()
        }

    var barHeight
        get() = _barHeight
        set(value) {
            _barHeight = value
            invalidate()
        }

    var barMinHeight
        get() = _barMinHeight
        set(value) {
            _barMinHeight = value
            invalidate()
        }

    var barWidth
        get() = _barWidth
        set(value) {
            _barWidth = value
            invalidate()
        }

    var barMargin
        get() = _barMargin
        set(value) {
            _barMargin = value
            invalidate()
        }

    var barCornerRadius
        get() = _barCornerRadius
        set(value) {
            _barCornerRadius = value
            invalidate()
        }

    var rawData
        get() = _rawData
        set(value) {
            _rawData = downSample(value, barsCount)
            invalidate()
        }

    private val barStep
        get() = _barMargin + _barWidth

    private val barsCount: Int
        get() = _width / barStep

    private lateinit var barsRectArray: Array<RectF>

    init {
        context.withStyledAttributes(attributeSet, R.styleable.AudioWave) {
            barColor = getColor(R.styleable.AudioWave_AudioWave_barColor, DEFAULT_BAR_COLOR)
            barHeight =
                getDimensionPixelSize(
                    R.styleable.AudioWave_AudioWave_barHeight,
                    DEFAULT_BAR_HEIGHT
                )
            barMinHeight = getDimensionPixelSize(
                R.styleable.AudioWave_AudioWave_barMinHeight,
                DEFAULT_BAR_MIN_HEIGHT
            )
            barWidth =
                getDimensionPixelSize(
                    R.styleable.AudioWave_AudioWave_barWidth,
                    DEFAULT_BAR_WIDTH
                )
            barMargin =
                getDimensionPixelSize(
                    R.styleable.AudioWave_AudioWave_barMargin,
                    DEFAULT_BAR_MARGIN
                )
            barCornerRadius =
                getDimensionPixelSize(
                    R.styleable.AudioWave_AudioWave_barCornerRadius,
                    DEFAULT_BAR_CORNER_RADIUS
                )
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        _width = right - left
        _height = bottom - top
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        var width = paddingStart + paddingEnd
        var height = paddingTop + paddingBottom

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize
        } else {
            width = maxOf(width, suggestedMinimumWidth)
            if (widthMode == MeasureSpec.AT_MOST) {
                width = minOf(widthSize, width)
            }
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize
        } else {
            height += _barHeight

            height = maxOf(height, suggestedMinimumHeight)
            if (heightMode == MeasureSpec.AT_MOST) {
                height = minOf(height, heightSize)
            }
        }

        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (isInEditMode) {
            drawEditMode(canvas)
        } else {
            if (!::barsRectArray.isInitialized) {
                barsRectArray = Array(barsCount) {
                    RectF(0F, 0F, 0F, 0F)
                }
            }
            _rawData.forEachIndexed { i, chunk ->
                val chunkHeight =
                    (chunk.abs().toFloat() / Byte.MAX_VALUE * _barHeight)
                val clampedHeight = maxOf(chunkHeight, _barMinHeight.toFloat())

                canvas.drawRoundRect(
                    barsRectArray[i].apply {
                        left = (_barMargin / 2 + i * barStep).toFloat()
                        top = (_height - _barMinHeight - clampedHeight)
                        right = (_barMargin / 2 + i * barStep + _barWidth).toFloat()
                        bottom = _height.toFloat()
                    },
                    _barCornerRadius.toFloat(),
                    _barCornerRadius.toFloat(),
                    barPaint
                )
            }
        }
    }

    private fun drawEditMode(canvas: Canvas) {
        for (i in 0..barsCount) {
            val clampedHeight = Random.nextInt(0, _barHeight)
            canvas.drawRoundRect(
                RectF(
                    (_barMargin / 2 + i * barStep).toFloat(),
                    (_height - _barMinHeight - clampedHeight).toFloat(),
                    (_barMargin / 2 + i * barStep + _barWidth).toFloat(),
                    _height.toFloat()
                ),
                _barCornerRadius.toFloat(),
                _barCornerRadius.toFloat(),
                barPaint
            )
        }
    }

    private fun downSample(data: ByteArray, targetSize: Int): ByteArray {
        val targetSized = ByteArray(targetSize)
        val chunkSize = data.size / targetSize
        val chunkStep = maxOf(floor((chunkSize / 10.0)), 1.0).toInt()

        var prevDataIndex = 0
        var sampledPerChunk = 0F
        var sumPerChunk = 0F

        if (targetSize >= data.size) {
            for (i in data.indices) {
                targetSized[i] = data[i]
            }
            return targetSized
        }

        for (index in 0..data.size step chunkStep) {
            val currentDataIndex = (targetSize * index.toLong() / data.size).toInt()

            if (prevDataIndex == currentDataIndex) {
                sampledPerChunk += 1
                sumPerChunk += data[index].abs()
            } else {
                targetSized[prevDataIndex] = (sumPerChunk / sampledPerChunk).toInt().toByte()

                sumPerChunk = 0F
                sampledPerChunk = 0F
                prevDataIndex = currentDataIndex
            }
        }

        return targetSized
    }
}

fun Byte.abs(): Byte {
    return when (this) {
        Byte.MIN_VALUE -> Byte.MAX_VALUE
        in (Byte.MIN_VALUE + 1..0) -> (-this).toByte()
        else -> this
    }
}