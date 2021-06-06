package com.thinksurfmedia.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Review(
    @PrimaryKey val id: Int,
    val image: String,
    val review: String
)
