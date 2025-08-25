package ru.sad.comicscreator.domain.model

import kotlinx.serialization.Serializable

/**
 * Модель готового комикса
 */
@Serializable
data class Comic(
    val id: String,
    val title: String, // Название комикса
    val templateId: String, // ID использованного шаблона
    val characters: List<Character>, // Персонажи, использованные в комиксе
    val customizedFrames: List<CustomizedFrame>, // Кастомизированные кадры
    val finalImageUrl: String? = null, // URL финального изображения комикса
    val createdAt: Long = 0L, // TODO: Использовать kotlinx-datetime
    val isShared: Boolean = false, // Поделился ли пользователь комиксом
    val metadata: ComicMetadata = ComicMetadata()
)

/**
 * Кастомизированный кадр комикса с пользовательскими персонажами
 */
@Serializable
data class CustomizedFrame(
    val frameId: String, // ID кадра из шаблона
    val characterPlacements: List<CharacterPlacement>, // Размещение конкретных персонажей
    val customSpeechBubbles: List<CustomSpeechBubble> // Кастомизированные реплики
)

/**
 * Размещение конкретного персонажа в кадре
 */
@Serializable
data class CharacterPlacement(
    val characterId: String, // ID персонажа
    val position: CharacterPosition // Позиция и настройки
)

/**
 * Кастомизированная реплика
 */
@Serializable
data class CustomSpeechBubble(
    val originalBubbleId: String, // ID оригинального пузыря из шаблона
    val characterId: String, // ID персонажа, который говорит
    val customText: String? = null, // Кастомный текст (если пользователь изменил)
    val isEdited: Boolean = false // Изменял ли пользователь текст
)

/**
 * Метаданные комикса
 */
@Serializable
data class ComicMetadata(
    val processingTimeMs: Long = 0, // Время обработки в миллисекундах
    val aiProcessingVersion: String = "1.0", // Версия алгоритма обработки
    val totalFrames: Int = 0, // Общее количество кадров
    val charactersUsed: Int = 0 // Количество использованных персонажей
)
