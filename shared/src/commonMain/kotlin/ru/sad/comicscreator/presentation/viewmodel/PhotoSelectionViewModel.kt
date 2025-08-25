package ru.sad.comicscreator.presentation.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.sad.comicscreator.domain.model.SelectedPhoto

/**
 * ViewModel для экрана выбора фотографий
 */
class PhotoSelectionViewModel : ViewModel() {
    
    // Состояние выбранных фотографий
    private val _selectedPhotos = MutableStateFlow<List<SelectedPhoto>>(emptyList())
    val selectedPhotos: StateFlow<List<SelectedPhoto>> = _selectedPhotos.asStateFlow()
    
    // Состояние ошибок
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    /**
     * Добавляет фотографию в список выбранных из ByteArray (результат Peekaboo)
     */
    fun addPhotoFromByteArray(byteArray: ByteArray, isFromCamera: Boolean = false) {
        try {
            val timestamp = kotlin.random.Random.nextLong() // Временное решение вместо System.currentTimeMillis()
            val photo = SelectedPhoto(
                uri = "bytearray://${byteArray.size}", // Временный URI, позже можно сохранить в файл
                fileName = if (isFromCamera) "camera_photo_${timestamp}.jpg" else "gallery_photo_${timestamp}.jpg",
                mimeType = "image/jpeg",
                selectedAt = 0L, // TODO: использовать kotlinx-datetime
                isFromCamera = isFromCamera
            )
            addPhoto(photo)
        } catch (e: Exception) {
            _errorMessage.value = "Ошибка при обработке фотографии: ${e.message}"
        }
    }
    
    /**
     * Добавляет фотографию в список выбранных
     */
    private fun addPhoto(photo: SelectedPhoto) {
        val currentPhotos = _selectedPhotos.value.toMutableList()
        currentPhotos.add(photo)
        _selectedPhotos.value = currentPhotos
    }
    
    /**
     * Удаляет фотографию из списка выбранных
     */
    fun removePhoto(photoId: String) {
        val currentPhotos = _selectedPhotos.value.toMutableList()
        currentPhotos.removeAll { it.id == photoId }
        _selectedPhotos.value = currentPhotos
    }
    
    /**
     * Очищает сообщение об ошибке
     */
    fun clearError() {
        _errorMessage.value = null
    }
    
    /**
     * Проверяет, можно ли продолжить (есть ли выбранные фотографии)
     */
    fun canContinue(): Boolean = _selectedPhotos.value.isNotEmpty()
}
