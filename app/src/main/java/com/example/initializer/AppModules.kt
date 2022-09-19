package com.example.initializer

import com.example.domainmodels.domainModelsModule
import com.example.logic.logicModule
import com.example.repositories.repositoriesModule
import com.example.viewmodels.viewModelModule
import networkLogicApi
import org.koin.core.module.Module

val appModules: List<Module> = viewModelModule + logicModule + repositoriesModule + networkLogicApi + domainModelsModule