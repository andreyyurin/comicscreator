package ru.sad.comicscreator.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.sad.comicscreator.domain.model.SelectedPhoto

/**
 * ViewModel для экрана создания персонажей
 * Управляет состоянием выбранных фотографий и процессом создания персонажей
 */
class CharacterCreationViewModel : ViewModel() {
    
    // Выбранные фотографии с предыдущего экрана
    private val _selectedPhotos = MutableStateFlow<List<SelectedPhoto>>(emptyList())
    val selectedPhotos: StateFlow<List<SelectedPhoto>> = _selectedPhotos.asStateFlow()
    
    // Сгенерированные персонажи (пока что пустой список)
    private val _generatedCharacters = MutableStateFlow<List<SelectedPhoto>>(emptyList())
    val generatedCharacters: StateFlow<List<SelectedPhoto>> = _generatedCharacters.asStateFlow()
    
    // Состояние загрузки создания персонажей
    private val _isCreatingCharacters = MutableStateFlow(false)
    val isCreatingCharacters: StateFlow<Boolean> = _isCreatingCharacters.asStateFlow()
    
    // Состояние ошибки
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    /**
     * Устанавливает выбранные фотографии с предыдущего экрана
     */
    fun setSelectedPhotos(photos: List<SelectedPhoto>) {
        _selectedPhotos.value = photos
    }
    
    /**
     * Создает персонажей из выбранных фотографий
     * Пока что это заглушка, в будущем будет AI генерация
     */
    fun createCharacters() {
        viewModelScope.launch {
            try {
                _isCreatingCharacters.value = true
                _error.value = null
                
                // Имитация загрузки AI генерации
                kotlinx.coroutines.delay(2000)
                
                // Пока что создаем заглушки персонажей
                val characters = _selectedPhotos.value.map { photo ->
                    SelectedPhoto(
                        id = "character_${photo.id}",
                        uri = photo.uri,
                        byteArray = photo.byteArray, // Пока что используем те же фото
                        isSelected = false
                    )
                }
                
                _generatedCharacters.value = characters
                
            } catch (e: Exception) {
                _error.value = "Ошибка при создании персонажей: ${e.message}"
            } finally {
                _isCreatingCharacters.value = false
            }
        }
    }
    
    /**
     * Проверяет, можно ли продолжить (есть ли сгенерированные персонажи)
     */
    fun canContinue(): Boolean {
        return _generatedCharacters.value.isNotEmpty()
    }
    
    /**
     * Очищает ошибку
     */
    fun clearError() {
        _error.value = null
    }
}
