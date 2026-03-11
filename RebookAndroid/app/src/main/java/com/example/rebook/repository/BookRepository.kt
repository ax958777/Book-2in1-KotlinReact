package com.example.rebook.repository

import com.example.rebook.data.local.BookDao
import com.example.rebook.data.network.OpenLibraryApi
import com.example.rebook.domain.Book
import com.example.rebook.domain.toDomain
import com.example.rebook.domain.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepository @Inject constructor(
    private val api: OpenLibraryApi,
    private val dao: BookDao
) {
    /** Fetch books from the remote API */
    suspend fun fetchBooks(): Result<List<Book>> = runCatching {
        api.searchBooks().docs.map { it.toDomain() }
    }

    /** Observe persisted favorites as a Flow */
    fun getFavorites(): Flow<List<Book>> =
        dao.getAllFavorites().map { entities -> entities.map { it.toDomain() } }

    /** Return all favorited keys at a point in time */
    suspend fun getFavoriteKeys(): Set<String> =
        dao.getAllFavoriteKeys().toSet()

    /** Toggle the favorite state of a book */
    suspend fun toggleFavorite(book: Book) {
        if (dao.isFavorite(book.key)) {
            dao.removeFavorite(book.toEntity())
        } else {
            dao.insertFavorite(book.toEntity())
        }
    }
}
