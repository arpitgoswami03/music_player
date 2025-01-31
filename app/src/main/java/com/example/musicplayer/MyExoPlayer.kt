package com.example.musicplayer

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.musicplayer.models.SongModel

object MyExoPlayer {
    private var exoPlayer : ExoPlayer? = null
    private var currentSong : SongModel? = null

    fun getInstance() : ExoPlayer?{
        return exoPlayer
    }

    fun getCurrentSong() : SongModel? {
        return currentSong
    }

    fun stopPlaying() {
        exoPlayer?.stop()
        exoPlayer?.release()
        exoPlayer = null
        currentSong = null
    }

    fun startPlaying(context : Context,
                     song : SongModel
    ){
        if(exoPlayer==null){
            exoPlayer = ExoPlayer.Builder(context).build()
        }
        if(currentSong!=song){
            stopPlaying()
            exoPlayer = ExoPlayer.Builder(context).build()
            currentSong = song
            currentSong?.url?.apply {
                val mediaItem = MediaItem.fromUri(this)
                exoPlayer?.setMediaItem(mediaItem)
                exoPlayer?.prepare()
                exoPlayer?.play()
            }
        }
    }
}