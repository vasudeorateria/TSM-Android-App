package com.thinksurfmedia.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Highlight(
    @PrimaryKey val id: Int,
    val animation: String,
    val image: String,
    val name: String,
    val description: String,
    val serviceId: Int
) : Parcelable