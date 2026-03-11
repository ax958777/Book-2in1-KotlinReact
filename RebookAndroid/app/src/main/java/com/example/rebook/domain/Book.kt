package com.example.rebook.domain

data class Book(
    val key: String,
    val title: String,
    val authors: List<String>,
    val coverId: Int?,
    val firstPublishYear: Int?
) {
    val coverUrl: String?
        get() = coverId?.let { "https://covers.openlibrary.org/b/id/$it-M.jpg" }
}
