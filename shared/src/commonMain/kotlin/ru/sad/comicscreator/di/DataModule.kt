package ru.sad.comicscreator.di

import org.koin.dsl.module
import ru.sad.comicscreator.domain.repository.CharacterRepository
import ru.sad.comicscreator.domain.repository.ComicRepository
import ru.sad.comicscreator.domain.repository.PhotoRepository

/**
 * Модуль для data слоя - репозитории и data sources
 */
val dataModule = module {
    
    // Repositories
    // TODO: Добавить реализации репозиториев когда будут созданы
    // single<PhotoRepository> { PhotoRepositoryImpl(get(), get()) }
    // single<CharacterRepository> { CharacterRepositoryImpl(get(), get()) }
    // single<ComicRepository> { ComicRepositoryImpl(get(), get()) }
    
    // Database
    // TODO: Добавить Realm базу данных
    // single { RealmDatabase() }
    
    // Local Data Sources
    // TODO: Добавить локальные data sources
    // single { PhotoLocalDataSource(get()) }
    // single { CharacterLocalDataSource(get()) }
    // single { ComicLocalDataSource(get()) }
}
