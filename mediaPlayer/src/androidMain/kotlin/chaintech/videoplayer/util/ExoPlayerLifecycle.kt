package chaintech.videoplayer.util

import androidx.compose.runtime.MutableState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.media3.exoplayer.ExoPlayer

fun getExoPlayerLifecycleObserver(
    exoPlayer: ExoPlayer,
    isPause: Boolean,
    appInBackground: MutableState<Boolean>
): LifecycleEventObserver {
    return LifecycleEventObserver { source, event ->
        when(event){
            Lifecycle.Event.ON_RESUME -> {
                if(appInBackground.value){
                    exoPlayer.playWhenReady =!isPause
                }
            }
            Lifecycle.Event.ON_PAUSE -> {
                exoPlayer.playWhenReady=false
                appInBackground.value=true
            }
            Lifecycle.Event.ON_STOP -> {
                exoPlayer.playWhenReady=false
                appInBackground.value=true
            }
            Lifecycle.Event.ON_DESTROY -> {
                exoPlayer.release()
            }
            else->Unit
        }
    }
}