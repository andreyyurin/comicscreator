package ru.sad.comicscreator.domain.repository

import ru.sad.comicscreator.domain.model.Character
import ru.sad.comicscreator.domain.model.Result

/**
 * Интерфейс репозитория для работы с персонажами
 */
interface CharacterRepository {
    
    /**
     * Создает персонажа из фотографии
     */
    suspend fun createCharacterFromPhoto(
        photoId: String,
        name: String
    ): Result<Character>
    
    /**
     * Сохраняет персонажа локально
     */
    suspend fun saveCharacter(character: Character): Result<Character>
    
    /**
     * Получает персонажа по ID
     */
    suspend fun getCharacterById(id: String): Result<Character>
    
    /**
     * Получает всех созданных персонажей
     */
    suspend fun getAllCharacters(): Result<List<Character>>
    
    /**
     * Удаляет персонажа
     */
    suspend fun deleteCharacter(id: String): Result<Unit>
    
    /**
     * Обновляет персонажа
     */
    suspend fun updateCharacter(character: Character): Result<Character>
}
