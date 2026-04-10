package com.watchface.premium.collection.complication

import android.graphics.RectF
import androidx.wear.watchface.complications.ComplicationSlotBounds
import androidx.wear.watchface.complications.DefaultComplicationDataSourcePolicy
import androidx.wear.watchface.complications.SystemDataSources
import androidx.wear.watchface.complications.data.ComplicationType

/**
 * Shared complication slot configuration used by all 20 watch faces.
 *
 * Three slots are placed at the top, left, and bottom of the dial.
 * Override positions in individual renderers if needed via
 * [ComplicationSlotsManager.complicationSlots].
 */
object ComplicationConfig {

    const val TOP_SLOT_ID    = 100
    const val LEFT_SLOT_ID   = 101
    const val BOTTOM_SLOT_ID = 102

    /** Fractional bounds in the [0..1] unit square. */
    val topBounds    = ComplicationSlotBounds(RectF(0.35f, 0.08f, 0.65f, 0.30f))
    val leftBounds   = ComplicationSlotBounds(RectF(0.08f, 0.40f, 0.30f, 0.60f))
    val bottomBounds = ComplicationSlotBounds(RectF(0.35f, 0.70f, 0.65f, 0.92f))

    val supportedTypes = listOf(
        ComplicationType.RANGED_VALUE,
        ComplicationType.SHORT_TEXT,
        ComplicationType.MONOCHROMATIC_IMAGE,
        ComplicationType.SMALL_IMAGE,
        ComplicationType.LONG_TEXT
    )

    /** Top slot defaults to battery level. */
    val topDefaultPolicy = DefaultComplicationDataSourcePolicy(
        SystemDataSources.DATA_SOURCE_WATCH_BATTERY,
        ComplicationType.RANGED_VALUE
    )

    /** Left slot defaults to step count. */
    val leftDefaultPolicy = DefaultComplicationDataSourcePolicy(
        SystemDataSources.DATA_SOURCE_STEP_COUNT,
        ComplicationType.SHORT_TEXT
    )

    /** Bottom slot defaults to date. */
    val bottomDefaultPolicy = DefaultComplicationDataSourcePolicy(
        SystemDataSources.DATA_SOURCE_DAY_OF_WEEK,
        ComplicationType.SHORT_TEXT
    )
}
