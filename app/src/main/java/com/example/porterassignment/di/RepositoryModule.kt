package com.example.porterassignment.di

import com.example.porterassignment.repository.VehicleRepository
import org.koin.dsl.module.module

internal val repositoryModule = module {

    single { VehicleRepository(apiManager = get()) }
}