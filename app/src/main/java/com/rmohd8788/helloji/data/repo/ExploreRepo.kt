package com.rmohd8788.helloji.data.repo

import androidx.lifecycle.LiveData
import com.rmohd8788.helloji.data.dao.ExploreDao
import com.rmohd8788.helloji.model.Explore

class ExploreRepo(private val exploreDao: ExploreDao) {
    val exploreRepo : LiveData<List<Explore>> = exploreDao.getAllTweets()

    val favList : LiveData<List<Explore>> = exploreDao.getFavoriteList()

    suspend fun insert(explore: Explore){
        exploreDao.insert(explore)
    }

    suspend fun delete(explore: Explore){
        exploreDao.delete(explore)
    }

    suspend fun update(explore: Explore){
        exploreDao.update(explore)
    }


}