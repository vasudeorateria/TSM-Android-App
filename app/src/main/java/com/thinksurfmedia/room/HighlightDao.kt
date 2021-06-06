package com.thinksurfmedia.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.thinksurfmedia.model.Highlight
import kotlinx.coroutines.flow.Flow

@Dao
interface HighlightDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHighlights(highlight: List<Highlight>)

    @Query("SELECT * FROM HIGHLIGHT WHERE serviceId = :serviceId")
    fun getHighlights(serviceId: Int): Flow<List<Highlight>>
}