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
    val byteArray: ByteArray? = null, // Данные изображения для отображения
    val fileName: String? = null, // Имя файла
    val mimeType: String? = null, // MIME тип файла (image/jpeg, image/png и т.д.)
    val selectedAt: Long = 0L, // TODO: Использовать kotlinx-datetime
    val isFromCamera: Boolean = false, // Флаг - получено ли фото с камеры или из галереи
    val isSelected: Boolean = false // Флаг - выбрана ли фотография для использования
) {
    // Переопределяем equals и hashCode так как ByteArray сравнивается по ссылке
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as SelectedPhoto

        if (id != other.id) return false
        if (uri != other.uri) return false
        if (byteArray != null) {
            if (other.byteArray == null) return false
            if (!byteArray.contentEquals(other.byteArray)) return false
        } else if (other.byteArray != null) return false
        if (fileName != other.fileName) return false
        if (mimeType != other.mimeType) return false
        if (selectedAt != other.selectedAt) return false
        if (isFromCamera != other.isFromCamera) return false
        if (isSelected != other.isSelected) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + uri.hashCode()
        result = 31 * result + (byteArray?.contentHashCode() ?: 0)
        result = 31 * result + (fileName?.hashCode() ?: 0)
        result = 31 * result + (mimeType?.hashCode() ?: 0)
        result = 31 * result + selectedAt.hashCode()
        result = 31 * result + isFromCamera.hashCode()
        result = 31 * result + isSelected.hashCode()
        return result
    }
}
