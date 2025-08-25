package ru.sad.comicscreator.platform

import android.Manifest
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * Android реализация PermissionsHelper
 */
actual class PermissionsHelper(private val activity: ComponentActivity) {

    private var permissionCallback: ((Boolean) -> Unit)? = null

    // ActivityResultLauncher для запроса разрешений - создается лениво
    private var permissionLauncher: ActivityResultLauncher<String>? = null

    private fun getOrCreatePermissionLauncher(): ActivityResultLauncher<String> {
        if (permissionLauncher == null) {
            permissionLauncher = activity.registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                permissionCallback?.invoke(isGranted)
                permissionCallback = null
            }
        }
        return permissionLauncher!!
    }
    
    /**
     * Проверяет, есть ли разрешение на камеру
     */
    actual suspend fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    /**
     * Проверяет, есть ли разрешение на галерею
     */
    actual suspend fun hasGalleryPermission(): Boolean {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
        
        return ContextCompat.checkSelfPermission(
            activity,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    /**
     * Запрашивает разрешение на камеру
     */
    actual suspend fun requestCameraPermission(): Boolean = suspendCancellableCoroutine { continuation ->
        // Используем runBlocking для вызова suspend функции
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            continuation.resume(true)
            return@suspendCancellableCoroutine
        }
        
        permissionCallback = { isGranted ->
            continuation.resume(isGranted)
        }

        getOrCreatePermissionLauncher().launch(Manifest.permission.CAMERA)
        
        continuation.invokeOnCancellation {
            permissionCallback = null
        }
    }
    
    /**
     * Запрашивает разрешение на галерею
     */
    actual suspend fun requestGalleryPermission(): Boolean = suspendCancellableCoroutine { continuation ->
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
        
        if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED) {
            continuation.resume(true)
            return@suspendCancellableCoroutine
        }
        
        permissionCallback = { isGranted ->
            continuation.resume(isGranted)
        }

        getOrCreatePermissionLauncher().launch(permission)
        
        continuation.invokeOnCancellation {
            permissionCallback = null
        }
    }
}

/**
 * Создает экземпляр PermissionsHelper для Android
 */
@Composable
actual fun createPermissionsHelper(): PermissionsHelper {
    val context = LocalContext.current
    val activity = remember(context) {
        when (context) {
            is ComponentActivity -> context
            is ContextWrapper -> context.baseContext as? ComponentActivity ?: error("PermissionsHelper requires an Activity context")
            else -> error("PermissionsHelper requires an Activity context")
        }
    }
    return remember(activity) { PermissionsHelper(activity) }
}

/**
 * Создает экземпляр PermissionsHelper для Android с активити
 */
fun createPermissionsHelper(activity: ComponentActivity): PermissionsHelper {
    return PermissionsHelper(activity)
}
