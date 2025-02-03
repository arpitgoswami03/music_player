package com.example.musicplayer

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.musicplayer.adapter.CategoryAdapter
import com.example.musicplayer.adapter.SectionSongListAdapter
import com.example.musicplayer.databinding.ActivityMainBinding
import com.example.musicplayer.models.CategoryModels
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getCategories()
        setupSection("section_1",
            binding.section1MainLayout,
            binding.section1Title,
            binding.section1RecyclerView
        )
        setupSection("section_2",
            binding.section2MainLayout,
            binding.section2Title,
            binding.section2RecyclerView
        )
        setupSection("section_3",
            binding.section3MainLayout,
            binding.section3Title,
            binding.section3RecyclerView
        )
    }

    override fun onResume() {
        super.onResume()
        showPlayerView()
    }

    fun showPlayerView(){
        binding.playerView.setOnClickListener {
            startActivity(Intent(this,PlayerActivity::class.java))
        }
        MyExoPlayer.getCurrentSong()?.let {
            binding.playerView.visibility = View.VISIBLE
            binding.songTitleTextView.text = "Now Playing : " + it.title
            Glide.with(binding.songCoverImageView).load(it.coverUrl)
                .apply(
                    RequestOptions().transform(RoundedCorners(32))
                ).into(binding.songCoverImageView)
        } ?: run{
            binding.playerView.visibility = View.GONE
        }
    }

    private fun getCategories(){
        FirebaseFirestore.getInstance().collection("category").get()
            .addOnSuccessListener {
                val categoryList = it.toObjects(CategoryModels::class.java)
                setupCategoryRecyclerView(categoryList)
            }
            .addOnFailureListener {

            }
    }

    private fun setupCategoryRecyclerView(categoryList: List<CategoryModels>){
        categoryAdapter = CategoryAdapter(categoryList)
        binding.categoriesRecyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false)
        binding.categoriesRecyclerView.adapter = categoryAdapter
    }

    private fun setupSection(id : String,
                             mainLayout : RelativeLayout,
                             titleView : TextView,
                             recyclerView : RecyclerView
    ){
        FirebaseFirestore.getInstance().collection("sections")
            .document(id)
            .get()
            .addOnSuccessListener {
                val section = it.toObject(CategoryModels::class.java)
                section?.apply {
                    mainLayout.visibility = View.VISIBLE
                    titleView.text = name
                    recyclerView.layoutManager = LinearLayoutManager(
                        this@MainActivity,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                    recyclerView.adapter = SectionSongListAdapter(songs)
                    mainLayout.setOnClickListener {
                        SongsListActivity.categories = section
                        startActivity(
                            Intent(this@MainActivity,SongsListActivity::class.java)
                        )
                    }
                }
            }
    }

}