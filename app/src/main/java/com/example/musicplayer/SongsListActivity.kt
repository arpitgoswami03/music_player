package com.example.musicplayer

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.musicplayer.adapter.SongListAdapter
import com.example.musicplayer.databinding.ActivitySongsListBinding
import com.example.musicplayer.models.CategoryModels

class SongsListActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySongsListBinding
    private lateinit var songListAdapter: SongListAdapter

    companion object{
        lateinit var categories: CategoryModels
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            binding.nameTextView.text = categories.name
            Glide.with(binding.coverImageView)
                .load(categories.coverUrl)
                .apply(
                    RequestOptions().transform(RoundedCorners(32))
                )
                .into(binding.coverImageView)

            setupSongRecyclerView()
        }
    }

    fun setupSongRecyclerView(){
        songListAdapter = SongListAdapter(categories.songs)
        binding.songsListRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.songsListRecyclerView.adapter = songListAdapter
    }
}