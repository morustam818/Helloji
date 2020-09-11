package com.rmohd8788.helloji.data.repo

import androidx.lifecycle.LiveData
import com.rmohd8788.helloji.data.dao.TrendingDao
import com.rmohd8788.helloji.model.Trending

class TrendingRepo(private val trendingDao: TrendingDao) {

    val trendingRepo : LiveData<List<Trending>> = trendingDao.getAllTrendingTags()

    suspend fun insert(trending: Trending){
        trendingDao.insert(trending)
    }

    suspend fun delete(trending: Trending){
        trendingDao.delete(trending)
    }

    suspend fun update(trending: Trending){
        trendingDao.update(trending)
    }

}