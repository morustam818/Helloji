package com.rmohd8788.helloji.model


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tag_table")
data class Tags(
    @PrimaryKey(autoGenerate = true)
    val tagsKey: Int = 0,

    val tags: String
)