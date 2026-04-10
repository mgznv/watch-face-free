package com.watchface.premium.collection.faces.analog

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.view.SurfaceHolder
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.style.CurrentUserStyleRepository
import com.watchface.premium.collection.base.BaseWatchFaceRenderer
import com.watchface.premium.collection.style.ColorScheme
import java.time.ZonedDateTime
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/**
 * Pilot Heritage – aviation instrument styling.
 * Bold Arabic numerals (implied via marks), triangle 12, cathedral hands.
 */
class PilotHeritageRenderer(
    surfaceHolder: SurfaceHolder,
    currentUserStyleRepository: CurrentUserStyleRepository,
    watchState: WatchState,
    complicationSlotsManager: ComplicationSlotsManager,
    colorSchemes: List<ColorScheme>
) : BaseWatchFaceRenderer(
    surfaceHolder, currentUserStyleRepository, watchState,
    complicationSlotsManager, colorSchemes
) {

    private val trianglePath = Path()
    private val handPath = Path()

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

        drawBezel(canvas, cx, cy, radius, colorScheme, isAmbient)
        drawMarkers(canvas, cx, cy, radius, colorScheme, isAmbient)
        drawHands(canvas, cx, cy, radius, colorScheme, isAmbient, zonedDateTime)
    }

    private fun drawBezel(
        canvas: Canvas, cx: Float, cy: Float, radius: Float,
        scheme: ColorScheme, isAmbient: Boolean
    ) {
        primaryPaint.color = scheme.primaryColor
        primaryPaint.strokeWidth = if (isAmbient) 2f else 3f
        primaryPaint.style = Paint.Style.STROKE
        canvas.drawCircle(cx, cy, radius, primaryPaint)
        primaryPaint.style = Paint.Style.STROKE
    }

    private fun drawMarkers(
        canvas: Canvas, cx: Float, cy: Float, radius: Float,
        scheme: ColorScheme, isAmbient: Boolean
    ) {
        fillPaint.color = scheme.primaryColor

        // Triangle at 12 o'clock
        val triSize = if (isAmbient) 6f else 9f
        val triR = radius * 0.88f
        trianglePath.reset()
        trianglePath.moveTo(cx, cy - triR - triSize)
        trianglePath.lineTo(cx - triSize * 0.6f, cy - triR + triSize * 0.2f)
        trianglePath.lineTo(cx + triSize * 0.6f, cy - triR + triSize * 0.2f)
        trianglePath.close()
        canvas.drawPath(trianglePath, fillPaint)

        // Rect marks at 3, 6, 9
        val markW = if (isAmbient) 3f else 5f
        val markH = if (isAmbient) 10f else 16f
        for (i in listOf(3, 6, 9)) {
            val angle = Math.toRadians(i * 30.0 - 90.0)
            val mx = cx + cos(angle).toFloat() * radius * 0.88f
            val my = cy + sin(angle).toFloat() * radius * 0.88f
            canvas.save()
            canvas.rotate(i * 30f, mx, my)
            canvas.drawRect(mx - markW / 2, my - markH / 2, mx + markW / 2, my + markH / 2, fillPaint)
            canvas.restore()
        }

        // Small dots at remaining hours
        for (i in listOf(1, 2, 4, 5, 7, 8, 10, 11)) {
            val angle = Math.toRadians(i * 30.0 - 90.0)
            val r = radius * 0.88f
            canvas.drawCircle(
                cx + cos(angle).toFloat() * r,
                cy + sin(angle).toFloat() * r,
                if (isAmbient) 3f else 4.5f, fillPaint
            )
        }
    }

    private fun drawHands(
        canvas: Canvas, cx: Float, cy: Float, radius: Float,
        scheme: ColorScheme, isAmbient: Boolean, time: ZonedDateTime
    ) {
        // Cathedral-style (broad spade) hour hand – approximated with thick line
        primaryPaint.color = scheme.primaryColor
        primaryPaint.strokeWidth = if (isAmbient) 5f else 9f
        primaryPaint.style = Paint.Style.STROKE
        canvas.drawHand(cx, cy, 0.54f, radius, hourAngle(time.hour, time.minute), primaryPaint)

        // Minute hand
        primaryPaint.strokeWidth = if (isAmbient) 3f else 6f
        canvas.drawHand(cx, cy, 0.80f, radius, minuteAngle(time.minute, time.second), primaryPaint)

        // Second hand – bi-color (main body accent, tail white)
        if (!isAmbient && showSeconds) {
            secondaryPaint.color = scheme.accentColor
            secondaryPaint.strokeWidth = 2f
            canvas.drawHandWithTail(cx, cy, 0.85f, 0.25f, radius, secondAngle(time.second), secondaryPaint)
        }

        // Center boss
        fillPaint.color = scheme.primaryColor
        canvas.drawCircle(cx, cy, if (isAmbient) 5f else 8f, fillPaint)
        fillPaint.color = scheme.backgroundColor
        canvas.drawCircle(cx, cy, if (isAmbient) 2f else 4f, fillPaint)
    }
}
