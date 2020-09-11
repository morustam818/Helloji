package com.rmohd8788.helloji.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rmohd8788.helloji.data.dao.TrendingDao
import com.rmohd8788.helloji.model.Trending

@Database(entities = [Trending::class],version = 1,exportSchema = false)
abstract class TrendingDatabase : RoomDatabase() {

    abstract fun trendingDao() : TrendingDao

    companion object{

        @Volatile
        private var INSTANCE : TrendingDatabase? = null

        fun getDatabase(context: Context) : TrendingDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TrendingDatabase::class.java,
                    "trending_db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }

    }

}