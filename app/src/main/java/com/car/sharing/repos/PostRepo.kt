package com.car.sharing.repos

import com.car.sharing.models.CarPhoto
import com.car.sharing.utils.IPostPhotos
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class PostRepo {

    fun retrievePhotos(query: Query, iPostPhotos: IPostPhotos){
        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                iPostPhotos.onErrorRetrieve(p0.message)
            }

            override fun onDataChange(data: DataSnapshot) {
                val list = mutableListOf<CarPhoto>()
                for (postSnapshot in data.children) {
                    val carPhoto = postSnapshot.getValue(CarPhoto::class.java)
                    list.add(carPhoto!!)
                }

                iPostPhotos.onSuccessRetrieve(list)
            }
        })
    }

}