package com.example.musicplayer.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.musicplayer.SongsListActivity
import com.example.musicplayer.databinding.CategoryitemRvBinding
import com.example.musicplayer.models.CategoryModels

class CategoryAdapter(
    private val categoryList: List<CategoryModels>
) : RecyclerView.Adapter<CategoryAdapter.MyViewHolder>() {
    @GlideModule
    class MyViewHolder(private val binding: CategoryitemRvBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bindData(category: CategoryModels) {
            binding.nameTV.text = category.name
            Glide.with(binding.coverImageV)
                .load(category.coverUrl)
                .apply(
                    RequestOptions()
                        .transform(
                            RoundedCorners(32)
                        )
                )
                .into(binding.coverImageV)

            //Start SongsList Activity
            val context = binding.root.context
            binding.root.setOnClickListener {
                SongsListActivity.categories = category
                context.startActivity(
                    Intent(context,SongsListActivity::class.java)
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = CategoryitemRvBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindData(categoryList[position])
    }
}
