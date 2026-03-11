package com.example.rebook.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class BookEntity(
    @PrimaryKey val key: String,
    val title: String,
    val authorName: String,     // comma-separated list
    val coverId: Int?,
    val firstPublishYear: Int?
)
