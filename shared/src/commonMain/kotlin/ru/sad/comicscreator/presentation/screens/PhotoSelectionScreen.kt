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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sad.comicscreator.domain.model.SelectedPhoto

@Composable
fun PhotoSelectionScreen(
    onBackPressed: () -> Unit,
    onGalleryClick: () -> Unit,
    onCameraClick: () -> Unit,
    onContinueClick: () -> Unit
) {
    // Временное состояние для демонстрации UI
    var selectedPhotos by remember { mutableStateOf(listOf<SelectedPhoto>()) }
    
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
            // Отображаем выбранные фотографии
            items(selectedPhotos) { photo ->
                PhotoItem(
                    photo = photo,
                    onRemove = {
                        selectedPhotos = selectedPhotos.filter { it.id != photo.id }
                    }
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
                onClick = onGalleryClick,
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
                onClick = onCameraClick,
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
            enabled = selectedPhotos.isNotEmpty() // Активна только при наличии фото
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
fun PhotoItem(
    photo: SelectedPhoto,
    onRemove: () -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF6200EE).copy(alpha = 0.1f)) // Временный цвет
    ) {
        // TODO: Загружать и отображать реальные изображения
        // Пока просто цветной прямоугольник с иконкой
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "📷",
                fontSize = 48.sp
            )
        }
        
        // Кнопка удаления
        Card(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .size(24.dp)
                .clickable { onRemove() },
            colors = CardDefaults.cardColors(
                containerColor = Color.Black.copy(alpha = 0.7f)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "✕",
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
        }
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
