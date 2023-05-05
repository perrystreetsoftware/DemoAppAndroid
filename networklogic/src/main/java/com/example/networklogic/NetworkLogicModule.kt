package com.example.networklogic

import com.example.interfaces.ITravelAdvisoriesApi
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val networkLogicApi = module {
    factoryOf(::TravelAdvisoriesApi) bind ITravelAdvisoriesApi::class
}
