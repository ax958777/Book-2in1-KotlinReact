package com.example.rebook.repository

import com.example.rebook.data.local.BookDao
import com.example.rebook.data.local.BookEntity
import com.example.rebook.data.network.BookDto
import com.example.rebook.data.network.OpenLibraryApi
import com.example.rebook.data.network.SearchResponse
import com.example.rebook.domain.Book
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class BookRepositoryTest {

    @MockK lateinit var api: OpenLibraryApi
    @MockK lateinit var dao: BookDao

    private lateinit var repository: BookRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        repository = BookRepository(api, dao)
    }

    // ── fetchBooks ────────────────────────────────────────────────────────────

    @Test
    fun `fetchBooks returns mapped domain list on success`() = runTest {
        val dto = BookDto(
            key = "/works/OL1",
            title = "Awakenings",
            authorName = listOf("Oliver Sacks"),
            coverId = 12345,
            firstPublishYear = 1973
        )
        coEvery { api.searchBooks() } returns SearchResponse(docs = listOf(dto))

        val result = repository.fetchBooks()

        assertTrue(result.isSuccess)
        val books = result.getOrThrow()
        assertEquals(1, books.size)
        with(books.first()) {
            assertEquals("/works/OL1", key)
            assertEquals("Awakenings", title)
            assertEquals(listOf("Oliver Sacks"), authors)
            assertEquals(12345, coverId)
            assertEquals(1973, firstPublishYear)
        }
    }

    @Test
    fun `fetchBooks returns failure on api exception`() = runTest {
        coEvery { api.searchBooks() } throws RuntimeException("Network error")

        val result = repository.fetchBooks()

        assertTrue(result.isFailure)
        assertEquals("Network error", result.exceptionOrNull()?.message)
    }

    @Test
    fun `fetchBooks maps empty docs list correctly`() = runTest {
        coEvery { api.searchBooks() } returns SearchResponse(docs = emptyList())

        val result = repository.fetchBooks()

        assertTrue(result.isSuccess)
        assertTrue(result.getOrThrow().isEmpty())
    }

    // ── toggleFavorite ────────────────────────────────────────────────────────

    @Test
    fun `toggleFavorite inserts book when not already a favorite`() = runTest {
        val book = Book("/works/OL1", "Awakenings", listOf("Oliver Sacks"), 12345, 1973)
        coEvery { dao.isFavorite(book.key) } returns false
        coEvery { dao.insertFavorite(any()) } just runs

        repository.toggleFavorite(book)

        coVerify { dao.insertFavorite(any()) }
        coVerify(exactly = 0) { dao.removeFavorite(any()) }
    }

    @Test
    fun `toggleFavorite removes book when already a favorite`() = runTest {
        val book = Book("/works/OL1", "Awakenings", listOf("Oliver Sacks"), 12345, 1973)
        coEvery { dao.isFavorite(book.key) } returns true
        coEvery { dao.removeFavorite(any()) } just runs

        repository.toggleFavorite(book)

        coVerify { dao.removeFavorite(any()) }
        coVerify(exactly = 0) { dao.insertFavorite(any()) }
    }

    // ── getFavorites ──────────────────────────────────────────────────────────

    @Test
    fun `getFavorites emits mapped domain books from dao`() = runTest {
        val entity = BookEntity("/works/OL1", "Awakenings", "Oliver Sacks", 12345, 1973)
        every { dao.getAllFavorites() } returns flowOf(listOf(entity))

        val favorites = repository.getFavorites().first()

        assertEquals(1, favorites.size)
        assertEquals("/works/OL1", favorites.first().key)
    }

    // ── getFavoriteKeys ───────────────────────────────────────────────────────

    @Test
    fun `getFavoriteKeys returns set of keys from dao`() = runTest {
        coEvery { dao.getAllFavoriteKeys() } returns listOf("/works/OL1", "/works/OL2")

        val keys = repository.getFavoriteKeys()

        assertEquals(setOf("/works/OL1", "/works/OL2"), keys)
    }
}
