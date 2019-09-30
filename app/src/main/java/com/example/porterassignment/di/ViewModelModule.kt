package com.example.porterassignment.di

import com.example.porterassignment.ui.viewModel.HomeViewModel
import com.example.porterassignment.ui.viewModel.SelectPlaceViewModel
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

/**
 * Module will initialize all viewmodels
 * @author Sumit Lakra
 * @date 09/02/19
 */
internal val viewModelModule = module {
    // initialize all viewmodule here
    viewModel { HomeViewModel(repository = get()) }
    viewModel { SelectPlaceViewModel() }
}