package com.example.logic
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val logicModule = module {
    factoryOf(::CountryDetailsLogic)
    factoryOf(::CountryListLogic)
    factoryOf(::ServerStatusLogic)
}