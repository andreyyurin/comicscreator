package ru.sad.comicscreator.domain.usecase

import ru.sad.comicscreator.domain.model.Character
import ru.sad.comicscreator.domain.model.Result
import ru.sad.comicscreator.domain.repository.CharacterRepository
import ru.sad.comicscreator.domain.repository.PhotoRepository

/**
 * UseCase для создания персонажа из фотографии
 */
class CreateCharacterUseCase(
    private val characterRepository: CharacterRepository,
    private val photoRepository: PhotoRepository
) {
    
    /**
     * Создает персонажа из загруженной фотографии
     * 
     * @param photoId ID фотографии
     * @param characterName имя персонажа
     * @return Result с созданным персонажем
     */
    suspend operator fun invoke(
        photoId: String,
        characterName: String
    ): Result<Character> {
        // Проверяем существование фото
        return when (val photoResult = photoRepository.getPhotoById(photoId)) {
            is Result.Success -> {
                // Валидируем имя персонажа
                if (characterName.isBlank()) {
                    Result.Error(IllegalArgumentException("Character name cannot be empty"))
                } else {
                    // Создаем персонажа
                    characterRepository.createCharacterFromPhoto(photoId, characterName.trim())
                }
            }
            is Result.Error -> Result.Error(photoResult.exception)
            is Result.Loading -> Result.Loading
        }
    }
}
