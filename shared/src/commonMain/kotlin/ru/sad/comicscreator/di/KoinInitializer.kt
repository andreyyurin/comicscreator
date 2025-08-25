package ru.sad.comicscreator.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

/**
 * Инициализация Koin для shared модуля
 */
fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(allModules)
}

/**
 * Инициализация Koin только для shared части (для использования в iOS)
 */
fun initKoin() = initKoin {}
