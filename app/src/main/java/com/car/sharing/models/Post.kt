package com.car.sharing.models

class Post(
    val carName: String,
    val carDescription: String,
    val price: Double,
    val owner: String,
    val isReserved: Boolean = false
) {
}