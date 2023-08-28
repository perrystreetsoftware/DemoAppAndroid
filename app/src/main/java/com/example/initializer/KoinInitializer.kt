@file:Suppress("unused")

package com.example.initializer

import android.content.Context
import androidx.startup.Initializer
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

class KoinInitializer : Initializer<KoinApplication> {
    override fun create(context: Context): KoinApplication {
        return startKoin {
            modules(appModules)
        }
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}
