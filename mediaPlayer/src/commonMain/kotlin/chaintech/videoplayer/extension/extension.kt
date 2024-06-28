package chaintech.videoplayer.extension

import chaintech.videoplayer.util.formatInterval
import chaintech.videoplayer.util.formatMinSec

fun Int.formatMinSec(): String {
    return formatMinSec(this)
}

fun Int.formattedInterval(): Int {
    return  formatInterval(this)
}