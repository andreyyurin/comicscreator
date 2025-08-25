package ru.sad.comicscreator.presentation.navigation

/**
 * Определение экранов приложения для навигации
 */
sealed class Screen(val route: String) {
    
    // Главный экран с обзором
    data object Home : Screen("home")
    
    // Экран создания персонажа
    data object CreateCharacter : Screen("create_character")
    
    // Экран выбора шаблона комикса
    data object SelectTemplate : Screen("select_template")
    
    // Экран создания комикса
    data object CreateComic : Screen("create_comic") {
        fun createRoute(templateId: String) = "$route/$templateId"
    }
    
    // Экран просмотра комикса
    data object ViewComic : Screen("view_comic") {
        fun createRoute(comicId: String) = "$route/$comicId"
    }
    
    // Экран редактирования комикса
    data object EditComic : Screen("edit_comic") {
        fun createRoute(comicId: String) = "$route/$comicId"
    }
    
    // Экран галереи персонажей
    data object CharacterGallery : Screen("character_gallery")
    
    // Экран галереи комиксов
    data object ComicGallery : Screen("comic_gallery")
    
    // Экран настроек
    data object Settings : Screen("settings")
    
    companion object {
        /**
         * Все доступные экраны
         */
        val allScreens = listOf(
            Home,
            CreateCharacter,
            SelectTemplate,
            CreateComic,
            ViewComic,
            EditComic,
            CharacterGallery,
            ComicGallery,
            Settings
        )
    }
}
