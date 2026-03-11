package com.example.rebook.data.network

import retrofit2.http.GET

interface OpenLibraryApi {

    @GET("search.json?q=oliver+sacks&limit=20")
    suspend fun searchBooks(): SearchResponse
}
