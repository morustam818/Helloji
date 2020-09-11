package com.rmohd8788.helloji.model


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "explore_table")
data class Explore(
    val imageId : Int,
    val username : String,
    val jobTitle : String,
    val description : String,
    val tags : String,
    var isSaved : Boolean = false,
    @PrimaryKey(autoGenerate = true)
    val exploreKey : Int = 0)