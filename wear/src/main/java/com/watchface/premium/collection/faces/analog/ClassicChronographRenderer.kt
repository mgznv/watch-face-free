package com.watchface.premium.collection.faces.analog

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.SurfaceHolder
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.style.CurrentUserStyleRepository
import com.watchface.premium.collection.base.BaseWatchFaceRenderer
import com.watchface.premium.collection.base.WatchFaceSharedAssets
import com.watchface.premium.collection.style.ColorScheme
import java.time.ZonedDateTime
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/**
 * Classic Chronograph – vintage chronograph aesthetic.
 * Dark dial, baton indices, sub-dial at 12 o'clock, contrasting second hand.
 */
class ClassicChronographRenderer(
    surfaceHolder: SurfaceHolder,
    currentUserStyleRepository: CurrentUserStyleRepository,
    watchState: WatchState,
    complicationSlotsManager: ComplicationSlotsManager,
    colorSchemes: List<ColorScheme>
) : BaseWatchFaceRenderer(
    surfaceHolder, currentUserStyleRepository, watchState,
    complicationSlotsManager, colorSchemes
) {

    private val subDialPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    private val tickPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.BUTT
    }

    override fun onDraw(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        isAmbient: Boolean,
        colorScheme: ColorScheme
    ) {
        val cx = bounds.exactCenterX()
        val cy = bounds.exactCenterY()
        val radius = min(cx, cy) * 0.88f

        drawTicks(canvas, cx, cy, radius, colorScheme, isAmbient)
        drawSubDial(canvas, cx, cy, radius, colorScheme, isAmbient, zonedDateTime)
        drawHands(canvas, cx, cy, radius, colorScheme, isAmbient, zonedDateTime)
        if (showDate) drawDate(canvas, cx, cy, colorScheme)
    }

    private fun drawTicks(
        canvas: Canvas, cx: Float, cy: Float, radius: Float,
        scheme: ColorScheme, isAmbient: Boolean
    ) {
        tickPaint.color = scheme.primaryColor
        for (i in 0 until 60) {
            val angle = Math.toRadians(i * 6.0)
            val isMajor = i % 5 == 0
            val innerR = if (isMajor) radius * 0.86f else radius * 0.92f
            val strokeW = if (isMajor) (if (isAmbient) 1.5f else 2.5f) else (if (isAmbient) 0.8f else 1.2f)
            tickPaint.strokeWidth = strokeW
            canvas.drawLine(
                cx + cos(angle).toFloat() * innerR,
                cy + sin(angle).toFloat() * innerR,
                cx + cos(angle).toFloat() * radius,
                cy + sin(angle).toFloat() * radius,
                tickPaint
            )
        }
    }

    private fun drawSubDial(
        canvas: Canvas, cx: Float, cy: Float, radius: Float,
        scheme: ColorScheme, isAmbient: Boolean,
        time: ZonedDateTime
    ) {
        // Sub-dial at 12 o'clock position (seconds register)
        val subCx = cx
        val subCy = cy - radius * 0.45f
        val subR = radius * 0.18f

        subDialPaint.color = Color.argb(80, 255, 255, 255)
        canvas.drawCircle(subCx, subCy, subR, subDialPaint)

        if (!isAmbient && showSeconds) {
            // Sub-dial second hand
            val angle = secondAngle(time.second)
            primaryPaint.color = scheme.accentColor
            primaryPaint.strokeWidth = 1.5f
            canvas.drawHand(subCx, subCy, 0.85f, subR, angle, primaryPaint)
        }
    }

    private fun drawHands(
        canvas: Canvas, cx: Float, cy: Float, radius: Float,
        scheme: ColorScheme, isAmbient: Boolean,
        time: ZonedDateTime
    ) {
        // Hour hand
        primaryPaint.color = scheme.primaryColor
        primaryPaint.strokeWidth = if (isAmbient) 4f else 7f
        canvas.drawHand(cx, cy, 0.56f, radius, hourAngle(time.hour, time.minute), primaryPaint)

        // Minute hand
        primaryPaint.strokeWidth = if (isAmbient) 2.5f else 4.5f
        canvas.drawHand(cx, cy, 0.82f, radius, minuteAngle(time.minute, time.second), primaryPaint)

        // Second hand (hidden in ambient)
        if (!isAmbient && showSeconds) {
            secondaryPaint.color = scheme.accentColor
            secondaryPaint.strokeWidth = 1.8f
            canvas.drawHandWithTail(cx, cy, 0.88f, 0.18f, radius, secondAngle(time.second), secondaryPaint)
        }

        // Center cap
        fillPaint.color = scheme.primaryColor
        canvas.drawCircle(cx, cy, if (isAmbient) 4f else 6f, fillPaint)
        fillPaint.color = scheme.backgroundColor
        canvas.drawCircle(cx, cy, if (isAmbient) 2f else 3f, fillPaint)
    }

    private fun drawDate(canvas: Canvas, cx: Float, cy: Float, scheme: ColorScheme) {
        textPaint.color = scheme.secondaryColor
        textPaint.textSize = 20f
        canvas.drawText(
            "${java.time.format.DateTimeFormatter.ofPattern("dd").format(java.time.LocalDate.now())}",
            cx + 50f, cy + 6f, textPaint
        )
    }
}
