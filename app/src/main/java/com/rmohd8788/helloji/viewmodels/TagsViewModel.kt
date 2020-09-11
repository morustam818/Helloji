package com.rmohd8788.helloji.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.rmohd8788.helloji.data.database.TagDatabase
import com.rmohd8788.helloji.data.repo.TagRepo
import com.rmohd8788.helloji.model.Tags
import kotlinx.coroutines.launch

class TagsViewModel(application : Application) : AndroidViewModel(application) {

    val tagsRepo : TagRepo

    val allTags : LiveData<List<Tags>>

    init {
        val tagsDao = TagDatabase.getDatabase(application).tagDao()
        tagsRepo = TagRepo(tagsDao)
        allTags = tagsRepo.tagsRepo
    }

    fun insert(tags: Tags) = viewModelScope.launch(){
        tagsRepo.insert(tags)
    }

    fun delete(tags: Tags) = viewModelScope.launch(){
        tagsRepo.delete(tags)
    }

    fun update(tags: Tags) = viewModelScope.launch(){
        tagsRepo.update(tags)
    }

}