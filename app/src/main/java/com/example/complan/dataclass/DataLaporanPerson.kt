package com.example.complan.dataclass

import android.provider.ContactsContract.DisplayPhoto
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class DataLaporanPerson(
    val uidStudent: String? = null,
    val suspect: String? = null,
    val violationType: String? = null,
    val description: String? = null,
    val photo: String?= null,
    val timestamp: Long? = null,
    val status : Boolean? = null
){
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
}