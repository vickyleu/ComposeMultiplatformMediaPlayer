package chaintech.videoplayer.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.InternalComposeApi
import androidx.compose.ui.uikit.InterfaceOrientation
import platform.Foundation.NSString
import platform.Foundation.setValue
import platform.Foundation.stringWithFormat
import platform.UIKit.UIApplication
import platform.UIKit.UIDevice
import platform.UIKit.UIInterfaceOrientationLandscapeLeft
import platform.UIKit.UIInterfaceOrientationMask
import platform.UIKit.UIInterfaceOrientationMaskLandscape
import platform.UIKit.UIInterfaceOrientationMaskLandscapeLeft
import platform.UIKit.UIInterfaceOrientationMaskLandscapeRight
import platform.UIKit.UIInterfaceOrientationMaskPortrait
import platform.UIKit.UIInterfaceOrientationPortrait
import platform.UIKit.UIWindowScene
import platform.UIKit.UIWindowSceneGeometryPreferences
import platform.UIKit.UIWindowSceneGeometryPreferencesIOS

actual fun formatMinSec(value: Int): String {
    val hour = (value / 3600)
    val remainingSecondsAfterHours = (value % 3600)
    val minutes = remainingSecondsAfterHours / 60
    val seconds = remainingSecondsAfterHours % 60

    val strHour : String = if (hour > 0) { NSString.stringWithFormat(format = "%02d:", hour)
    } else { "" }
    val strMinutes : String = NSString.stringWithFormat(format = "%02d:", minutes)
    val strSeconds : String = NSString.stringWithFormat(format = "%02d", seconds)

    return "${strHour}${strMinutes}${strSeconds}"
}

actual fun formatInterval(value: Int): Int {
    return value
}

@OptIn(InternalComposeApi::class)
@Composable
actual fun LandscapeOrientation(
    isLandscape: Boolean,
    content: @Composable () -> Unit
) {
    DisposableEffect(isLandscape) {
        val windowScene = UIApplication.sharedApplication.keyWindow?.windowScene
        val orientation = if (isLandscape) {
            UIInterfaceOrientationMaskLandscapeRight
        } else {
            UIInterfaceOrientationMaskPortrait
        }
        windowScene?.requestGeometryUpdateWithPreferences(UIWindowSceneGeometryPreferencesIOS().apply {
            interfaceOrientations = orientation//不存在的字段
        }, errorHandler = null)
        onDispose {
            windowScene?.requestGeometryUpdateWithPreferences(UIWindowSceneGeometryPreferencesIOS().apply {
                interfaceOrientations = UIInterfaceOrientationMaskPortrait//不存在的字段
            }, errorHandler = null)
        }
    }
    content()
}


