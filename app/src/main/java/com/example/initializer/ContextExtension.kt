package com.example.initializer

import android.annotation.SuppressLint
import android.content.Context
import androidx.core.content.ContextCompat
import org.koin.core.Koin

@SuppressLint("WrongConstant")
internal fun Context.getKoinAsSystemService(): Koin {
    val koin = ContextCompat.getSystemService(this, Koin::class.java)

    return koin ?: error("Koin not initialized yet.")
}
