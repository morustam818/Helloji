package com.rmohd8788.helloji.data.repo

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.rmohd8788.helloji.data.dao.TagsDao
import com.rmohd8788.helloji.model.Tags

class TagRepo(private val tagsDao: TagsDao) {

    val tagsRepo : LiveData<List<Tags>> = tagsDao.getAllTags()

    suspend fun insert(tags: Tags){
        tagsDao.insert(tags)
    }
    suspend fun delete(tags: Tags){
        tagsDao.delete(tags)
    }
    suspend fun update(tags: Tags){
        tagsDao.update(tags)
    }

}