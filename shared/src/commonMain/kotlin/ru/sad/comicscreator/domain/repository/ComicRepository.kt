package ru.sad.comicscreator.domain.repository

import ru.sad.comicscreator.domain.model.Comic
import ru.sad.comicscreator.domain.model.ComicTemplate
import ru.sad.comicscreator.domain.model.Result

/**
 * Интерфейс репозитория для работы с комиксами и шаблонами
 */
interface ComicRepository {
    
    /**
     * Получает доступные шаблоны комиксов
     */
    suspend fun getComicTemplates(): Result<List<ComicTemplate>>
    
    /**
     * Получает шаблон по ID
     */
    suspend fun getTemplateById(id: String): Result<ComicTemplate>
    
    /**
     * Получает популярные шаблоны
     */
    suspend fun getPopularTemplates(): Result<List<ComicTemplate>>
    
    /**
     * Создает комикс на основе шаблона
     */
    suspend fun createComic(comic: Comic): Result<Comic>
    
    /**
     * Сохраняет комикс локально
     */
    suspend fun saveComic(comic: Comic): Result<Comic>
    
    /**
     * Получает комикс по ID
     */
    suspend fun getComicById(id: String): Result<Comic>
    
    /**
     * Получает все сохраненные комиксы
     */
    suspend fun getAllComics(): Result<List<Comic>>
    
    /**
     * Удаляет комикс
     */
    suspend fun deleteComic(id: String): Result<Unit>
    
    /**
     * Генерирует финальное изображение комикса
     */
    suspend fun generateComicImage(comic: Comic): Result<String>
    
    /**
     * Делится комиксом (отправка на сервер)
     */
    suspend fun shareComic(comicId: String): Result<String>
}
