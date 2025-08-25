package ru.sad.comicscreator.platform

/**
 * Platform-specific интерфейс для работы с разрешениями
 */
expect class PermissionsHelper {
    
    /**
     * Проверяет, есть ли разрешение на камеру
     */
    suspend fun hasCameraPermission(): Boolean
    
    /**
     * Проверяет, есть ли разрешение на галерею
     */
    suspend fun hasGalleryPermission(): Boolean
    
    /**
     * Запрашивает разрешение на камеру
     */
    suspend fun requestCameraPermission(): Boolean
    
    /**
     * Запрашивает разрешение на галерею
     */
    suspend fun requestGalleryPermission(): Boolean
}

/**
 * Создает экземпляр PermissionsHelper для текущей платформы
 */
expect fun createPermissionsHelper(): PermissionsHelper
