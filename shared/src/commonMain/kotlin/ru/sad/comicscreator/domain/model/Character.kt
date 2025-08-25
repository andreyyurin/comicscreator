package ru.sad.comicscreator.domain.model

import kotlinx.serialization.Serializable

/**
 * Модель персонажа комикса (создается из фото пользователя)
 */
@Serializable
data class Character(
    val id: String,
    val name: String, // Имя персонажа (задается пользователем)
    val originalPhotoId: String, // ID исходного фото
    val comicStyleImageUrl: String, // URL изображения в стиле комикса
    val createdAt: Long = 0L // TODO: Использовать kotlinx-datetime
)
