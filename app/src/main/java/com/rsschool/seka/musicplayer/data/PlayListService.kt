package com.rsschool.seka.musicplayer.data

import android.content.Context
import com.rsschool.seka.musicplayer.R
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import javax.inject.Inject

class PlayListService @Inject constructor(private val context: Context) {


    fun getPlaylist(): List<Song>? {

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val listType = Types.newParameterizedType(List::class.java, Song::class.java)
        val adapter: JsonAdapter<List<Song>> = moshi.adapter(listType)

        val jsonPlaylist = context.resources.openRawResource(R.raw.playlist)
            .bufferedReader().use { it.readText() }

        return adapter.fromJson(jsonPlaylist)
    }
}
