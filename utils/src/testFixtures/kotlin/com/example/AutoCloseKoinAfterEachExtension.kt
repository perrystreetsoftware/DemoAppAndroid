package com.example
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.koin.core.context.stopKoin


class AutoCloseKoinAfterEachExtension : AfterEachCallback {

    override fun afterEach(context: ExtensionContext?) {
        stopKoin()
    }
}
