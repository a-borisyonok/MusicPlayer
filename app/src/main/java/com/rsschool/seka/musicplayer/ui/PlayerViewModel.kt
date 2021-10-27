package com.rsschool.seka.musicplayer.ui

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rsschool.seka.musicplayer.data.Song
import com.rsschool.seka.musicplayer.exoplayer.*
import com.rsschool.seka.musicplayer.util.Constants.MEDIA_ROOT_ID
import com.rsschool.seka.musicplayer.util.Constants.UPDATE_PLAYER_POSITION_INTERVAL
import com.rsschool.seka.musicplayer.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class PlayerViewModel @Inject constructor(
    private val musicServiceConnection: MusicServiceConnection
) : ViewModel() {


    private val _mediaItems = MutableLiveData<Resource<List<Song>>>()

    val mediaItems: LiveData<Resource<List<Song>>> = _mediaItems

    val curPlayingSong = musicServiceConnection.curPlayingSong
    val playbackState = musicServiceConnection.playbackState


    private val _curSongDuration = MutableLiveData<Long>()
    val curSongDuration: LiveData<Long> = _curSongDuration

    private val _curPlayerPosition = MutableLiveData<Long>()
    val curPlayerPosition: LiveData<Long> = _curPlayerPosition


    init {
        updateCurrentPlayerPosition()
        _mediaItems.postValue(Resource.loading(null))
        musicServiceConnection.subscribe(
            MEDIA_ROOT_ID,
            object : MediaBrowserCompat.SubscriptionCallback() {
                override fun onChildrenLoaded(
                    parentId: String,
                    children: MutableList<MediaBrowserCompat.MediaItem>
                ) {
                    super.onChildrenLoaded(parentId, children)
                    val items = children.map {
                        Song(

                            it.description.title.toString(),
                            it.description.subtitle.toString(),
                            it.description.mediaUri.toString(),
                            it.description.iconUri.toString(),

                        )
                    }
                    _mediaItems.postValue(Resource.success(items))
                }
            })
    }

    private fun updateCurrentPlayerPosition() {
        viewModelScope.launch {
            while (true) {
                val pos = playbackState.value?.currentPlaybackPosition
                if (curPlayerPosition.value != pos) {
                    _curPlayerPosition.postValue(pos!!)
                    _curSongDuration.postValue(MusicService.curSongDuration)

                }
                delay(UPDATE_PLAYER_POSITION_INTERVAL)
            }
        }
    }

    fun onNext() {
        musicServiceConnection.transportControls.skipToNext()

    }

    fun onPrevious() {
        musicServiceConnection.transportControls.skipToPrevious()
    }

    fun onStop() {
        musicServiceConnection.transportControls.pause()
        seekTo(0L)
        _curPlayerPosition.postValue(0L)
    }


    fun seekTo(pos: Long) {
        musicServiceConnection.transportControls.seekTo(pos)
    }

    fun playOrToggleSong(mediaItem: Song, toggle: Boolean = false) {
        val isPrepared = playbackState.value?.isPrepared ?: false
        if (isPrepared && mediaItem.title ==
            curPlayingSong.value?.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID)

        ) {

            playbackState.value?.let { playbackState ->
                when {
                    playbackState.isPlaying -> if (toggle) musicServiceConnection.transportControls.pause()
                    playbackState.isPlayEnabled -> musicServiceConnection.transportControls.play()
                    else -> Unit
                }
            }
        } else {
            musicServiceConnection.transportControls.playFromMediaId(mediaItem.title, null)
           }
    }

    override fun onCleared() {
        super.onCleared()
        musicServiceConnection.unsubscribe(
            MEDIA_ROOT_ID,
            object : MediaBrowserCompat.SubscriptionCallback() {})
    }
}



