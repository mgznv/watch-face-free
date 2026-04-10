package com.watchface.premium.collection.faces.digital

import android.graphics.Color
import android.view.SurfaceHolder
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.WatchFaceType
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.style.CurrentUserStyleRepository
import com.watchface.premium.collection.base.BaseWatchFaceRenderer
import com.watchface.premium.collection.base.BaseWatchFaceService
import com.watchface.premium.collection.style.ColorScheme
import com.watchface.premium.collection.style.StyleIds

class HealthFocusWatchFaceService : BaseWatchFaceService() {

    override fun getWatchFaceType() = WatchFaceType.DIGITAL

    override fun getColorSchemes() = listOf(
        ColorScheme(
            id = StyleIds.OPTION_1,
            primaryColor          = Color.rgb(255, 45, 85),    // Apple Health red
            secondaryColor        = Color.rgb(76, 217, 100),   // green
            accentColor           = Color.rgb(90, 200, 250),   // blue
            backgroundColor       = Color.rgb(10, 10, 15),
            ambientPrimaryColor   = Color.rgb(160, 30, 50)
        ),
        ColorScheme(
            id = StyleIds.OPTION_2,
            primaryColor          = Color.rgb(255, 100, 130),
            secondaryColor        = Color.rgb(100, 210, 255),
            accentColor           = Color.rgb(255, 220, 0),
            backgroundColor       = Color.rgb(12, 8, 20),
            ambientPrimaryColor   = Color.rgb(160, 60, 80)
        ),
        ColorScheme(
            id = StyleIds.OPTION_3,
            primaryColor          = Color.rgb(0, 230, 180),
            secondaryColor        = Color.rgb(0, 180, 255),
            accentColor           = Color.rgb(255, 200, 0),
            backgroundColor       = Color.rgb(0, 12, 18),
            ambientPrimaryColor   = Color.rgb(0, 140, 110)
        )
    )

    override suspend fun createRenderer(
        surfaceHolder: SurfaceHolder,
        watchState: WatchState,
        currentUserStyleRepository: CurrentUserStyleRepository,
        complicationSlotsManager: ComplicationSlotsManager
    ): BaseWatchFaceRenderer = HealthFocusRenderer(
        surfaceHolder, currentUserStyleRepository, watchState,
        complicationSlotsManager, getColorSchemes()
    )
}
