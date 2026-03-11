package com.example.rebook.data.network

import com.google.gson.annotations.SerializedName

data class BookDto(
    @SerializedName("key") val key: String = "",
    @SerializedName("title") val title: String = "",
    @SerializedName("author_name") val authorName: List<String>? = null,
    @SerializedName("cover_i") val coverId: Int? = null,
    @SerializedName("first_publish_year") val firstPublishYear: Int? = null
)

data class SearchResponse(
    @SerializedName("docs") val docs: List<BookDto> = emptyList()
)
