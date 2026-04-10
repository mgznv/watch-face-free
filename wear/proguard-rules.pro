# Keep all WatchFaceService subclasses – the system binds them by class name
-keep class * extends androidx.wear.watchface.WatchFaceService { *; }

# Keep Wear watchface library reflection-dependent classes
-keep class androidx.wear.watchface.** { *; }
-keep class androidx.wear.watchface.complications.** { *; }

# Keep Compose-related classes
-keep class androidx.compose.** { *; }
