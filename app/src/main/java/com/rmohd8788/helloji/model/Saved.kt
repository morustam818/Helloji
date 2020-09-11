package com.rmohd8788.helloji.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_table")
class Saved(
    val imageId: Int,
    val username: String,
    val jobTitle: String,
    val description: String,
    val tags: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)