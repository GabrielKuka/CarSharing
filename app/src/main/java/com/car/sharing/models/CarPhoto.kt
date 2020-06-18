package com.car.sharing.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class CarPhoto(val name: String = "", var url: String = "", var postId: String = "")