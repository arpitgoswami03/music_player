package com.example.musicplayer.models

data class CategoryModels(
    val name: String,
    val coverUrl: String,
    var songs: List<String>
){
    constructor(): this("","", listOf())
}

data class SongModel(
    val id: String,
    val title: String,
    val singer_name: String,
    val url: String,
    val coverUrl: String,
) {
    constructor() : this("", "", "", "", "")
}