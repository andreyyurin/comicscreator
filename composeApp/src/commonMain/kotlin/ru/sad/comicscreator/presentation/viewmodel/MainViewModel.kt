package ru.sad.comicscreator.presentation.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.sad.comicscreator.domain.model.Character
import ru.sad.comicscreator.domain.model.Comic
import ru.sad.comicscreator.domain.model.ComicTemplate
import ru.sad.comicscreator.domain.usecase.GetComicTemplatesUseCase
import ru.sad.comicscreator.domain.repository.CharacterRepository
import ru.sad.comicscreator.domain.repository.ComicRepository

/**
 * Основной ViewModel для главного экрана приложения
 */
class MainViewModel(
    private val getComicTemplatesUseCase: GetComicTemplatesUseCase,
    private val characterRepository: CharacterRepository,
    private val comicRepository: ComicRepository
) : BaseViewModel<MainUiState, MainUiEvent>() {
    
    override val _uiState = MutableStateFlow(MainUiState())
    override val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()
    
    init {
        loadInitialData()
    }
    
    override fun onEvent(event: MainUiEvent) {
        when (event) {
            is MainUiEvent.LoadCharacters -> loadCharacters()
            is MainUiEvent.LoadTemplates -> loadTemplates()
            is MainUiEvent.LoadComics -> loadComics()
            is MainUiEvent.RefreshData -> loadInitialData()
            is MainUiEvent.SelectTemplate -> selectTemplate(event.template)
            is MainUiEvent.NavigateToCreateCharacter -> navigateToCreateCharacter()
            is MainUiEvent.NavigateToCreateComic -> navigateToCreateComic(event.templateId)
        }
    }
    
    private fun loadInitialData() {
        loadCharacters()
        loadTemplates()
        loadComics()
    }
    
    private fun loadCharacters() {
        safeCall<List<Character>>(
            onLoading = {
                updateState { it.copy(isLoadingCharacters = true) }
            },
            onSuccess = { characters ->
                updateState { 
                    it.copy(
                        characters = characters,
                        isLoadingCharacters = false,
                        errorMessage = null
                    )
                }
            },
            onError = { error ->
                updateState { 
                    it.copy(
                        isLoadingCharacters = false,
                        errorMessage = "Failed to load characters: ${error.message}"
                    )
                }
            }
        ) {
            characterRepository.getAllCharacters()
        }
    }
    
    private fun loadTemplates() {
        safeCall<List<ComicTemplate>>(
            onLoading = {
                updateState { it.copy(isLoadingTemplates = true) }
            },
            onSuccess = { templates ->
                updateState { 
                    it.copy(
                        templates = templates,
                        popularTemplates = templates.filter { template -> template.isPopular },
                        isLoadingTemplates = false
                    )
                }
            },
            onError = { error ->
                updateState { 
                    it.copy(
                        isLoadingTemplates = false,
                        errorMessage = "Failed to load templates: ${error.message}"
                    )
                }
            }
        ) {
            getComicTemplatesUseCase()
        }
    }
    
    private fun loadComics() {
        safeCall<List<Comic>>(
            onLoading = {
                updateState { it.copy(isLoadingComics = true) }
            },
            onSuccess = { comics ->
                updateState { 
                    it.copy(
                        recentComics = comics.sortedByDescending { comic -> comic.createdAt }.take(5),
                        isLoadingComics = false
                    )
                }
            },
            onError = { error ->
                updateState { 
                    it.copy(
                        isLoadingComics = false,
                        errorMessage = "Failed to load comics: ${error.message}"
                    )
                }
            }
        ) {
            comicRepository.getAllComics()
        }
    }
    
    private fun selectTemplate(template: ComicTemplate) {
        updateState { it.copy(selectedTemplate = template) }
    }
    
    private fun navigateToCreateCharacter() {
        updateState { it.copy(navigationEvent = NavigationEvent.CreateCharacter) }
    }
    
    private fun navigateToCreateComic(templateId: String) {
        updateState { it.copy(navigationEvent = NavigationEvent.CreateComic(templateId)) }
    }
    
    fun clearNavigationEvent() {
        updateState { it.copy(navigationEvent = null) }
    }
}

/**
 * Состояние UI для главного экрана
 */
data class MainUiState(
    val characters: List<Character> = emptyList(),
    val templates: List<ComicTemplate> = emptyList(),
    val popularTemplates: List<ComicTemplate> = emptyList(),
    val recentComics: List<Comic> = emptyList(),
    val selectedTemplate: ComicTemplate? = null,
    val isLoadingCharacters: Boolean = false,
    val isLoadingTemplates: Boolean = false,
    val isLoadingComics: Boolean = false,
    val errorMessage: String? = null,
    val navigationEvent: NavigationEvent? = null
) {
    val isLoading: Boolean = isLoadingCharacters || isLoadingTemplates || isLoadingComics
    val hasCharacters: Boolean = characters.isNotEmpty()
    val hasTemplates: Boolean = templates.isNotEmpty()
    val canCreateComic: Boolean = hasCharacters && hasTemplates
}

/**
 * События UI для главного экрана
 */
sealed class MainUiEvent {
    data object LoadCharacters : MainUiEvent()
    data object LoadTemplates : MainUiEvent()
    data object LoadComics : MainUiEvent()
    data object RefreshData : MainUiEvent()
    data class SelectTemplate(val template: ComicTemplate) : MainUiEvent()
    data object NavigateToCreateCharacter : MainUiEvent()
    data class NavigateToCreateComic(val templateId: String) : MainUiEvent()
}

/**
 * События навигации
 */
sealed class NavigationEvent {
    data object CreateCharacter : NavigationEvent()
    data class CreateComic(val templateId: String) : NavigationEvent()
    data class ViewComic(val comicId: String) : NavigationEvent()
}
