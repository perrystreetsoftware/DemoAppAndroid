package com.example.interfaces

import org.koin.dsl.module

val networkLogicApiMocks = module {
    single<ITravelAdvisoriesApi> {
        MockTravelApi()
    }
}
