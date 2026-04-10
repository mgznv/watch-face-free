package com.watchface.premium.collection.faces.analog

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

class PilotHeritageWatchFaceService : BaseWatchFaceService() {

    override fun getWatchFaceType() = WatchFaceType.ANALOG

    override fun getColorSchemes() = listOf(
        ColorScheme(
            id = StyleIds.OPTION_1,
            primaryColor          = Color.WHITE,
            secondaryColor        = Color.rgb(220, 220, 220),
            accentColor           = Color.rgb(255, 100, 0),
            backgroundColor       = Color.rgb(8, 8, 8),
            ambientPrimaryColor   = Color.GRAY
        ),
        ColorScheme(
            id = StyleIds.OPTION_2,
            primaryColor          = Color.rgb(255, 255, 200),
            secondaryColor        = Color.rgb(200, 200, 150),
            accentColor           = Color.rgb(255, 200, 0),
            backgroundColor       = Color.rgb(5, 10, 5),
            ambientPrimaryColor   = Color.rgb(130, 130, 100)
        ),
        ColorScheme(
            id = StyleIds.OPTION_3,
            primaryColor          = Color.rgb(180, 220, 255),
            secondaryColor        = Color.rgb(140, 180, 220),
            accentColor           = Color.rgb(255, 80, 80),
            backgroundColor       = Color.rgb(0, 5, 15),
            ambientPrimaryColor   = Color.rgb(80, 120, 160)
        )
    )

    override suspend fun createRenderer(
        surfaceHolder: SurfaceHolder,
        watchState: WatchState,
        currentUserStyleRepository: CurrentUserStyleRepository,
        complicationSlotsManager: ComplicationSlotsManager
    ): BaseWatchFaceRenderer = PilotHeritageRenderer(
        surfaceHolder, currentUserStyleRepository, watchState,
        complicationSlotsManager, getColorSchemes()
    )
}
