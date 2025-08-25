package ru.sad.comicscreator.domain.model

import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Модель выбранной фотографии для создания персонажа комикса
 */
@Serializable
data class SelectedPhoto @OptIn(ExperimentalUuidApi::class) constructor(
    val id: String = Uuid.random().toString(),
    val uri: String, // Локальный путь к файлу или URI
    val fileName: String? = null, // Имя файла
    val mimeType: String? = null, // MIME тип файла (image/jpeg, image/png и т.д.)
    val selectedAt: Long = 0L, // TODO: Использовать kotlinx-datetime
    val isFromCamera: Boolean = false // Флаг - получено ли фото с камеры или из галереи
)
