package ru.sad.comicscreator.platform

/**
 * iOS реализация PermissionsHelper
 * На iOS Peekaboo сам обрабатывает разрешения через Info.plist
 */
actual class PermissionsHelper {
    
    /**
     * На iOS разрешения обрабатываются автоматически через Info.plist
     */
    actual suspend fun hasCameraPermission(): Boolean = true
    
    /**
     * На iOS разрешения обрабатываются автоматически через Info.plist
     */
    actual suspend fun hasGalleryPermission(): Boolean = true
    
    /**
     * На iOS разрешения запрашиваются автоматически системой
     */
    actual suspend fun requestCameraPermission(): Boolean = true
    
    /**
     * На iOS разрешения запрашиваются автоматически системой
     */
    actual suspend fun requestGalleryPermission(): Boolean = true
}

/**
 * Создает экземпляр PermissionsHelper для iOS
 */
actual fun createPermissionsHelper(): PermissionsHelper {
    return PermissionsHelper()
}
