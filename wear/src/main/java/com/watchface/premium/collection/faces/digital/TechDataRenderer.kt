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
 * Tech Data – information-dense layout inspired by developer dashboards.
 * Grid-based layout with time in the centre and data panels in corners.
 */
class TechDataRenderer(
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
    private val dateFormatter = DateTimeFormatter.ofPattern("dd MMM")
    private val dayFormatter  = DateTimeFormatter.ofPattern("EEE")
    private val panelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    private val gridPaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 0.5f
    }

    override fun onDraw(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        isAmbient: Boolean,
        colorScheme: ColorScheme
    ) {
        val w = bounds.width().toFloat()
        val h = bounds.height().toFloat()
        val cx = w / 2
        val cy = h / 2

        if (!isAmbient) drawGrid(canvas, w, h, colorScheme)
        drawCornerPanels(canvas, w, h, colorScheme, isAmbient, zonedDateTime)
        drawCenterTime(canvas, cx, cy, colorScheme, isAmbient, zonedDateTime)
    }

    private fun drawGrid(canvas: Canvas, w: Float, h: Float, scheme: ColorScheme) {
        gridPaint.color = scheme.primaryColor
        gridPaint.alpha = 25
        val thirds = 3
        for (i in 1 until thirds) {
            canvas.drawLine(w * i / thirds, 0f, w * i / thirds, h, gridPaint)
            canvas.drawLine(0f, h * i / thirds, w, h * i / thirds, gridPaint)
        }
        gridPaint.alpha = 255
    }

    private fun drawCornerPanels(
        canvas: Canvas, w: Float, h: Float,
        scheme: ColorScheme, isAmbient: Boolean,
        time: ZonedDateTime
    ) {
        val pad = w * 0.05f
        val panelW = w * 0.28f
        val panelH = h * 0.20f

        if (!isAmbient) {
            panelPaint.color = scheme.primaryColor
            panelPaint.alpha = 18
        }

        val corners = listOf(
            Pair(pad, pad) to scheme.primaryColor,
            Pair(w - pad - panelW, pad) to scheme.secondaryColor,
            Pair(pad, h - pad - panelH) to scheme.accentColor,
            Pair(w - pad - panelW, h - pad - panelH) to scheme.secondaryColor
        )

        corners.forEachIndexed { i, (pos, color) ->
            if (!isAmbient) {
                canvas.drawRoundRect(
                    RectF(pos.first, pos.second, pos.first + panelW, pos.second + panelH),
                    6f, 6f, panelPaint
                )
            }
            textPaint.color = color
            textPaint.textSize = if (isAmbient) 12f else 14f
            textPaint.textAlign = Paint.Align.CENTER
            canvas.drawText(
                listOf("BATT", "HR", "STEPS", "DATE")[i],
                pos.first + panelW / 2,
                pos.second + panelH / 2 + 5f,
                textPaint
            )
        }
        panelPaint.alpha = 255
    }

    private fun drawCenterTime(
        canvas: Canvas, cx: Float, cy: Float,
        scheme: ColorScheme, isAmbient: Boolean, time: ZonedDateTime
    ) {
        // Main time
        textPaint.color = scheme.primaryColor
        textPaint.textSize = cx * 0.52f
        textPaint.isFakeBoldText = true
        canvas.drawText(timeFormatter.format(time), cx, cy + textPaint.textSize * 0.35f, textPaint)

        // Seconds (if interactive)
        if (!isAmbient && showSeconds) {
            textPaint.color = scheme.accentColor
            textPaint.textSize = cx * 0.20f
            textPaint.isFakeBoldText = false
            canvas.drawText(
                ".${time.second.toString().padStart(2, '0')}",
                cx + cx * 0.42f, cy + cx * 0.20f, textPaint
            )
        }

        // Date below
        if (showDate) {
            textPaint.color = scheme.secondaryColor
            textPaint.textSize = cx * 0.15f
            textPaint.isFakeBoldText = false
            canvas.drawText(
                "${dayFormatter.format(time)} · ${dateFormatter.format(time)}",
                cx, cy + cx * 0.60f, textPaint
            )
        }
    }
}
