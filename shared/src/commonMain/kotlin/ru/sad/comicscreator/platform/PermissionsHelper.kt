package ru.sad.comicscreator.platform

import androidx.compose.runtime.Composable

/**
 * Platform-specific интерфейс для работы с разрешениями
 */
interface PermissionsHelper {

    /**
     * Callback для запроса разрешения
     */
    var onPermissionResult: ((String, Boolean) -> Unit)?

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
@Composable
expect fun createPermissionsHelper(): PermissionsHelper

