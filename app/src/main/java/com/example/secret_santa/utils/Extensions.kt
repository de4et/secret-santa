package com.example.secret_santa.utils

import android.os.Build
import android.os.Parcelable
import androidx.fragment.app.Fragment

inline fun <reified T : Parcelable?> Fragment.getParcelable(key: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arguments?.getParcelable(key, T::class.java)
    } else {
        arguments?.getParcelable<T>(key)
    }
}