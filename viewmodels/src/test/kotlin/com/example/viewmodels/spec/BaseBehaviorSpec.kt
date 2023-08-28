package com.example.viewmodels.spec

import com.example.interfaces.networkLogicApiMocks
import com.example.logic.logicModule
import com.example.repositories.repositoriesModule
import com.example.viewmodels.viewModelModule
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.BehaviorSpec
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.test.KoinTest

open class BaseBehaviorSpec : BehaviorSpec(), KoinTest {

    private val modulesToLoad: List<Module> =
        viewModelModule + logicModule + repositoriesModule + networkLogicApiMocks

    override fun isolationMode() = IsolationMode.InstancePerLeaf

    override fun extensions(): List<Extension> = listOf(RxSchedulersSpecExtension())

    override suspend fun beforeSpec(spec: Spec) {
        super.beforeSpec(spec)
        startKoin {
            modules(modulesToLoad)
        }
    }

    override suspend fun afterSpec(spec: Spec) {
        super.afterSpec(spec)
        stopKoin()
    }
}
