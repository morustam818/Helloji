package com.rmohd8788.helloji.data.database

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rmohd8788.helloji.data.dao.TagsDao
import com.rmohd8788.helloji.model.Tags

@Database(entities = [Tags::class],version = 1,exportSchema = false)
abstract class TagDatabase : RoomDatabase(){

    abstract fun tagDao() : TagsDao

    companion object{

        @Volatile
        private var INSTANCE : TagDatabase? = null

        fun getDatabase(context: Context) : TagDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TagDatabase::class.java,
                    "tags_db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }

    }

}