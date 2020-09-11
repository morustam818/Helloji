package com.rmohd8788.helloji.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.rmohd8788.helloji.model.Tags

@Dao
interface TagsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(tags: Tags)

    @Update
    suspend fun update(tags: Tags)

    @Delete
    suspend fun delete(tags: Tags)

    @Query("SELECT * FROM tag_table")
    fun getAllTags() : LiveData<List<Tags>>

}