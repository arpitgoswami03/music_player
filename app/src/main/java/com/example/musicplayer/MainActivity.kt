package com.example.musicplayer

import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.adapter.CategoryAdapter
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

}