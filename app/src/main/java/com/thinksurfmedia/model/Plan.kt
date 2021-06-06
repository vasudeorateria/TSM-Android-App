package com.thinksurfmedia.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Plan(
    @PrimaryKey val id: Int,
    val color: String,
    val features: List<String>,
    val name: String,
    var price: String,
    val serviceId: Int
) : Parcelable