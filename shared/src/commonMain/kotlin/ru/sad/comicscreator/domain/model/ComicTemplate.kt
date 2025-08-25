package ru.sad.comicscreator.domain.model

import kotlinx.serialization.Serializable

/**
 * Модель шаблона комикса с репликами
 */
@Serializable
data class ComicTemplate(
    val id: String,
    val name: String, // Название шаблона
    val description: String, // Описание сценки
    val backgroundImageUrl: String, // URL фонового изображения
    val frames: List<ComicFrame>, // Кадры комикса
    val category: String, // Категория (юмор, романтика, дружба и т.д.)
    val minCharacters: Int = 1, // Минимальное количество персонажей
    val maxCharacters: Int = 2, // Максимальное количество персонажей
    val isPopular: Boolean = false // Популярный шаблон
)

/**
 * Кадр комикса с позициями персонажей и репликами
 */
@Serializable
data class ComicFrame(
    val id: String,
    val order: Int, // Порядок кадра
    val characterPositions: List<CharacterPosition>, // Позиции персонажей в кадре
    val speechBubbles: List<SpeechBubble> // Реплики в кадре
)

/**
 * Позиция персонажа в кадре
 */
@Serializable
data class CharacterPosition(
    val characterIndex: Int, // Индекс персонажа (0 - первый, 1 - второй)
    val x: Float, // Позиция X (от 0 до 1)
    val y: Float, // Позиция Y (от 0 до 1)
    val scale: Float = 1.0f, // Масштаб персонажа
    val rotation: Float = 0f // Поворот в градусах
)

/**
 * Реплика персонажа
 */
@Serializable
data class SpeechBubble(
    val id: String,
    val characterIndex: Int, // Индекс персонажа, который говорит
    val text: String, // Текст реплики
    val x: Float, // Позиция X (от 0 до 1)
    val y: Float, // Позиция Y (от 0 до 1)
    val bubbleType: BubbleType = BubbleType.SPEECH // Тип пузыря
)

/**
 * Тип пузыря с репликой
 */
@Serializable
enum class BubbleType {
    SPEECH, // Обычная речь
    THOUGHT, // Мысли
    SHOUT, // Крик/восклицание
    WHISPER // Шепот
}
