package com.thinksurfmedia.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.thinksurfmedia.model.Portfolio
import kotlinx.coroutines.flow.Flow

@Dao
interface PortfolioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPortfolios(portfolio: List<Portfolio>)

    @Query("DELETE FROM Portfolio")
    suspend fun deletePortfolios()

    @Query("SELECT * FROM Portfolio")
    fun getPortfolios(): Flow<List<Portfolio>>
}