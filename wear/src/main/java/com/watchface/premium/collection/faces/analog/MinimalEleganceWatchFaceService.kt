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

class MinimalEleganceWatchFaceService : BaseWatchFaceService() {

    override fun getWatchFaceType() = WatchFaceType.ANALOG

    override fun getColorSchemes() = listOf(
        ColorScheme(
            id = StyleIds.OPTION_1,
            primaryColor          = Color.rgb(50, 50, 50),
            secondaryColor        = Color.rgb(150, 150, 150),
            accentColor           = Color.rgb(196, 154, 108),  // rose gold
            backgroundColor       = Color.rgb(245, 240, 232),  // cream
            ambientPrimaryColor   = Color.DKGRAY,
            ambientBackgroundColor = Color.BLACK
        ),
        ColorScheme(
            id = StyleIds.OPTION_2,
            primaryColor          = Color.WHITE,
            secondaryColor        = Color.rgb(200, 200, 200),
            accentColor           = Color.rgb(120, 180, 255),
            backgroundColor       = Color.rgb(20, 20, 35),
            ambientPrimaryColor   = Color.GRAY
        ),
        ColorScheme(
            id = StyleIds.OPTION_3,
            primaryColor          = Color.rgb(30, 30, 30),
            secondaryColor        = Color.rgb(100, 100, 100),
            accentColor           = Color.rgb(180, 200, 120),
            backgroundColor       = Color.rgb(245, 245, 240),
            ambientPrimaryColor   = Color.DKGRAY,
            ambientBackgroundColor = Color.BLACK
        )
    )

    override suspend fun createRenderer(
        surfaceHolder: SurfaceHolder,
        watchState: WatchState,
        currentUserStyleRepository: CurrentUserStyleRepository,
        complicationSlotsManager: ComplicationSlotsManager
    ): BaseWatchFaceRenderer = MinimalEleganceRenderer(
        surfaceHolder, currentUserStyleRepository, watchState,
        complicationSlotsManager, getColorSchemes()
    )
}
