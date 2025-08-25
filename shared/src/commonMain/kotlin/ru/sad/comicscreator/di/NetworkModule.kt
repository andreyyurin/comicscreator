package ru.sad.comicscreator.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

/**
 * Модуль для сетевого слоя
 */
val networkModule = module {
    
    // Ktor Client configuration
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
        }
    }
    
    // Remote Data Sources
    // TODO: Добавить remote data sources когда будет готов API
    // single { PhotoRemoteDataSource(get()) }
    // single { CharacterRemoteDataSource(get()) }
    // single { ComicRemoteDataSource(get()) }
    
    // API Services
    // TODO: Добавить API сервисы
    // single { PhotoApiService(get()) }
    // single { CharacterApiService(get()) }
    // single { ComicApiService(get()) }
}
