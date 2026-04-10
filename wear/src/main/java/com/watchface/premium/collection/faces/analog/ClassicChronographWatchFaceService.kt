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

class ClassicChronographWatchFaceService : BaseWatchFaceService() {

    override fun getWatchFaceType() = WatchFaceType.ANALOG

    override fun getColorSchemes() = listOf(
        ColorScheme(
            id = StyleIds.OPTION_1,
            primaryColor          = Color.WHITE,
            secondaryColor        = Color.LTGRAY,
            accentColor           = Color.rgb(220, 50, 50),
            backgroundColor       = Color.rgb(18, 18, 18),
            ambientPrimaryColor   = Color.GRAY
        ),
        ColorScheme(
            id = StyleIds.OPTION_2,
            primaryColor          = Color.rgb(255, 215, 0),
            secondaryColor        = Color.rgb(200, 170, 50),
            accentColor           = Color.WHITE,
            backgroundColor       = Color.rgb(15, 12, 5),
            ambientPrimaryColor   = Color.rgb(160, 130, 0)
        ),
        ColorScheme(
            id = StyleIds.OPTION_3,
            primaryColor          = Color.rgb(100, 180, 255),
            secondaryColor        = Color.rgb(50, 130, 200),
            accentColor           = Color.rgb(255, 120, 0),
            backgroundColor       = Color.rgb(5, 5, 25),
            ambientPrimaryColor   = Color.rgb(60, 100, 160)
        )
    )

    override suspend fun createRenderer(
        surfaceHolder: SurfaceHolder,
        watchState: WatchState,
        currentUserStyleRepository: CurrentUserStyleRepository,
        complicationSlotsManager: ComplicationSlotsManager
    ): BaseWatchFaceRenderer = ClassicChronographRenderer(
        surfaceHolder, currentUserStyleRepository, watchState,
        complicationSlotsManager, getColorSchemes()
    )
}
