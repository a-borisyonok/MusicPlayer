package com.rsschool.seka.musicplayer.data


import com.squareup.moshi.Json

data class Song(

    @Json(name = "title") val title: String,
    @Json(name = "artist") val artist: String,
    @Json(name = "trackUri") val trackUri: String,
    @Json(name = "bitmapUri") val bitmapUri: String

)
