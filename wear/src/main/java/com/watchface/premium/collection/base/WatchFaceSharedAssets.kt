package com.watchface.premium.collection.base

import androidx.wear.watchface.Renderer

/**
 * Shared assets instance passed to every render() call.
 * Allocate Bitmaps, Typefaces, or other heavy objects here so they are
 * created once and reused across interactive and headless rendering.
 */
class WatchFaceSharedAssets : Renderer.SharedAssets {
    override fun onDestroy() {
        // Release any retained resources (bitmaps, typefaces) here.
    }
}
