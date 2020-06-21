package com.car.sharing.models

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@IgnoreExtraProperties
@Parcelize
data class Post(
    val postId: String = "",
    val carName: String? = "",
    val carDescription: String? = "",
    val price: Double? = 0.0,
    val ownerName: String? = "",
    val ownerEmail: String? = "",
    val mainPhoto: String? = "",
    val rateNumber: Double? = 0.0,
    val isReserved: Boolean = false
) : Parcelable