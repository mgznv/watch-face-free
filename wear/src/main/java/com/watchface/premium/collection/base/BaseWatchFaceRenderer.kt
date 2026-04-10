package com.watchface.premium.collection.base

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.SurfaceHolder
import androidx.wear.watchface.CanvasType
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.DrawMode
import androidx.wear.watchface.Renderer
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.style.CurrentUserStyleRepository
import androidx.wear.watchface.style.UserStyle
import androidx.wear.watchface.style.UserStyleSetting.BooleanUserStyleSetting.BooleanOption
import androidx.wear.watchface.style.UserStyleSetting.ListUserStyleSetting.ListOption
import androidx.wear.watchface.style.WatchFaceLayer
import com.watchface.premium.collection.style.ColorScheme
import com.watchface.premium.collection.style.StyleIds
import java.time.ZonedDateTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/**
 * Base renderer for all 20 watch faces.
 *
 * Handles:
 *  - style observation (color scheme, show-seconds, show-date)
 *  - background fill
 *  - complication rendering
 *  - ambient vs interactive switching
 *
 * Subclasses implement [onDraw] with their unique visual logic.
 */
abstract class BaseWatchFaceRenderer(
    surfaceHolder: SurfaceHolder,
    private val currentUserStyleRepository: CurrentUserStyleRepository,
    watchState: WatchState,
    protected val complicationSlotsManager: ComplicationSlotsManager,
    private val colorSchemes: List<ColorScheme>
) : Renderer.CanvasRenderer2<WatchFaceSharedAssets>(
    surfaceHolder = surfaceHolder,
    currentUserStyleRepository = currentUserStyleRepository,
    watchState = watchState,
    canvasType = CanvasType.HARDWARE,
    interactiveDrawModeUpdateDelayMillis = 16L,
    clearWithBackgroundTintBeforeRenderingHighlightLayer = false
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    // Current style values (updated from Flow)
    protected var currentColorScheme: ColorScheme = colorSchemes.first()
        private set
    protected var showSeconds: Boolean = true
        private set
    protected var showDate: Boolean = true
        private set

    // Pre-allocated paints – subclasses may reconfigure colors in onDraw
    protected val backgroundPaint = Paint().apply { style = Paint.Style.FILL }
    protected val primaryPaint    = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style      = Paint.Style.STROKE
        strokeCap  = Paint.Cap.ROUND
    }
    protected val secondaryPaint  = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style      = Paint.Style.STROKE
        strokeCap  = Paint.Cap.ROUND
    }
    protected val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
    }
    protected val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    init {
        scope.launch {
            currentUserStyleRepository.userStyle.collect { style ->
                applyStyle(style)
                invalidate()
            }
        }
    }

    private fun applyStyle(style: UserStyle) {
        val schema = currentUserStyleRepository.schema

        // Color scheme
        val colorSetting = schema.userStyleSettings.firstOrNull { it.id.value == StyleIds.COLOR_SCHEME }
        if (colorSetting != null) {
            val selectedId = (style[colorSetting] as? ListOption)?.id?.value
            currentColorScheme = colorSchemes.firstOrNull { it.id == selectedId } ?: colorSchemes.first()
        }

        // Show seconds
        val secondsSetting = schema.userStyleSettings.firstOrNull { it.id.value == StyleIds.SHOW_SECONDS }
        if (secondsSetting != null) {
            showSeconds = (style[secondsSetting] as? BooleanOption)?.value ?: true
        }

        // Show date
        val dateSetting = schema.userStyleSettings.firstOrNull { it.id.value == StyleIds.SHOW_DATE }
        if (dateSetting != null) {
            showDate = (style[dateSetting] as? BooleanOption)?.value ?: true
        }
    }

    // ----- CanvasRenderer2 contract --------------------------------------------

    override suspend fun createSharedAssets(): WatchFaceSharedAssets = WatchFaceSharedAssets()

    override fun render(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        sharedAssets: WatchFaceSharedAssets
    ) {
        val isAmbient = renderParameters.drawMode == DrawMode.AMBIENT

        // Pick ambient or interactive color scheme
        val scheme = if (isAmbient) {
            currentColorScheme.copy(
                primaryColor     = currentColorScheme.ambientPrimaryColor,
                secondaryColor   = currentColorScheme.ambientPrimaryColor,
                accentColor      = currentColorScheme.ambientPrimaryColor,
                backgroundColor  = currentColorScheme.ambientBackgroundColor
            )
        } else {
            currentColorScheme
        }

        // Background
        backgroundPaint.color = scheme.backgroundColor
        canvas.drawRect(bounds, backgroundPaint)

        // Watch face specific drawing
        onDraw(canvas, bounds, zonedDateTime, isAmbient, scheme)

        // Complications (only when the complications layer is requested)
        if (renderParameters.watchFaceLayers.contains(WatchFaceLayer.COMPLICATIONS)) {
            for ((_, slot) in complicationSlotsManager.complicationSlots) {
                if (slot.enabled) {
                    slot.render(canvas, zonedDateTime, renderParameters)
                }
            }
        }
    }

    override fun renderHighlightLayer(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        sharedAssets: WatchFaceSharedAssets
    ) {
        for ((_, slot) in complicationSlotsManager.complicationSlots) {
            if (slot.enabled) {
                slot.renderHighlightLayer(canvas, zonedDateTime, renderParameters)
            }
        }
    }

    /**
     * Subclasses draw their unique face here.
     *
     * @param canvas       The hardware-accelerated canvas to draw on.
     * @param bounds       The full surface bounds (origin at 0,0).
     * @param zonedDateTime The current time with timezone.
     * @param isAmbient    True when the watch is in always-on / ambient mode.
     * @param colorScheme  Pre-resolved color scheme (ambient-adjusted if [isAmbient]).
     */
    abstract fun onDraw(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        isAmbient: Boolean,
        colorScheme: ColorScheme
    )

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }

    // ----- Utility helpers used by all renderers --------------------------------

    /** Hour angle in degrees, starting from 12 o'clock, clockwise. */
    protected fun hourAngle(hour: Int, minute: Int): Double =
        Math.toRadians(((hour % 12) * 30.0 + minute * 0.5) - 90.0)

    /** Minute angle in degrees. */
    protected fun minuteAngle(minute: Int, second: Int): Double =
        Math.toRadians((minute * 6.0 + second * 0.1) - 90.0)

    /** Second angle in degrees. */
    protected fun secondAngle(second: Int): Double =
        Math.toRadians((second * 6.0) - 90.0)

    /** Draw a clock hand from the center outward. */
    protected fun Canvas.drawHand(
        cx: Float, cy: Float,
        lengthFraction: Float, radius: Float,
        angleDegrees: Double, paint: Paint
    ) {
        val x = cx + Math.cos(angleDegrees).toFloat() * radius * lengthFraction
        val y = cy + Math.sin(angleDegrees).toFloat() * radius * lengthFraction
        drawLine(cx, cy, x, y, paint)
    }

    /** Draw a clock hand with a tail (counter-balance). */
    protected fun Canvas.drawHandWithTail(
        cx: Float, cy: Float,
        lengthFraction: Float, tailFraction: Float, radius: Float,
        angleDegrees: Double, paint: Paint
    ) {
        val cos = Math.cos(angleDegrees).toFloat()
        val sin = Math.sin(angleDegrees).toFloat()
        drawLine(
            cx - cos * radius * tailFraction,
            cy - sin * radius * tailFraction,
            cx + cos * radius * lengthFraction,
            cy + sin * radius * lengthFraction,
            paint
        )
    }
}
