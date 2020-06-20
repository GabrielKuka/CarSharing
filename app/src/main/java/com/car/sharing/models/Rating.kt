package com.car.sharing.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Rating(
    val rateId: String = "",
    val rateNumber: Double = 0.0,
    val review: String = "",
    val reviewerName: String = "",
    val reviewerEmail: String = "",
    val postId: String = ""
)