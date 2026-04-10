package com.watchface.premium.collection.faces.analog

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.SurfaceHolder
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.style.CurrentUserStyleRepository
import com.watchface.premium.collection.base.BaseWatchFaceRenderer
import com.watchface.premium.collection.style.ColorScheme
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/**
 * Minimal Elegance – ultra-clean design, dot indices, extremely thin hands.
 * Inspired by Swiss minimalist watchmaking.
 */
class MinimalEleganceRenderer(
    surfaceHolder: SurfaceHolder,
    currentUserStyleRepository: CurrentUserStyleRepository,
    watchState: WatchState,
    complicationSlotsManager: ComplicationSlotsManager,
    colorSchemes: List<ColorScheme>
) : BaseWatchFaceRenderer(
    surfaceHolder, currentUserStyleRepository, watchState,
    complicationSlotsManager, colorSchemes
) {

    private val dateFormatter = DateTimeFormatter.ofPattern("EEE d")

    override fun onDraw(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        isAmbient: Boolean,
        colorScheme: ColorScheme
    ) {
        val cx = bounds.exactCenterX()
        val cy = bounds.exactCenterY()
        val radius = min(cx, cy) * 0.86f

        drawIndicators(canvas, cx, cy, radius, colorScheme)
        drawHands(canvas, cx, cy, radius, colorScheme, isAmbient, zonedDateTime)
        if (showDate && !isAmbient) drawDate(canvas, cx, cy, colorScheme, zonedDateTime)
    }

    private fun drawIndicators(
        canvas: Canvas, cx: Float, cy: Float, radius: Float, scheme: ColorScheme
    ) {
        fillPaint.color = scheme.secondaryColor
        for (i in 0 until 12) {
            val angle = Math.toRadians(i * 30.0)
            val dotRadius = if (i % 3 == 0) 4f else 2.5f
            val r = radius * 0.93f
            canvas.drawCircle(
                cx + cos(angle).toFloat() * r,
                cy + sin(angle).toFloat() * r,
                dotRadius, fillPaint
            )
        }
    }

    private fun drawHands(
        canvas: Canvas, cx: Float, cy: Float, radius: Float,
        scheme: ColorScheme, isAmbient: Boolean, time: ZonedDateTime
    ) {
        // Hour – thin but slightly wider
        primaryPaint.color = scheme.primaryColor
        primaryPaint.strokeWidth = if (isAmbient) 1.5f else 2.5f
        canvas.drawHand(cx, cy, 0.52f, radius, hourAngle(time.hour, time.minute), primaryPaint)

        // Minute – thinnest
        primaryPaint.strokeWidth = if (isAmbient) 1f else 1.8f
        canvas.drawHand(cx, cy, 0.80f, radius, minuteAngle(time.minute, time.second), primaryPaint)

        // Second – accent color, very thin
        if (!isAmbient && showSeconds) {
            secondaryPaint.color = scheme.accentColor
            secondaryPaint.strokeWidth = 0.8f
            canvas.drawHandWithTail(cx, cy, 0.85f, 0.20f, radius, secondAngle(time.second), secondaryPaint)
        }

        // Center dot – accent
        fillPaint.color = scheme.accentColor
        canvas.drawCircle(cx, cy, if (isAmbient) 2.5f else 3.5f, fillPaint)
    }

    private fun drawDate(
        canvas: Canvas, cx: Float, cy: Float, scheme: ColorScheme, time: ZonedDateTime
    ) {
        textPaint.color = scheme.secondaryColor
        textPaint.textSize = 18f
        canvas.drawText(
            dateFormatter.format(time.toLocalDate()),
            cx, cy + 55f, textPaint
        )
    }
}
