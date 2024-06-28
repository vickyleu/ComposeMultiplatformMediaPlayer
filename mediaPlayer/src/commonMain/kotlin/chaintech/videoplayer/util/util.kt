package chaintech.videoplayer.util

import androidx.compose.runtime.Composable

expect fun formatMinSec(value: Int): String

expect fun formatInterval(value: Int): Int

@Composable
expect fun LandscapeOrientation(
    isLandscape: Boolean,
    content: @Composable () -> Unit
)