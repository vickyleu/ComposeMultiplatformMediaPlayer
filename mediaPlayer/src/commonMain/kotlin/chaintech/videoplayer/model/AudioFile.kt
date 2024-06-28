package chaintech.videoplayer.model

import androidx.compose.ui.graphics.Color


data class AudioFile (
    var audioUrl: String,
    var audioTitle: String,
    var thumbnailUrl: String = ""
)