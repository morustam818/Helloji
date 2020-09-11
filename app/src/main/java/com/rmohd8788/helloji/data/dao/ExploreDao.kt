package com.rmohd8788.helloji.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.rmohd8788.helloji.model.Explore

@Dao
interface ExploreDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(explore: Explore)

    @Update
    suspend fun update(explore: Explore)

    @Delete
    suspend fun delete(explore: Explore)

    @Query("SELECT * FROM explore_table")
    fun getAllTweets() : LiveData<List<Explore>>

    @Query("SELECT * FROM explore_table WHERE isSaved =1" )
    fun getFavoriteList() : LiveData<List<Explore>>

}