package com.example.domainmodels

import com.squareup.moshi.Moshi
import org.koin.dsl.module

val domainModelsModule = module {
    single<Moshi> {
        Moshi.Builder().build()
    }
}