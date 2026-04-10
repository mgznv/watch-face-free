package com.watchface.premium.collection.faces.digital

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.view.SurfaceHolder
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.style.CurrentUserStyleRepository
import com.watchface.premium.collection.base.BaseWatchFaceRenderer
import com.watchface.premium.collection.style.ColorScheme
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.min

/**
 * Health Focus – digital display inspired by health/activity tracking apps.
 * Heart rate icon, large time, activity rings placeholder.
 */
class HealthFocusRenderer(
    surfaceHolder: SurfaceHolder,
    currentUserStyleRepository: CurrentUserStyleRepository,
    watchState: WatchState,
    complicationSlotsManager: ComplicationSlotsManager,
    colorSchemes: List<ColorScheme>
) : BaseWatchFaceRenderer(
    surfaceHolder, currentUserStyleRepository, watchState,
    complicationSlotsManager, colorSchemes
) {

    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    private val dateFormatter = DateTimeFormatter.ofPattern("EEE dd MMM")

    private val arcPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }

    private val heartPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
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
        val r = min(cx, cy)

        // Background
        canvas.drawColor(colorScheme.backgroundColor)

        // Activity ring (outer)
        if (!isAmbient) {
            val sweep = zonedDateTime.second / 60f * 360f
            arcPaint.color = colorScheme.primaryColor
            arcPaint.strokeWidth = 8f
            arcPaint.alpha = 200
            canvas.drawArc(
                RectF(cx - r * 0.75f, cy - r * 0.75f, cx + r * 0.75f, cy + r * 0.75f),
                -90f, sweep, false, arcPaint
            )
            // Track arc
            arcPaint.alpha = 40
            arcPaint.strokeWidth = 8f
            canvas.drawArc(
                RectF(cx - r * 0.75f, cy - r * 0.75f, cx + r * 0.75f, cy + r * 0.75f),
                -90f + sweep, 360f - sweep, false, arcPaint
            )
            arcPaint.alpha = 255
        }

        // Heart icon above time
        heartPaint.color = colorScheme.primaryColor
        val heartSize = r * 0.12f
        canvas.drawCircle(cx, cy - r * 0.55f, heartSize, heartPaint)

        // Large time
        textPaint.color = colorScheme.primaryColor
        textPaint.textSize = r * 0.5f
        textPaint.isFakeBoldText = true
        canvas.drawText(timeFormatter.format(zonedDateTime), cx, cy + textPaint.textSize * 0.35f, textPaint)

        // Date below
        if (showDate) {
            textPaint.color = colorScheme.secondaryColor
            textPaint.textSize = r * 0.14f
            textPaint.isFakeBoldText = false
            canvas.drawText(dateFormatter.format(zonedDateTime), cx, cy + r * 0.5f, textPaint)
        }

        // Accent dot
        primaryPaint.color = colorScheme.accentColor
        canvas.drawCircle(cx + r * 0.55f, cy - r * 0.55f, r * 0.04f, primaryPaint)
    }
}
