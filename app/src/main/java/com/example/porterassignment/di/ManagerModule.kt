package com.example.porterassignment.di

import com.example.porterassignment.apiManager.ApiManager
import org.koin.dsl.module.module

internal val managerModule = module {

    single { ApiManager(apiService = get()) }
}