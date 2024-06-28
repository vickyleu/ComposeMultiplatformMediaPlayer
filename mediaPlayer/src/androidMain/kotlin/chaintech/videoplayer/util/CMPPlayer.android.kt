package chaintech.videoplayer.util

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.Player
import chaintech.videoplayer.model.PlayerSpeed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext

@Composable
actual fun CMPPlayer(
    modifier: Modifier,
    url: String,
    isPause: Boolean,
    isMute: Boolean,
    totalTime: (Int) -> Unit,
    currentTime: (Int) -> Unit,
    isSliding: Boolean,
    sliderTime: Int?,
    speed: PlayerSpeed
) {
    val context = LocalContext.current
    val exoPlayer = rememberExoPlayerWithLifecycle(url, context, isPause)
    val playerView = rememberPlayerView(exoPlayer, context)
    LaunchedEffect(exoPlayer) {
        withContext(Dispatchers.IO) {
            while (isActive) {
                withContext(Dispatchers.Main){
                    currentTime(exoPlayer.currentPosition.toInt().coerceAtLeast(0))
                }
                delay(1000L)
            }
        }
    }
    LaunchedEffect(playerView) {
        playerView.keepScreenOn = true
    }
    Box(modifier = modifier) {
        AndroidView(
            factory = { playerView },
            modifier = Modifier.fillMaxSize(),
            update = {
                it.player = exoPlayer
                exoPlayer.playWhenReady = !isPause
                exoPlayer.volume = if (isMute) 0f else 1f
                sliderTime?.let { exoPlayer.seekTo(it.toLong()) }
                exoPlayer.setPlaybackSpeed(
                    when (speed) {
                        PlayerSpeed.X0_5 -> 0.5f
                        PlayerSpeed.X1 -> 1.0f
                        PlayerSpeed.X1_5 -> 1.5f
                        PlayerSpeed.X2 -> 2.0f
                    }
                )
            }
        )

        DisposableEffect(Unit) {
            val listener = object : Player.Listener {
                override fun onEvents(player: Player, events: Player.Events) {
                    if (!isSliding) {
                        totalTime(player.duration.coerceAtLeast(0).toInt())
                        currentTime(player.currentPosition.coerceAtLeast(0).toInt())
                    }
                }
            }
            exoPlayer.addListener(listener)
            onDispose {
                exoPlayer.removeListener(listener)
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
            playerView.player = null
        }
    }
}