package com.watchface.premium.collection.style

import androidx.annotation.ColorInt

/**
 * Immutable color scheme passed to the renderer.
 *
 * [ambientPrimaryColor] and [ambientBackgroundColor] should be desaturated versions
 * of their interactive counterparts to satisfy Wear OS ambient-mode power guidelines.
 */
data class ColorScheme(
    /** Option ID matching the ListUserStyleSetting option (e.g. "option_1"). */
    val id: String,
    @ColorInt val primaryColor: Int,
    @ColorInt val secondaryColor: Int,
    @ColorInt val accentColor: Int,
    @ColorInt val backgroundColor: Int,
    /** Desaturated primary for ambient mode. */
    @ColorInt val ambientPrimaryColor: Int,
    /** Near-black background for ambient mode (OLED power saving). */
    @ColorInt val ambientBackgroundColor: Int = 0xFF000000.toInt()
)
