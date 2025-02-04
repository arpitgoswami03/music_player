package com.example.musicplayer

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.whenCreated
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.musicplayer.adapter.CategoryAdapter
import com.example.musicplayer.adapter.SectionSongListAdapter
import com.example.musicplayer.databinding.ActivityMainBinding
import com.example.musicplayer.models.CategoryModels
import com.example.musicplayer.models.SongModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObjects

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getCategories()
        setupSection(
            "section_1",
            binding.section1MainLayout,
            binding.section1Title,
            binding.section1RecyclerView
        )
        setupSection(
            "section_2",
            binding.section2MainLayout,
            binding.section2Title,
            binding.section2RecyclerView
        )
        setupSection(
            "section_3",
            binding.section3MainLayout,
            binding.section3Title,
            binding.section3RecyclerView
        )
        setupMostPlayed(
            "most_played",
            binding.mostplayedMainLayout,
            binding.mostplayedTitle,
            binding.mostplayedRecyclerView
        )
        binding.optionBtn.setOnClickListener {
            showMenu()
        }
    }

    private fun showMenu() {
        val popupMenu = PopupMenu(this, binding.optionBtn)
        val inflater = popupMenu.menuInflater
        inflater.inflate(R.menu.menu, popupMenu.menu)
        popupMenu.show()
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.logout -> {
                    logout()
                    true
                }
            }
            false
        }
    }

    private fun logout(){
        MyExoPlayer.getInstance()?.release()
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun onResume() {
        super.onResume()
        showPlayerView()
    }

    private fun showPlayerView() {
        binding.playerView.setOnClickListener {
            startActivity(Intent(this, PlayerActivity::class.java))
        }
        MyExoPlayer.getCurrentSong()?.let {
            binding.playerView.visibility = View.VISIBLE
            binding.songTitleTextView.text = "${R.string.now_playing}" + ": " + it.title
            Glide.with(binding.songCoverImageView).load(it.coverUrl)
                .apply(
                    RequestOptions().transform(RoundedCorners(32))
                ).into(binding.songCoverImageView)
        } ?: run {
            binding.playerView.visibility = View.GONE
        }
    }

    private fun getCategories() {
        FirebaseFirestore.getInstance().collection("category").get()
            .addOnSuccessListener {
                val categoryList = it.toObjects(CategoryModels::class.java)
                setupCategoryRecyclerView(categoryList)
            }
            .addOnFailureListener {

            }
    }

    private fun setupCategoryRecyclerView(categoryList: List<CategoryModels>) {
        categoryAdapter = CategoryAdapter(categoryList)
        binding.categoriesRecyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.categoriesRecyclerView.adapter = categoryAdapter
    }


    private fun setupSection(
        id: String,
        mainLayout: RelativeLayout,
        titleView: TextView,
        recyclerView: RecyclerView,
    ) {
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
                            Intent(this@MainActivity, SongsListActivity::class.java)
                        )
                    }
                }
            }
    }

    private fun setupMostPlayed(
        id: String,
        mainLayout: RelativeLayout,
        titleView: TextView,
        recyclerView: RecyclerView,
    ) {
        FirebaseFirestore.getInstance().collection("sections")
            .document(id)
            .get()
            .addOnSuccessListener {

                FirebaseFirestore.getInstance().collection("songs")
                    .orderBy("count", Query.Direction.DESCENDING)
                    .limit(5)
                    .get()
                    .addOnSuccessListener { songListSnapshot ->
                        val songModelList = songListSnapshot.toObjects<SongModel>()
                        val songIdList = songModelList.map {
                            it.id
                        }.toList()
                        val section = it.toObject(CategoryModels::class.java)
                        section?.apply {
                            section.songs = songIdList
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
                                    Intent(this@MainActivity, SongsListActivity::class.java)
                                )
                            }
                        }
                    }
            }
    }

}