package chaintech.videoplayer.util

import android.net.Uri
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.media3.common.C.VIDEO_SCALING_MODE_SCALE_TO_FIT
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.media3.ui.PlayerView.SHOW_BUFFERING_ALWAYS


@OptIn(UnstableApi::class)
@Composable
fun rememberExoPlayerWithLifecycle(reelUrl: String, context: android.content.Context, isPause: Boolean): ExoPlayer{
    val lifecycleOwner = LocalLifecycleOwner.current
   val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            videoScalingMode = VIDEO_SCALING_MODE_SCALE_TO_FIT
            repeatMode = Player.REPEAT_MODE_ONE
            setHandleAudioBecomingNoisy(true)
        }
    }
    LaunchedEffect(reelUrl){
        val videoUri = Uri.parse(reelUrl)
        val mediaItem = MediaItem.fromUri(videoUri)
        val mediaSource = ProgressiveMediaSource.Factory(DefaultDataSource.Factory(context)).createMediaSource(mediaItem)
        exoPlayer.seekTo(0,0L)
        exoPlayer.setMediaSource(mediaSource)
        exoPlayer.prepare()
    }
    val appInBackground = remember{ mutableStateOf(false) }
    DisposableEffect(lifecycleOwner,appInBackground){
        val observer = getExoPlayerLifecycleObserver(exoPlayer = exoPlayer,isPause,appInBackground)
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    return exoPlayer
}

@OptIn(UnstableApi::class)
@Composable
fun rememberPlayerView(exoPlayer: ExoPlayer, context: android.content.Context): PlayerView {
    val player = remember{
        PlayerView(context).apply {
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
            useController=false
            resizeMode= AspectRatioFrameLayout.RESIZE_MODE_ZOOM
            player = exoPlayer
            setShowBuffering(SHOW_BUFFERING_ALWAYS)
        }
    }

    DisposableEffect(Unit){
        onDispose {
            player.player=null
        }
    }

    return player
}
