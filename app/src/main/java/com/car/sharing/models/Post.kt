package com.car.sharing.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Post(
    val carName: String? = "",
    val carDescription: String? = "",
    val price: Double? = 0.0,
    val owner: String? = "",
    val isReserved: Boolean = false
)