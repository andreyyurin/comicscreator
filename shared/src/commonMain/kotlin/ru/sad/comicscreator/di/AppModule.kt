package ru.sad.comicscreator.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.sad.comicscreator.domain.usecase.CreateCharacterUseCase
import ru.sad.comicscreator.domain.usecase.CreateComicUseCase
import ru.sad.comicscreator.domain.usecase.GetComicTemplatesUseCase
import ru.sad.comicscreator.presentation.viewmodel.PhotoSelectionViewModel

/**
 * Модуль Koin для основных зависимостей приложения
 */
val appModule = module {
    
    // Use Cases
    // TODO: Добавить когда будут реализованы репозитории
    // single { CreateCharacterUseCase(get(), get()) }
    // single { CreateComicUseCase(get(), get()) }
    // single { GetComicTemplatesUseCase(get()) }
    
    // ViewModels
    viewModel { PhotoSelectionViewModel() }
    
    // TODO: Добавить остальные ViewModels когда будут реализованы репозитории
    // factory { MainViewModel(get(), get(), get()) }
}

/**
 * Все модули приложения
 */
val allModules = listOf(
    appModule,
    dataModule,
    networkModule
)
