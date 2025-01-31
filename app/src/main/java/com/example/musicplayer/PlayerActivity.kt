package com.example.musicplayer

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.bumptech.glide.Glide
import com.example.musicplayer.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    lateinit var exoPlayer: ExoPlayer

    var playerlistener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            super.onIsPlayingChanged(isPlaying)
            if (isPlaying)
                showGif(true)
            else
                showGif(false)
        }
    }

    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MyExoPlayer.getCurrentSong()?.apply {
            Log.i("song",this.title+" "+this.singer_name+" "+this.coverUrl)
            binding.songTitleTextView.text = this.title
            binding.songSubtitleTextView.text = this.singer_name
            Glide.with(binding.songCoverImageView)
                .load(this.coverUrl)
                .circleCrop()
                .into(binding.songCoverImageView)
            Glide.with(binding.songGifImageView)
                .load(R.drawable.media_playing)
                .circleCrop()
                .into(binding.songGifImageView)
            exoPlayer = MyExoPlayer.getInstance()!!
            binding.playerView.player = exoPlayer
            binding.playerView.showController()
            exoPlayer.addListener(playerlistener)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.removeListener(playerlistener)
    }

    fun showGif(show : Boolean){
        if(show)
            binding.songGifImageView.visibility= View.VISIBLE
        else
            binding.songGifImageView.visibility= View.INVISIBLE
    }
}