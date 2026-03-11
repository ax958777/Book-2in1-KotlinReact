package com.example.rebook.ui

import androidx.annotation.experimental.Experimental
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.rebook.domain.Book

// ─── Color Palette ────────────────────────────────────────────────────────────
private val BackgroundStart = Color(0xFF0F0C29)
private val BackgroundEnd   = Color(0xFF302B63)
private val CardBg          = Color(0xFF1E1B4B)
private val AccentPurple    = Color(0xFF7C3AED)
private val AccentPink      = Color(0xFFEC4899)
private val TextPrimary     = Color(0xFFF1F5F9)
private val TextSecondary   = Color(0xFF94A3B8)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BooksScreen(
    viewModel: BooksViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(BackgroundStart, BackgroundEnd)
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // ── Top Bar ────────────────────────────────────────────────────
            TopAppBar(
                title = {
                    Text(
                        text = "📚 Rebook",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = TextPrimary
                    )
                },
                actions = {
                    IconButton(onClick = { viewModel.loadBooks() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = TextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )

            // ── Content ────────────────────────────────────────────────────
            when {
                state.isLoading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = AccentPurple)
                    }
                }

                state.error != null -> {
                    ErrorView(message = state.error!!, onRetry = { viewModel.loadBooks() })
                }

                state.books.isEmpty() -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No books found.", color = TextSecondary)
                    }
                }

                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.books, key = { it.key }) { book ->
                            BookCard(
                                book = book,
                                isFavorite = book.key in state.favoriteKeys,
                                onToggleFavorite = { viewModel.toggleFavorite(book) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BookCard(
    book: Book,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ── Cover Image ────────────────────────────────────────────────
            book.coverUrl?.let { url ->
                AsyncImage(
                    model = url,
                    contentDescription = "Cover of ${book.title}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(70.dp, 100.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF312E81))
                )
            } ?: Box(
                modifier = Modifier
                    .size(70.dp, 100.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        Brush.verticalGradient(listOf(AccentPurple, AccentPink))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("📖", fontSize = 28.sp)
            }

            Spacer(modifier = Modifier.width(14.dp))

            // ── Details ────────────────────────────────────────────────────
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = book.title,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                if (book.authors.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = book.authors.joinToString(", "),
                        color = AccentPurple,
                        fontSize = 13.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                book.firstPublishYear?.let { year ->
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "$year",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
            }

            // ── Favorite Button ────────────────────────────────────────────
            IconButton(onClick = onToggleFavorite) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                    tint = if (isFavorite) AccentPink else TextSecondary
                )
            }
        }
    }
}

@Composable
fun ErrorView(message: String, onRetry: () -> Unit) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("⚠️ Something went wrong", color = TextPrimary, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            Text(message, color = TextSecondary, fontSize = 13.sp)
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(containerColor = AccentPurple)
            ) {
                Text("Retry", color = Color.White)
            }
        }
    }
}
