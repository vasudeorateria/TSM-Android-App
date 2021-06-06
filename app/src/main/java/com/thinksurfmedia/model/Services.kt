package com.thinksurfmedia.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Services(
    @PrimaryKey val id: Int,
    val name: String,
    val image: String,
    val description: String
) : Parcelable