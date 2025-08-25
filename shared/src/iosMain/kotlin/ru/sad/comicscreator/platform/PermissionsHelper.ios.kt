package ru.sad.comicscreator.platform

import androidx.compose.runtime.Composable

/**
 * iOS реализация PermissionsHelper
 * На iOS Peekaboo сам обрабатывает разрешения через Info.plist
 */
class IosPermissionsHelper : PermissionsHelper {

    override var onPermissionResult: ((String, Boolean) -> Unit)? = null

    /**
     * На iOS разрешения обрабатываются автоматически через Info.plist
     */
    override suspend fun hasCameraPermission(): Boolean = true

    /**
     * На iOS разрешения обрабатываются автоматически через Info.plist
     */
    override suspend fun hasGalleryPermission(): Boolean = true

    /**
     * На iOS разрешения запрашиваются автоматически системой
     */
    override suspend fun requestCameraPermission(): Boolean = true

    /**
     * На iOS разрешения запрашиваются автоматически системой
     */
    override suspend fun requestGalleryPermission(): Boolean = true
}

/**
 * Создает экземпляр PermissionsHelper для iOS
 */
@Composable
actual fun createPermissionsHelper(): PermissionsHelper {
    return IosPermissionsHelper()
}
