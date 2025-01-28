package com.example.musicplayer.models

data class CategoryModels(
    val name: String,
    val coverUrl: String,
    val songs: List<String>
){
    constructor(): this("","", listOf())
}
