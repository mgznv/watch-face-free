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

class TechDataWatchFaceService : BaseWatchFaceService() {

    override fun getWatchFaceType() = WatchFaceType.DIGITAL

    override fun getColorSchemes() = listOf(
        ColorScheme(
            id = StyleIds.OPTION_1,
            primaryColor          = Color.rgb(88, 166, 255),   // GitHub blue
            secondaryColor        = Color.rgb(248, 129, 102),
            accentColor           = Color.rgb(86, 211, 100),
            backgroundColor       = Color.rgb(13, 17, 23),
            ambientPrimaryColor   = Color.rgb(50, 100, 160)
        ),
        ColorScheme(
            id = StyleIds.OPTION_2,
            primaryColor          = Color.rgb(0, 255, 200),
            secondaryColor        = Color.rgb(0, 180, 255),
            accentColor           = Color.rgb(255, 200, 0),
            backgroundColor       = Color.rgb(0, 10, 20),
            ambientPrimaryColor   = Color.rgb(0, 150, 110)
        ),
        ColorScheme(
            id = StyleIds.OPTION_3,
            primaryColor          = Color.rgb(220, 220, 220),
            secondaryColor        = Color.rgb(150, 150, 150),
            accentColor           = Color.rgb(255, 100, 100),
            backgroundColor       = Color.rgb(20, 20, 20),
            ambientPrimaryColor   = Color.GRAY
        )
    )

    override suspend fun createRenderer(
        surfaceHolder: SurfaceHolder,
        watchState: WatchState,
        currentUserStyleRepository: CurrentUserStyleRepository,
        complicationSlotsManager: ComplicationSlotsManager
    ): BaseWatchFaceRenderer = TechDataRenderer(
        surfaceHolder, currentUserStyleRepository, watchState,
        complicationSlotsManager, getColorSchemes()
    )
}
