package ru.sad.comicscreator.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.preat.peekaboo.image.picker.SelectionMode
import com.preat.peekaboo.image.picker.rememberImagePickerLauncher
import com.preat.peekaboo.image.picker.toImageBitmap
import com.preat.peekaboo.ui.camera.PeekabooCamera
import com.preat.peekaboo.ui.camera.rememberPeekabooCameraState
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.koin.compose.viewmodel.koinViewModel
import ru.sad.comicscreator.domain.model.SelectedPhoto
import ru.sad.comicscreator.platform.PermissionsHelper
import ru.sad.comicscreator.platform.createPermissionsHelper
import ru.sad.comicscreator.presentation.navigation.Screen
import ru.sad.comicscreator.presentation.viewmodel.PhotoSelectionViewModel

@Composable
fun PhotoSelectionScreen(
    onBackPressed: () -> Unit,
    onContinueClick: () -> Unit,
    viewModel: PhotoSelectionViewModel = koinViewModel()
) {
    // Состояние из ViewModel
    val availablePhotos by viewModel.availablePhotos.collectAsStateWithLifecycle()
    val selectedPhotos by viewModel.selectedPhotos.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()

    var showCamera by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val permissionsHelper = createPermissionsHelper()
    
    // Launcher для выбора из галереи
    val galleryLauncher = rememberImagePickerLauncher(
        selectionMode = SelectionMode.Multiple(maxSelection = 10),
        scope = rememberCoroutineScope(),
        onResult = { byteArrays ->
            byteArrays.forEach { byteArray ->
                viewModel.addPhotoFromByteArray(byteArray, false)
            }
        }
    )
    
    // Состояние камеры
    val cameraState = rememberPeekabooCameraState(
        onCapture = { byteArray ->
            byteArray?.let { 
                viewModel.addPhotoFromByteArray(it, true)
                showCamera = false
            }
        }
    )
    
    // Показываем экран камеры если выбрана камера
    if (showCamera) {
        CameraScreen(
            cameraState = cameraState,
            onBack = { showCamera = false }
        )
        return
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
    ) {
        // Заголовок с кнопкой назад
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Кнопка назад
            Card(
                modifier = Modifier
                    .size(40.dp)
                    .clickable { onBackPressed() },
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF5F5F5)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "←",
                        fontSize = 20.sp,
                        color = Color.Black
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Text(
                text = "Загрузите\nфото",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            
            Spacer(modifier = Modifier.width(56.dp)) // Баланс для центрирования
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Подзаголовок
        Text(
            text = "Выберите фотографии",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Загрузите фото себя и друзей, чтобы превратить их в персонажей комикса",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            lineHeight = 20.sp
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Сетка фотографий
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.weight(1f)
        ) {
            // Отображаем загруженные фотографии
            items(availablePhotos) { photo ->
                PhotoItem(
                    photo = photo,
                    onRemove = { viewModel.removePhoto(photo.id) },
                    onSelect = { viewModel.togglePhotoSelection(photo.id) }
                )
            }
            
            // Кнопка добавления фото
            item {
                AddPhotoItem(
                    onClick = { /* Пока заглушка */ }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Кнопки галерея и камера
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Кнопка галереи
            Button(
                onClick = { 
                    scope.launch {
                        if (permissionsHelper.requestGalleryPermission()) {
                            galleryLauncher.launch()
                        } else {
                            viewModel.clearError()
                            // TODO: Показать сообщение об отказе в разрешении
                        }
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF9C27B0) // Фиолетовый
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("📁", fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Галерея",
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            
            // Кнопка камеры
            Button(
                onClick = { 
                    scope.launch {
                        if (permissionsHelper.requestCameraPermission()) {
                            showCamera = true
                        } else {
                            viewModel.clearError()
                            // TODO: Показать сообщение об отказе в разрешении
                        }
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2196F3) // Синий
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("📷", fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Камера",
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Кнопка продолжить
        Button(
            onClick = onContinueClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2196F3) // Синий
            ),
            shape = RoundedCornerShape(28.dp),
            enabled = viewModel.canContinue() // Активна только при наличии фото
        ) {
            Text(
                text = "Продолжить",
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun CameraScreen(
    cameraState: com.preat.peekaboo.ui.camera.PeekabooCameraState,
    onBack: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        PeekabooCamera(
            state = cameraState,
            modifier = Modifier.fillMaxSize(),
            permissionDeniedContent = {
                // UI при отказе в разрешениях
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Необходимо разрешение на камеру",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Пожалуйста, предоставьте разрешение на использование камеры в настройках",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        )
        
        // Кнопка назад
        Card(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .size(48.dp)
                .clickable { onBack() },
            colors = CardDefaults.cardColors(
                containerColor = Color.Black.copy(alpha = 0.5f)
            ),
            shape = RoundedCornerShape(24.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "←",
                    color = Color.White,
                    fontSize = 20.sp
                )
            }
        }
    }
}

@Composable
fun PhotoItem(
    photo: SelectedPhoto,
    onRemove: () -> Unit,
    onSelect: () -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onSelect() }
            .border(
                width = if (photo.isSelected) 3.dp else 0.dp,
                color = if (photo.isSelected) Color(0xFFE0E0E0) else Color.Transparent, // Перламутровый цвет
                shape = RoundedCornerShape(16.dp)
            )
            .background(Color(0xFF6200EE).copy(alpha = 0.1f))
    ) {
        // Отображение изображения
        photo.byteArray?.let { byteArray ->
            val imageBitmap = remember(byteArray) {
                try {
                    byteArray.toImageBitmap()
                } catch (e: Exception) {
                    null
                }
            }
            
            if (imageBitmap != null) {
                androidx.compose.foundation.Image(
                    bitmap = imageBitmap,
                    contentDescription = "Выбранная фотография",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                PhotoPlaceholder(photo.isFromCamera)
            }
        } ?: run {
            // Если нет данных изображения, показываем заглушку
            PhotoPlaceholder(photo.isFromCamera)
        }
        
        // Индикатор выбора
        if (photo.isSelected) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
                    .size(24.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF5F5F5)), // Светло-перламутровый фон
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "✓",
                    color = Color(0xFF6200EE), // Оставляем фиолетовый для контраста
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        
        // Кнопка удаления
        Card(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .size(24.dp)
                .clickable { onRemove() },
            colors = CardDefaults.cardColors(
                containerColor = Color.Red.copy(alpha = 0.8f)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "×",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun PhotoPlaceholder(isFromCamera: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (isFromCamera) "📷" else "🖼️",
            fontSize = 48.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = if (isFromCamera) "Камера" else "Галерея",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun AddPhotoItem(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clickable { onClick() }
            .border(
                width = 2.dp,
                color = Color.Gray.copy(alpha = 0.3f),
                shape = RoundedCornerShape(16.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "👤+",
                fontSize = 32.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Добавить фото",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}
