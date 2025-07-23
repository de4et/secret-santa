package com.example.secret_santa.model.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id : String,
    val name: String,
    val pathToImage: String? = null,
    val wishes: String? = null,
    var recipientId: String? = null
): Parcelable
