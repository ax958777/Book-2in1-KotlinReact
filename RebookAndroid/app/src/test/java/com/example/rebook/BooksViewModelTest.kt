package com.example.rebook.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.rebook.domain.Book
import com.example.rebook.repository.BookRepository
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BooksViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @MockK lateinit var repository: BookRepository

    private lateinit var viewModel: BooksViewModel

    private val sampleBook = Book(
        key = "/works/OL1",
        title = "Awakenings",
        authors = listOf("Oliver Sacks"),
        coverId = 12345,
        firstPublishYear = 1973
    )

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        // Default stubs to prevent init{} crashes
        every { repository.getFavorites() } returns flowOf(emptyList())
        coEvery { repository.fetchBooks() } returns Result.success(emptyList())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // ── Initial load ──────────────────────────────────────────────────────────

    @Test
    fun `initial state has isLoading false and empty books`() {
        viewModel = BooksViewModel(repository)
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.books.isEmpty())
    }

    @Test
    fun `loadBooks sets books on success`() = runTest {
        coEvery { repository.fetchBooks() } returns Result.success(listOf(sampleBook))
        viewModel = BooksViewModel(repository)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(1, state.books.size)
        assertEquals("Awakenings", state.books.first().title)
        assertFalse(state.isLoading)
        assertNull(state.error)
    }

    @Test
    fun `loadBooks sets error on failure`() = runTest {
        coEvery { repository.fetchBooks() } returns Result.failure(RuntimeException("Failed"))
        viewModel = BooksViewModel(repository)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("Failed", state.error)
        assertTrue(state.books.isEmpty())
    }

    // ── Refresh ───────────────────────────────────────────────────────────────

    @Test
    fun `calling loadBooks again refreshes the list`() = runTest {
        coEvery { repository.fetchBooks() } returnsMany listOf(
            Result.success(emptyList()),
            Result.success(listOf(sampleBook))
        )
        viewModel = BooksViewModel(repository)
        advanceUntilIdle()

        viewModel.loadBooks()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(1, state.books.size)
    }

    // ── toggleFavorite ────────────────────────────────────────────────────────

    @Test
    fun `toggleFavorite calls repository toggleFavorite`() = runTest {
        coEvery { repository.toggleFavorite(any()) } just runs
        viewModel = BooksViewModel(repository)
        advanceUntilIdle()

        viewModel.toggleFavorite(sampleBook)
        advanceUntilIdle()

        coVerify { repository.toggleFavorite(sampleBook) }
    }

    // ── Favorites observer ────────────────────────────────────────────────────

    @Test
    fun `favorite keys update in state when repository emits favorites`() = runTest {
        every { repository.getFavorites() } returns flowOf(listOf(sampleBook))
        viewModel = BooksViewModel(repository)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.favoriteKeys.contains(sampleBook.key))
    }

    @Test
    fun `state clears error after successful retry`() = runTest {
        coEvery { repository.fetchBooks() } returnsMany listOf(
            Result.failure(RuntimeException("Network error")),
            Result.success(listOf(sampleBook))
        )
        viewModel = BooksViewModel(repository)
        advanceUntilIdle()
        assertEquals("Network error", viewModel.uiState.value.error)

        viewModel.loadBooks()
        advanceUntilIdle()

        assertNull(viewModel.uiState.value.error)
        assertEquals(1, viewModel.uiState.value.books.size)
    }
}
