package com.example.musicplayer.models

data class SongModel(
    val id: String,
    val title: String,
    val singer_name: String,
    val url: String,
    val coverUrl: String,
) {
    constructor() : this("", "", "", "", "")
}

