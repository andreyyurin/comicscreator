package ru.sad.comicscreator.domain.model

import kotlinx.serialization.Serializable

/**
 * Модель для загруженного фото пользователя
 */
@Serializable
data class Photo(
    val id: String,
    val uri: String, // Локальный путь к файлу
    val uploadedAt: Long = 0L, // TODO: Использовать kotlinx-datetime
    val processedImageUrl: String? = null // URL обработанного изображения персонажа
)
