package com.rmohd8788.helloji.viewmodels


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rmohd8788.helloji.model.Trending

class TrendingViewModel : ViewModel() {

    private val getTrendingList = MutableLiveData<List<Trending>>()

    private var trendingData : MutableList<Trending> = mutableListOf()

    init {
        populateList()
    }

    //dummy data
    private fun populateList(){
        trendingData = mutableListOf(
            Trending("#lockdown","18 people nearby"),
            Trending("#covid","20 people nearby"),
            Trending("#corona","10 people nearby"),
            Trending("#upse","5 people nearby"),
            Trending("#ipl","100 people nearby"),
            Trending("#raina","200 people nearby"),
            Trending("#mahi","100 people nearby"),
            Trending("#kohli","200 people nearby")
        )

        getTrendingList.value = trendingData
    }

    fun getAllTrendingData() : MutableLiveData<List<Trending>>{
        return getTrendingList
    }
}