package com.rmohd8788.helloji.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.rmohd8788.helloji.data.database.ExploreDatabase
import com.rmohd8788.helloji.data.repo.ExploreRepo
import com.rmohd8788.helloji.model.Explore
import kotlinx.coroutines.launch

class ExploreViewModel(application: Application) : AndroidViewModel(application) {

    private val repo : ExploreRepo

    val allTweets : LiveData<List<Explore>>
    val allFavList : LiveData<List<Explore>>

    init {
        val exploreDao = ExploreDatabase.getDatabase(application).exploreDao()
        repo = ExploreRepo(exploreDao)
        allTweets = repo.exploreRepo
        allFavList = repo.favList
    }

    //launching the coroutines
    fun insert(explore: Explore) = viewModelScope.launch(){
        repo.insert(explore)
    }

    fun update(explore: Explore) = viewModelScope.launch(){
        repo.update(explore)
    }


    fun delete(explore: Explore)= viewModelScope.launch(){
        repo.delete(explore)
    }

}