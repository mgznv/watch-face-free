package com.watchface.premium.collection.base

import android.view.SurfaceHolder
import androidx.wear.watchface.CanvasComplication
import androidx.wear.watchface.CanvasComplicationFactory
import androidx.wear.watchface.ComplicationSlot
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.WatchFace
import androidx.wear.watchface.WatchFaceService
import androidx.wear.watchface.WatchFaceType
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.complications.rendering.CanvasComplicationDrawable
import androidx.wear.watchface.complications.rendering.ComplicationDrawable
import androidx.wear.watchface.style.CurrentUserStyleRepository
import androidx.wear.watchface.style.UserStyleSchema
import androidx.wear.watchface.style.UserStyleSetting
import androidx.wear.watchface.style.UserStyleSetting.BooleanUserStyleSetting
import androidx.wear.watchface.style.UserStyleSetting.ListUserStyleSetting
import androidx.wear.watchface.style.UserStyleSetting.ListUserStyleSetting.ListOption
import androidx.wear.watchface.style.UserStyleSetting.Option
import androidx.wear.watchface.style.WatchFaceLayer
import com.watchface.premium.collection.R
import com.watchface.premium.collection.complication.ComplicationConfig
import com.watchface.premium.collection.style.ColorScheme
import com.watchface.premium.collection.style.StyleIds

/**
 * Base class shared by all 20 watch face services.
 *
 * Subclasses only need to provide:
 *  - [getColorSchemes]: the three color palettes for this face
 *  - [getWatchFaceType]: ANALOG or DIGITAL
 *  - [createRenderer]: the concrete renderer instance
 */
abstract class BaseWatchFaceService : WatchFaceService() {

    abstract fun getColorSchemes(): List<ColorScheme>
    abstract fun getWatchFaceType(): Int // WatchFaceType.ANALOG / WatchFaceType.DIGITAL

    abstract suspend fun createRenderer(
        surfaceHolder: SurfaceHolder,
        watchState: WatchState,
        currentUserStyleRepository: CurrentUserStyleRepository,
        complicationSlotsManager: ComplicationSlotsManager
    ): BaseWatchFaceRenderer

    // ----- UserStyleSchema -------------------------------------------------------

    override fun createUserStyleSchema(): UserStyleSchema {
        val schemes = getColorSchemes()
        require(schemes.size == 3) { "Each watch face must provide exactly 3 color schemes" }

        val optionStringResIds = listOf(
            R.string.color_option_1,
            R.string.color_option_2,
            R.string.color_option_3
        )

        val colorOptions = schemes.mapIndexed { index, scheme ->
            ListOption(
                id = Option.Id(scheme.id),
                resources = resources,
                displayNameResourceId = optionStringResIds[index],
                screenReaderNameResourceId = optionStringResIds[index],
                icon = null
            )
        }

        return UserStyleSchema(
            listOf(
                ListUserStyleSetting(
                    id = UserStyleSetting.Id(StyleIds.COLOR_SCHEME),
                    resources = resources,
                    displayNameResourceId = R.string.setting_color_scheme,
                    descriptionResourceId = R.string.setting_color_scheme_desc,
                    icon = null,
                    options = colorOptions,
                    affectsWatchFaceLayers = listOf(
                        WatchFaceLayer.BASE,
                        WatchFaceLayer.COMPLICATIONS,
                        WatchFaceLayer.COMPLICATIONS_OVERLAY
                    )
                ),
                BooleanUserStyleSetting(
                    id = UserStyleSetting.Id(StyleIds.SHOW_SECONDS),
                    resources = resources,
                    displayNameResourceId = R.string.setting_show_seconds,
                    descriptionResourceId = R.string.setting_show_seconds_desc,
                    icon = null,
                    affectsWatchFaceLayers = listOf(WatchFaceLayer.BASE),
                    defaultValue = true
                ),
                BooleanUserStyleSetting(
                    id = UserStyleSetting.Id(StyleIds.SHOW_DATE),
                    resources = resources,
                    displayNameResourceId = R.string.setting_show_date,
                    descriptionResourceId = R.string.setting_show_date_desc,
                    icon = null,
                    affectsWatchFaceLayers = listOf(WatchFaceLayer.BASE),
                    defaultValue = true
                )
            )
        )
    }

    // ----- ComplicationSlotsManager ---------------------------------------------

    override fun createComplicationSlotsManager(
        currentUserStyleRepository: CurrentUserStyleRepository
    ): ComplicationSlotsManager {

        val factory = CanvasComplicationFactory { watchState: WatchState,
                                                  invalidateCallback: CanvasComplication.InvalidateCallback ->
            CanvasComplicationDrawable(
                ComplicationDrawable(applicationContext),
                watchState,
                invalidateCallback
            )
        }

        return ComplicationSlotsManager(
            listOf(
                ComplicationSlot.createRoundRectComplicationSlotBuilder(
                    id = ComplicationConfig.TOP_SLOT_ID,
                    canvasComplicationFactory = factory,
                    supportedTypes = ComplicationConfig.supportedTypes,
                    defaultDataSourcePolicy = ComplicationConfig.topDefaultPolicy,
                    bounds = ComplicationConfig.topBounds
                ).build(),
                ComplicationSlot.createRoundRectComplicationSlotBuilder(
                    id = ComplicationConfig.LEFT_SLOT_ID,
                    canvasComplicationFactory = factory,
                    supportedTypes = ComplicationConfig.supportedTypes,
                    defaultDataSourcePolicy = ComplicationConfig.leftDefaultPolicy,
                    bounds = ComplicationConfig.leftBounds
                ).build(),
                ComplicationSlot.createRoundRectComplicationSlotBuilder(
                    id = ComplicationConfig.BOTTOM_SLOT_ID,
                    canvasComplicationFactory = factory,
                    supportedTypes = ComplicationConfig.supportedTypes,
                    defaultDataSourcePolicy = ComplicationConfig.bottomDefaultPolicy,
                    bounds = ComplicationConfig.bottomBounds
                ).build()
            ),
            currentUserStyleRepository
        )
    }

    // ----- WatchFace factory ----------------------------------------------------

    override suspend fun createWatchFace(
        surfaceHolder: SurfaceHolder,
        watchState: WatchState,
        complicationSlotsManager: ComplicationSlotsManager,
        currentUserStyleRepository: CurrentUserStyleRepository
    ): WatchFace {
        val renderer = createRenderer(
            surfaceHolder,
            watchState,
            currentUserStyleRepository,
            complicationSlotsManager
        )
        return WatchFace(getWatchFaceType(), renderer)
    }
}
