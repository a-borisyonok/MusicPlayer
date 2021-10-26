package com.rsschool.seka.musicplayer.exoplayer

import android.support.v4.media.MediaMetadataCompat
import com.rsschool.seka.musicplayer.data.Song

fun MediaMetadataCompat.toSong(): Song? {
    return description?.let {
        Song(

            it.title.toString(),
            it.subtitle.toString(),
            it.mediaUri.toString(),
            it.iconUri.toString()
        )
    }
}