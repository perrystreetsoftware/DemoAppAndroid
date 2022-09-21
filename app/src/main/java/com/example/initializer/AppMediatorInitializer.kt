package com.example.initializer

import android.content.Context
import androidx.startup.Initializer

class AppMediatorInitializer : Initializer<Unit> {
    override fun create(context: Context) {
//        val koin = context.getKoinAsSystemService()
//        val appMediator = koin.get<ApplicationMediator>()
//        appMediator.onApplicationStarted()
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf(
            KoinInitializer::class.java,
        )
    }
}
