package ru.sad.comicscreator.domain.usecase

import ru.sad.comicscreator.domain.model.ComicTemplate
import ru.sad.comicscreator.domain.model.Result
import ru.sad.comicscreator.domain.repository.ComicRepository

/**
 * UseCase для получения шаблонов комиксов
 */
class GetComicTemplatesUseCase(
    private val comicRepository: ComicRepository
) {
    
    /**
     * Получает все доступные шаблоны комиксов
     * 
     * @param onlyPopular если true, возвращает только популярные шаблоны
     * @return Result со списком шаблонов
     */
    suspend operator fun invoke(onlyPopular: Boolean = false): Result<List<ComicTemplate>> {
        return if (onlyPopular) {
            comicRepository.getPopularTemplates()
        } else {
            comicRepository.getComicTemplates()
        }
    }
    
    /**
     * Получает шаблоны по категории
     * 
     * @param category категория шаблонов
     * @return Result со списком отфильтрованных шаблонов
     */
    suspend fun getByCategory(category: String): Result<List<ComicTemplate>> {
        return when (val result = comicRepository.getComicTemplates()) {
            is Result.Success -> {
                val filteredTemplates = result.data.filter { template ->
                    template.category.equals(category, ignoreCase = true)
                }
                Result.Success(filteredTemplates)
            }
            is Result.Error -> result
            is Result.Loading -> result
        }
    }
    
    /**
     * Получает шаблоны, подходящие для указанного количества персонажей
     * 
     * @param charactersCount количество персонажей
     * @return Result со списком подходящих шаблонов
     */
    suspend fun getForCharactersCount(charactersCount: Int): Result<List<ComicTemplate>> {
        return when (val result = comicRepository.getComicTemplates()) {
            is Result.Success -> {
                val filteredTemplates = result.data.filter { template ->
                    charactersCount >= template.minCharacters && charactersCount <= template.maxCharacters
                }
                Result.Success(filteredTemplates)
            }
            is Result.Error -> result
            is Result.Loading -> result
        }
    }
}
