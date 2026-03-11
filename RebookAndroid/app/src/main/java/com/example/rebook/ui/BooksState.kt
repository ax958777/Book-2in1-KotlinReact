package com.example.rebook.ui

import com.example.rebook.domain.Book

data class BooksState(
    val books: List<Book> = emptyList(),
    val favoriteKeys: Set<String> = emptySet(),
    val isLoading: Boolean = false,
    val error: String? = null
)
