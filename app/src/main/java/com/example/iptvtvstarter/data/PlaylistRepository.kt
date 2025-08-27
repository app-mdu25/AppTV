package com.example.iptvtvstarter.data

import com.example.iptvtvstarter.util.M3UParser

class PlaylistRepository(private val api: PlaylistApi) {
    suspend fun loadLive(url: String) = M3UParser.parse(api.getText(url))
}
