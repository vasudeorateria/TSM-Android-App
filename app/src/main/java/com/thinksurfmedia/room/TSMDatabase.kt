package com.thinksurfmedia.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.thinksurfmedia.model.*
import com.thinksurfmedia.utils.DataConvertor

@Database(
    entities = [Highlight::class, Plan::class, Portfolio::class, Review::class, Services::class],
    version = 1
)
@TypeConverters(DataConvertor::class)
abstract class TSMDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "TSM_DATABASE"
        operator fun invoke(context: Context): TSMDatabase {
            return Room.databaseBuilder(context, TSMDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    abstract fun highlightDao(): HighlightDao
    abstract fun planDao(): PlanDao
    abstract fun portfolioDao(): PortfolioDao
    abstract fun reviewDao(): ReviewDao
    abstract fun servicesDao(): ServicesDao
}