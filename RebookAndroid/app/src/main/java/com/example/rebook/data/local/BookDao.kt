package com.example.rebook.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {

    @Query("SELECT * FROM favorites")
    fun getAllFavorites(): Flow<List<BookEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(book: BookEntity)

    @Delete
    suspend fun removeFavorite(book: BookEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE key = :key)")
    suspend fun isFavorite(key: String): Boolean

    @Query("SELECT key FROM favorites")
    suspend fun getAllFavoriteKeys(): List<String>
}
