package com.example.iptvtvstarter.data

import retrofit2.http.GET
import retrofit2.http.Url

interface PlaylistApi {
    @GET
    suspend fun getText(@Url url: String): String
}
