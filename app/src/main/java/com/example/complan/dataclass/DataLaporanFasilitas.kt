package com.example.complan.dataclass

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class DataLaporanFasilitas(
    val uid :String? = null,
    val facilityName: String? = null,
    val facilityLocation: String? = null,
    val description: String? = null,
    val photo: String? = null,
    val timestamp: Long? = null,
    val status: Boolean? =null
){
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
}