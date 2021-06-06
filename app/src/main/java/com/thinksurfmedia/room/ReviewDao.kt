package com.thinksurfmedia.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.thinksurfmedia.model.Review
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReviews(review: List<Review>)

    @Query("DELETE FROM Review")
    suspend fun deleteReviews()

    @Query("SELECT * FROM Review")
    fun getReviews(): Flow<List<Review>>
}