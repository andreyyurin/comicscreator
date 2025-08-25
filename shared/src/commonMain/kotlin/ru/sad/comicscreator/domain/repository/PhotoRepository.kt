package ru.sad.comicscreator.domain.repository

import ru.sad.comicscreator.domain.model.Photo
import ru.sad.comicscreator.domain.model.Result

/**
 * Интерфейс репозитория для работы с фотографиями
 */
interface PhotoRepository {
    
    /**
     * Сохраняет фото локально
     */
    suspend fun savePhoto(photo: Photo): Result<Photo>
    
    /**
     * Получает фото по ID
     */
    suspend fun getPhotoById(id: String): Result<Photo>
    
    /**
     * Получает все сохраненные фото
     */
    suspend fun getAllPhotos(): Result<List<Photo>>
    
    /**
     * Удаляет фото
     */
    suspend fun deletePhoto(id: String): Result<Unit>
    
    /**
     * Загружает фото на сервер для обработки
     */
    suspend fun uploadPhotoForProcessing(photo: Photo): Result<String>
}
