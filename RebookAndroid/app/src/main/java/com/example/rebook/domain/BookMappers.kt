package com.example.rebook.domain

import com.example.rebook.data.local.BookEntity
import com.example.rebook.data.network.BookDto

fun BookDto.toDomain(): Book = Book(
    key = key,
    title = title,
    authors = authorName ?: emptyList(),
    coverId = coverId,
    firstPublishYear = firstPublishYear
)

fun Book.toEntity(): BookEntity = BookEntity(
    key = key,
    title = title,
    authorName = authors.joinToString(","),
    coverId = coverId,
    firstPublishYear = firstPublishYear
)

fun BookEntity.toDomain(): Book = Book(
    key = key,
    title = title,
    authors = if (authorName.isBlank()) emptyList() else authorName.split(","),
    coverId = coverId,
    firstPublishYear = firstPublishYear
)
