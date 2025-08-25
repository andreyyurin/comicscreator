package ru.sad.comicscreator.domain.usecase

import ru.sad.comicscreator.domain.model.*
import ru.sad.comicscreator.domain.repository.ComicRepository
import ru.sad.comicscreator.domain.repository.CharacterRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * UseCase для создания комикса
 */
class CreateComicUseCase(
    private val comicRepository: ComicRepository,
    private val characterRepository: CharacterRepository
) {
    
    /**
     * Создает комикс на основе шаблона и выбранных персонажей
     * 
     * @param templateId ID шаблона комикса
     * @param characterIds список ID персонажей
     * @param title название комикса
     * @return Result с созданным комиксом
     */
    @OptIn(ExperimentalUuidApi::class)
    suspend operator fun invoke(
        templateId: String,
        characterIds: List<String>,
        title: String
    ): Result<Comic> {
        // Получаем шаблон
        return when (val templateResult = comicRepository.getTemplateById(templateId)) {
            is Result.Success -> {
                val template = templateResult.data
                
                // Валидируем количество персонажей
                if (characterIds.size < template.minCharacters || characterIds.size > template.maxCharacters) {
                    return Result.Error(
                        IllegalArgumentException(
                            "Template requires ${template.minCharacters}-${template.maxCharacters} characters, " +
                                    "but ${characterIds.size} provided"
                        )
                    )
                }
                
                // Получаем персонажей
                val charactersResults = characterIds.map { characterId ->
                    characterRepository.getCharacterById(characterId)
                }
                
                // Проверяем, что все персонажи найдены
                val characters = mutableListOf<Character>()
                for (result in charactersResults) {
                    when (result) {
                        is Result.Success -> characters.add(result.data)
                        is Result.Error -> return Result.Error(result.exception)
                        is Result.Loading -> return Result.Loading
                    }
                }
                
                // Создаем кастомизированные кадры на основе шаблона
                val customizedFrames = template.frames.map { frame ->
                    CustomizedFrame(
                        frameId = frame.id,
                        characterPlacements = frame.characterPositions.mapNotNull { position ->
                            if (position.characterIndex < characters.size) {
                                CharacterPlacement(
                                    characterId = characters[position.characterIndex].id,
                                    position = position
                                )
                            } else null
                        },
                        customSpeechBubbles = frame.speechBubbles.map { bubble ->
                            val character = characters.getOrNull(bubble.characterIndex)
                            CustomSpeechBubble(
                                originalBubbleId = bubble.id,
                                characterId = character?.id ?: characters.first().id,
                                customText = null,
                                isEdited = false
                            )
                        }
                    )
                }
                
                // Создаем комикс
                val comic = Comic(
                    id = Uuid.random().toString(),
                    title = title.ifBlank { "My Comic" },
                    templateId = templateId,
                    characters = characters,
                    customizedFrames = customizedFrames,
                    metadata = ComicMetadata(
                        totalFrames = template.frames.size,
                        charactersUsed = characters.size
                    )
                )
                
                // Сохраняем комикс
                comicRepository.saveComic(comic)
            }
            is Result.Error -> Result.Error(templateResult.exception)
            is Result.Loading -> Result.Loading
        }
    }
}
