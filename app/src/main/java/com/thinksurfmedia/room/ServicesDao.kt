package com.thinksurfmedia.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.thinksurfmedia.model.Services
import kotlinx.coroutines.flow.Flow

@Dao
interface ServicesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServices(services: kotlin.collections.List<com.thinksurfmedia.model.Services>)

    @Query("DELETE FROM Services")
    suspend fun deleteServices()

    @Query("SELECT * FROM Services")
    fun getServices(): Flow<List<Services>>
}