package com.example.dtos

import com.squareup.moshi.Moshi
import org.koin.dsl.module

val dtoModelsModule = module {
    single<Moshi> {
        Moshi.Builder().build()
    }
}