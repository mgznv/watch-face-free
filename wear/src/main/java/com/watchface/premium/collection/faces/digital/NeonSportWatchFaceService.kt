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

class NeonSportWatchFaceService : BaseWatchFaceService() {

    override fun getWatchFaceType() = WatchFaceType.DIGITAL

    override fun getColorSchemes() = listOf(
        ColorScheme(
            id = StyleIds.OPTION_1,
            primaryColor          = Color.rgb(0, 255, 136),    // neon green
            secondaryColor        = Color.rgb(0, 136, 255),    // neon blue
            accentColor           = Color.rgb(255, 255, 0),
            backgroundColor       = Color.rgb(5, 5, 16),
            ambientPrimaryColor   = Color.rgb(0, 140, 75)
        ),
        ColorScheme(
            id = StyleIds.OPTION_2,
            primaryColor          = Color.rgb(255, 50, 180),   // neon pink
            secondaryColor        = Color.rgb(150, 0, 255),    // neon purple
            accentColor           = Color.rgb(0, 220, 255),
            backgroundColor       = Color.rgb(10, 0, 16),
            ambientPrimaryColor   = Color.rgb(160, 30, 100)
        ),
        ColorScheme(
            id = StyleIds.OPTION_3,
            primaryColor          = Color.rgb(255, 150, 0),    // neon orange
            secondaryColor        = Color.rgb(255, 50, 0),     // neon red
            accentColor           = Color.rgb(255, 255, 100),
            backgroundColor       = Color.rgb(16, 5, 0),
            ambientPrimaryColor   = Color.rgb(160, 80, 0)
        )
    )

    override suspend fun createRenderer(
        surfaceHolder: SurfaceHolder,
        watchState: WatchState,
        currentUserStyleRepository: CurrentUserStyleRepository,
        complicationSlotsManager: ComplicationSlotsManager
    ): BaseWatchFaceRenderer = NeonSportRenderer(
        surfaceHolder, currentUserStyleRepository, watchState,
        complicationSlotsManager, getColorSchemes()
    )
}
