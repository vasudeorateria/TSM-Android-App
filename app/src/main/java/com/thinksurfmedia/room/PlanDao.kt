package com.thinksurfmedia.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.thinksurfmedia.model.Plan
import kotlinx.coroutines.flow.Flow

@Dao
interface PlanDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlans(plan: List<Plan>)

    @Query("SELECT * FROM `Plan` WHERE serviceId = :serviceId")
    fun getPlans(serviceId: Int): Flow<List<Plan>>
}