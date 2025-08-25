package ru.sad.comicscreator.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import kotlinx.serialization.json.Json
import org.koin.compose.viewmodel.koinViewModel
import ru.sad.comicscreator.presentation.navigation.Screen
import ru.sad.comicscreator.presentation.viewmodel.CharacterCreationViewModel
import ru.sad.comicscreator.data.GlobalPhotoStorage

/**
 * Экран создания персонажей
 * Отображает выбранные фотографии и позволяет создать персонажей
 */
@Composable
fun CharacterCreationScreen(
    onNavigate: (String) -> Unit,
    viewModel: CharacterCreationViewModel = koinViewModel()
) {
    
    val selectedPhotos by viewModel.selectedPhotos.collectAsState()
    val generatedCharacters by viewModel.generatedCharacters.collectAsState()
    val isCreatingCharacters by viewModel.isCreatingCharacters.collectAsState()
    val error by viewModel.error.collectAsState()
    
    // Загружаем фотографии из глобального хранилища
    LaunchedEffect(Unit) {
        println("DEBUG: CharacterCreationScreen LaunchedEffect started")
        viewModel.loadPhotosFromGlobalStorage()
    }
    
    // Показываем ошибку если есть
    LaunchedEffect(error) {
        error?.let {
            // TODO: Показать Snackbar с ошибкой
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        
        // Заголовок
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Кнопка назад
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clickable { onNavigate(Screen.PhotoSelection.route) }
                    .background(
                        color = Color(0xFFF5F5F5),
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "←",
                    fontSize = 20.sp,
                    color = Color.Black
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Заголовок
            Text(
                text = "Создание персонажей",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Подзаголовок
        Text(
            text = "Генерация персонажей",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray
        )
        
        Text(
            text = "Превратим ваши фото в персонажей комикса",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 4.dp)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Основной контент
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            
            // Секция с фотографиями и персонажами
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    
                    // Левая колонка - Фотографии
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Фото",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        
                        // Сетка фотографий
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(selectedPhotos) { photo ->
                                PhotoItem(
                                    photo = photo,
                                    modifier = Modifier.size(80.dp)
                                )
                            }
                        }
                    }
                    
                    // Правая колонка - Персонажи
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Персонажи",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        
                        if (generatedCharacters.isEmpty()) {
                            // Placeholder для персонажей
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(80.dp)
                                    .background(
                                        color = Color(0xFFF5F5F5),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = Color(0xFFE0E0E0),
                                        shape = RoundedCornerShape(8.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Нажмите \"Создать\"",
                                    fontSize = 12.sp,
                                    color = Color.Gray,
                                    textAlign = TextAlign.Center
                                )
                            }
                        } else {
                            // Сетка персонажей
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(generatedCharacters) { character ->
                                    PhotoItem(
                                        photo = character,
                                        modifier = Modifier.size(80.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
            
            // Кнопка создания персонажей
            item {
                Button(
                    onClick = { viewModel.createCharacters() },
                    enabled = selectedPhotos.isNotEmpty() && !isCreatingCharacters,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6200EE)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "✨",
                            fontSize = 16.sp
                        )
                        Text(
                            text = if (isCreatingCharacters) "Создание..." else "Создать персонажей",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                    }
                }
            }
        }
        
        // Кнопка продолжить
        Button(
            onClick = { 
                // TODO: Переход к следующему экрану (выбор шаблона)
                onNavigate(Screen.SelectTemplate.route)
            },
            enabled = viewModel.canContinue(),
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF03DAC6)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Продолжить",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }
    }
}

/**
 * Компонент для отображения фотографии
 */
@Composable
private fun PhotoItem(
    photo: ru.sad.comicscreator.domain.model.SelectedPhoto,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF5F5F5))
    ) {
        photo.byteArray?.let { byteArray ->
            val imageBitmap = remember(byteArray) {
                try {
                    // TODO: Реализовать конвертацию ByteArray в ImageBitmap
                    null
                } catch (e: Exception) {
                    null
                }
            }
            
            imageBitmap?.let { bitmap ->
                Image(
                    bitmap = bitmap,
                    contentDescription = "Фотография",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } ?: run {
                // Fallback если не удалось загрузить изображение
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "📷",
                        fontSize = 24.sp
                    )
                }
            }
        } ?: run {
            // Fallback если нет данных изображения
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "📷",
                    fontSize = 24.sp
                )
            }
        }
    }
}
