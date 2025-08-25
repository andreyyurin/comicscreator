package ru.sad.comicscreator.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sad.comicscreator.domain.model.Result

/**
 * Базовый ViewModel с общей функциональностью
 */
abstract class BaseViewModel<UiState, UiEvent> : ViewModel() {
    
    // Приватный мутируемый state
    protected abstract val _uiState: MutableStateFlow<UiState>
    
    // Публичный read-only state
    abstract val uiState: StateFlow<UiState>
    
    /**
     * Обрабатывает UI события
     */
    abstract fun onEvent(event: UiEvent)
    
    /**
     * Безопасное выполнение suspend функций с обработкой ошибок
     */
    protected fun <T> safeCall(
        onLoading: () -> Unit = {},
        onSuccess: (T) -> Unit = {},
        onError: (Throwable) -> Unit = {},
        action: suspend () -> Result<T>
    ) {
        viewModelScope.launch {
            try {
                onLoading()
                when (val result = action()) {
                    is Result.Success -> onSuccess(result.data)
                    is Result.Error -> onError(result.exception)
                    is Result.Loading -> onLoading()
                }
            } catch (e: Exception) {
                onError(e)
            }
        }
    }
    
    /**
     * Обновляет состояние UI
     */
    protected inline fun updateState(update: (UiState) -> UiState) {
        _uiState.value = update(_uiState.value)
    }
}
