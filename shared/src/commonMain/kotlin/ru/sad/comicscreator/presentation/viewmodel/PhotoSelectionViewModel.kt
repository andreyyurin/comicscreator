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
    
    // Состояние загруженных фотографий
    private val _availablePhotos = MutableStateFlow<List<SelectedPhoto>>(emptyList())
    val availablePhotos: StateFlow<List<SelectedPhoto>> = _availablePhotos.asStateFlow()
    
    // Состояние выбранных фотографий для продолжения
    private val _selectedPhotos = MutableStateFlow<List<SelectedPhoto>>(emptyList())
    val selectedPhotos: StateFlow<List<SelectedPhoto>> = _selectedPhotos.asStateFlow()
    
    // Состояние ошибок
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    /**
     * Добавляет фотографию в список доступных из ByteArray (результат Peekaboo)
     */
    fun addPhotoFromByteArray(byteArray: ByteArray, isFromCamera: Boolean = false) {
        try {
            val timestamp = kotlin.random.Random.nextLong() // Временное решение вместо System.currentTimeMillis()
            val photo = SelectedPhoto(
                uri = "bytearray://${byteArray.size}", // Временный URI, позже можно сохранить в файл
                byteArray = byteArray, // Сохраняем данные изображения для отображения
                fileName = if (isFromCamera) "camera_photo_${timestamp}.jpg" else "gallery_photo_${timestamp}.jpg",
                mimeType = "image/jpeg",
                selectedAt = 0L, // TODO: использовать kotlinx-datetime
                isFromCamera = isFromCamera,
                isSelected = true // Автоматически выбираем добавленную фотографию
            )
            addAvailablePhoto(photo)
        } catch (e: Exception) {
            _errorMessage.value = "Ошибка при обработке фотографии: ${e.message}"
        }
    }
    
    /**
     * Добавляет фотографию в список доступных
     */
    private fun addAvailablePhoto(photo: SelectedPhoto) {
        val currentPhotos = _availablePhotos.value.toMutableList()
        currentPhotos.add(photo)
        _availablePhotos.value = currentPhotos
        
        // Обновляем список выбранных фотографий
        updateSelectedPhotos()
    }
    
    /**
     * Переключает статус выбора фотографии
     */
    fun togglePhotoSelection(photoId: String) {
        println("DEBUG: togglePhotoSelection called for photoId: $photoId")
        val currentAvailable = _availablePhotos.value.toMutableList()
        val photoIndex = currentAvailable.indexOfFirst { it.id == photoId }
        
        if (photoIndex != -1) {
            val photo = currentAvailable[photoIndex]
            val updatedPhoto = photo.copy(isSelected = !photo.isSelected)
            println("DEBUG: Photo ${photo.id} selection changed from ${photo.isSelected} to ${updatedPhoto.isSelected}")
            currentAvailable[photoIndex] = updatedPhoto
            _availablePhotos.value = currentAvailable
            
            // Обновляем список выбранных фотографий
            updateSelectedPhotos()
        } else {
            println("DEBUG: Photo with id $photoId not found")
        }
    }
    
    /**
     * Удаляет фотографию из списка доступных
     */
    fun removePhoto(photoId: String) {
        val currentAvailable = _availablePhotos.value.toMutableList()
        currentAvailable.removeAll { it.id == photoId }
        _availablePhotos.value = currentAvailable
        
        // Обновляем список выбранных фотографий
        updateSelectedPhotos()
    }
    
    /**
     * Обновляет список выбранных фотографий на основе флага isSelected
     */
    private fun updateSelectedPhotos() {
        val selectedPhotos = _availablePhotos.value.filter { it.isSelected }
        _selectedPhotos.value = selectedPhotos
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
    fun canContinue(): Boolean {
        val canContinue = _selectedPhotos.value.isNotEmpty()
        println("DEBUG: canContinue() = $canContinue, selectedPhotos count = ${_selectedPhotos.value.size}")
        return canContinue
    }
    
    /**
     * Получает список выбранных фотографий для передачи на следующий экран
     */
    fun getSelectedPhotosForNextScreen(): List<SelectedPhoto> = _selectedPhotos.value
}
