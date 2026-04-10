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
 * Neon Sport – full-screen digital display with neon glow arc rings.
 * Large bold time, progress arc for seconds, neon accent lines.
 */
class NeonSportRenderer(
    surfaceHolder: SurfaceHolder,
    currentUserStyleRepository: CurrentUserStyleRepository,
    watchState: WatchState,
    complicationSlotsManager: ComplicationSlotsManager,
    colorSchemes: List<ColorScheme>
) : BaseWatchFaceRenderer(
    surfaceHolder, currentUserStyleRepository, watchState,
    complicationSlotsManager, colorSchemes
) {

    private val timeFormatter  = DateTimeFormatter.ofPattern("HH:mm")
    private val dateFormatter  = DateTimeFormatter.ofPattern("EEE dd MMM")
    private val arcPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
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
        val r  = min(cx, cy)

        // Outer neon ring (progress = seconds / 60)
        if (!isAmbient && showSeconds) {
            val sweep = zonedDateTime.second / 60f * 360f
            arcPaint.color = colorScheme.primaryColor
            arcPaint.strokeWidth = 6f
            arcPaint.alpha = 180
            canvas.drawArc(
                RectF(r * 0.08f, r * 0.08f, bounds.width() - r * 0.08f, bounds.height() - r * 0.08f),
                -90f, sweep, false, arcPaint
            )
            // Track arc (dim)
            arcPaint.alpha = 40
            canvas.drawArc(
                RectF(r * 0.08f, r * 0.08f, bounds.width() - r * 0.08f, bounds.height() - r * 0.08f),
                -90f + sweep, 360f - sweep, false, arcPaint
            )
            arcPaint.alpha = 255
        }

        // Inner accent ring
        arcPaint.color = colorScheme.secondaryColor
        arcPaint.strokeWidth = 2f
        arcPaint.alpha = 80
        canvas.drawCircle(cx, cy, r * 0.82f, arcPaint)
        arcPaint.alpha = 255

        // Large time text
        textPaint.color = colorScheme.primaryColor
        textPaint.textSize = r * 0.52f
        textPaint.isFakeBoldText = true
        canvas.drawText(timeFormatter.format(zonedDateTime), cx, cy + textPaint.textSize * 0.35f, textPaint)

        // Date below (if enabled)
        if (showDate) {
            textPaint.color = colorScheme.secondaryColor
            textPaint.textSize = r * 0.14f
            textPaint.isFakeBoldText = false
            canvas.drawText(dateFormatter.format(zonedDateTime), cx, cy + r * 0.55f, textPaint)
        }

        // Neon separator line
        primaryPaint.color = colorScheme.accentColor
        primaryPaint.strokeWidth = 1.5f
        canvas.drawLine(cx - r * 0.3f, cy + r * 0.40f, cx + r * 0.3f, cy + r * 0.40f, primaryPaint)
    }
}
