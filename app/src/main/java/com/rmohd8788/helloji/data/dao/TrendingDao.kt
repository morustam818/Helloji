package com.rmohd8788.helloji.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.rmohd8788.helloji.model.Trending

@Dao
interface TrendingDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(trending: Trending)

    @Update
    suspend fun update(trending: Trending)

    @Delete
    suspend fun delete(trending: Trending)

    @Query("SELECT * FROM trending")
    fun getAllTrendingTags() : LiveData<List<Trending>>

}