package com.rmohd8788.helloji.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trending")
data class Trending(
    val tags: String,
    val nearbyPeople: String,
    @PrimaryKey(autoGenerate = true)
    val trendingKey: Int = 0
)