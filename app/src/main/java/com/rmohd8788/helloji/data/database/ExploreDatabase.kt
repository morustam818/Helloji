package com.rmohd8788.helloji.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.rmohd8788.helloji.R
import com.rmohd8788.helloji.data.dao.ExploreDao
import com.rmohd8788.helloji.model.Explore
import com.rmohd8788.helloji.model.Trending
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Explore::class],version = 1,exportSchema = false)
abstract class ExploreDatabase : RoomDatabase() {

    abstract fun exploreDao() : ExploreDao

    companion object{

        @Volatile
        private var INSTANCE : ExploreDatabase? = null

        fun getDatabase(context: Context) : ExploreDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = databaseBuilder(
                    context.applicationContext,
                    ExploreDatabase::class.java,
                    "db_explore"
                ).build()
                INSTANCE = instance
                return instance
            }
        }

    }

}