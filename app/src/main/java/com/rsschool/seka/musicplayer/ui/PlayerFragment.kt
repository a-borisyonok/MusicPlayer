package com.rsschool.seka.musicplayer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.rsschool.seka.musicplayer.data.PlayListService
import com.rsschool.seka.musicplayer.databinding.PlayerFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlayerFragment : Fragment() {


    private var _binding: PlayerFragmentBinding? = null
    private val binding get() = _binding!!
//    private lateinit var viewModel: PlayerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

//        songsList = context?.let { Repository(it).playList } as List<Song>
        return PlayerFragmentBinding.inflate(inflater).also { _binding = it }.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//       val  viewModel = ViewModelProvider(this).get(PlayerViewModel::class.java)
        val palylist = context?.let { PlayListService(it).getPlaylist() }?.get(0)
        val viewModel = ViewModelProvider(this).get(PlayerViewModel::class.java)
        var songNumber = 0
        binding.playpauseButton.setOnClickListener { viewModel.playOrToggleSong(palylist!!) }


    }
}

//    private fun getSongInfo(song: Song) {
//        Log.i("infoinfosong", song.title)
//        val imgUrl = song.bitmapUri
//        val imgView = binding.trackBitmap
//        binding.track.text = song.title
//        binding.artist.text = song.artist
//
//        imgUrl.let {
//            val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
//
//            imgView.context?.let { it1 ->
//                Glide.with(it1)
//                    .load(imgUri)
//                    .apply(
//                        RequestOptions()
//                            .placeholder(R.drawable.loading_animation)
//                            .error(R.drawable.default_image)
//                    )
//                    .into(imgView)
//            }
//        }
//    }




