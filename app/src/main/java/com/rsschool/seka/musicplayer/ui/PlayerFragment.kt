package com.rsschool.seka.musicplayer.ui

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.RequestManager
import com.rsschool.seka.musicplayer.R
import com.rsschool.seka.musicplayer.data.Song
import com.rsschool.seka.musicplayer.databinding.PlayerFragmentBinding
import com.rsschool.seka.musicplayer.exoplayer.isPlaying
import com.rsschool.seka.musicplayer.exoplayer.toSong
import com.rsschool.seka.musicplayer.util.Status
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class PlayerFragment : Fragment() {

    @Inject
    lateinit var glide: RequestManager

    private var _binding: PlayerFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: PlayerViewModel

    private var curPlayingSong: Song? = null
    private var playbackState: PlaybackStateCompat? = null
    private var shouldUpdateSeekbar = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = PlayerFragmentBinding.inflate(inflater).also { _binding = it }.root


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(PlayerViewModel::class.java)

        subscribeToObservers()

        binding.playpauseButton.setOnClickListener {
            curPlayingSong?.let {
                viewModel.playOrToggleSong(it, true)
            }
        }
        binding.stopButton.setOnClickListener {
            viewModel.onStop()

        }

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    setCurPlayerTimeToTextView(progress.toLong())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                shouldUpdateSeekbar = false
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seekBar?.let {
                    viewModel.seekTo(it.progress.toLong())
                    shouldUpdateSeekbar = true
                }
            }
        })

        binding.previousButton.setOnClickListener {
            viewModel.onPrevious()
        }

        binding.nextButton.setOnClickListener {
            viewModel.onNext()
        }
    }

    private fun updateTitleAndSongImage(song: Song) {
        binding.track.text = song.title
        binding.artist.text = song.artist
        glide.load(song.bitmapUri).into(binding.trackBitmap)
    }

    private fun subscribeToObservers() {
        viewModel.mediaItems.observe(viewLifecycleOwner) {
            it?.let { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        result.data?.let { songs ->
                            if (curPlayingSong == null && songs.isNotEmpty()) {
                                curPlayingSong = songs[0]
                                updateTitleAndSongImage(songs[0])
                            }
                        }
                    }
                    else -> Unit
                }
            }
        }
        viewModel.curPlayingSong.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            curPlayingSong = it.toSong()
            curPlayingSong?.let { it1 -> updateTitleAndSongImage(it1) }
        }
        viewModel.playbackState.observe(viewLifecycleOwner) {
            playbackState = it
            binding.playpauseButton.setImageResource(
                if (playbackState?.isPlaying == true) R.drawable.ic_baseline_pause_circle_filled_24 else R.drawable.ic_baseline_play_circle_filled_24
            )
            binding.seekBar.progress = it?.position?.toInt() ?: 0
        }
        viewModel.curPlayerPosition.observe(viewLifecycleOwner) {
            if (shouldUpdateSeekbar) {
                binding.seekBar.progress = it.toInt()
                setCurPlayerTimeToTextView(it)
            }
        }
        viewModel.curSongDuration.observe(viewLifecycleOwner) {
            binding.seekBar.max = it.toInt()
            val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
            binding.songDuration.text = dateFormat.format(it)
        }
    }

    private fun setCurPlayerTimeToTextView(ms: Long) {
        val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
        binding.currentTime.text = dateFormat.format(ms)
    }
}