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
class AndroidPermissionsHelper(private val activity: ComponentActivity) : PermissionsHelper {

    override var onPermissionResult: ((String, Boolean) -> Unit)? = null

    private var currentPermission: String? = null

    /**
     * Проверяет, есть ли разрешение на камеру
     */
    override suspend fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Проверяет, есть ли разрешение на галерею
     */
    override suspend fun hasGalleryPermission(): Boolean {
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
    override suspend fun requestCameraPermission(): Boolean = suspendCancellableCoroutine { continuation ->
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            continuation.resume(true)
            return@suspendCancellableCoroutine
        }

        currentPermission = Manifest.permission.CAMERA
        onPermissionResult = { permission, isGranted ->
            if (permission == Manifest.permission.CAMERA) {
                continuation.resume(isGranted)
                currentPermission = null
                onPermissionResult = null
            }
        }

        requestPermission(Manifest.permission.CAMERA)

        continuation.invokeOnCancellation {
            currentPermission = null
            onPermissionResult = null
        }
    }

    /**
     * Запрашивает разрешение на галерею
     */
    override suspend fun requestGalleryPermission(): Boolean = suspendCancellableCoroutine { continuation ->
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED) {
            continuation.resume(true)
            return@suspendCancellableCoroutine
        }

        currentPermission = permission
        onPermissionResult = { perm, isGranted ->
            if (perm == permission) {
                continuation.resume(isGranted)
                currentPermission = null
                onPermissionResult = null
            }
        }

        requestPermission(permission)

        continuation.invokeOnCancellation {
            currentPermission = null
            onPermissionResult = null
        }
    }

    /**
     * Запрашивает разрешение через системный диалог
     */
    private fun requestPermission(permission: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.requestPermissions(arrayOf(permission), 1001)
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
    return remember(activity) { AndroidPermissionsHelper(activity) }
}

/**
 * Обработчик результатов разрешений для активности
 * Этот метод должен быть вызван в onRequestPermissionsResult активности
 */
fun handlePermissionResult(permissionsHelper: AndroidPermissionsHelper, requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    if (requestCode == 1001 && permissions.isNotEmpty() && grantResults.isNotEmpty()) {
        val permission = permissions[0]
        val isGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED
        permissionsHelper.onPermissionResult?.invoke(permission, isGranted)
    }
}
