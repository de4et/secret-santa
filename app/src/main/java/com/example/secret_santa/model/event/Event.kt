package com.example.secret_santa.model.event

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Event(
    val id: String,
    val name: String,
    val dateStart: String,
    val isLocked: Boolean = false,
    val participants: List<String> = emptyList()
) : Parcelable
