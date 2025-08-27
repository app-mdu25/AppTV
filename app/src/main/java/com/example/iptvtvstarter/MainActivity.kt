package com.example.iptvtvstarter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.tv.foundation.lazy.grid.TvLazyVerticalGrid
import androidx.tv.material3.TvTheme
import androidx.tv.material3.Button
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.remember
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.runtime.LaunchedEffect
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import android.view.ViewGroup
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.runtime.DisposableEffect
import timber.log.Timber
import androidx.media3.common.C

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(object : timber.log.Timber.DebugTree() {})
        setContent {
            TvTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val context = LocalContext.current
    val playerReady = remember { mutableOf(false) }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("IPTV TV Starter", modifier = Modifier.padding(24.dp), textAlign = TextAlign.Center)
        Button(onClick = {
            // navigate to player - for simplicity show inline player below
            playerReady.value = true
        }) {
            Text("Play sample stream (ClearKey demo)")
        }

        if (playerReady.value) {
            Box(modifier = Modifier
                .padding(12.dp)
                .fillMaxSize()) {
                SamplePlayerView()
            }
        }
    }
}

// small wrapper to allow mutable boolean in compose without bringing full state classes
fun <T> mutableOf(initial: T) = androidx.compose.runtime.mutableStateOf(initial)

@OptIn(UnstableApi::class)
@Composable
fun SamplePlayerView() {
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            playWhenReady = true
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    // Example: a public demo ClearKey-protected stream is not provided here.
    // Replace with your stream URL and DRM license configuration.
    val mediaItem = remember {
        MediaItem.Builder()
            .setUri(Uri.parse("https://storage.googleapis.com/wvmedia/clear/h264/tears/tears.mpd"))
            // No DRM config here — you can set MediaItem.DrmConfiguration if needed
            .build()
    }

    LaunchedEffect(mediaItem) {
        try {
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
        } catch (e: Exception) {
            Timber.e(e, "Error preparing player")
        }
    }

    AndroidView(factory = { ctx ->
        PlayerView(ctx).apply {
            player = exoPlayer
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }, modifier = Modifier.fillMaxSize())
}
