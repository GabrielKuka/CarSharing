package com.car.sharing.models

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@IgnoreExtraProperties
@Parcelize
data class CarPhoto(val name: String = "", var url: String = "", var postId: String = "") :
    Parcelable