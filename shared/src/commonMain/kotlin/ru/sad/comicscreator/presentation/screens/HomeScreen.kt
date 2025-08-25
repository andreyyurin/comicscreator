package ru.sad.comicscreator.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(
    onNavigateToCreateCharacter: () -> Unit,
    onNavigateToSelectTemplate: () -> Unit,
    onNavigateToComicGallery: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF4A90E2), // Синий сверху
                        Color(0xFF8E44AD)  // Фиолетовый снизу
                    )
                )
            )
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(80.dp))
        
        // Заголовок приложения
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFFC107) // Желтый цвет как на дизайне
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Моментальный\nКомикс",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center,
                    color = Color.Black
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Подзаголовок
        Text(
            text = "Превратите фото в комикс!",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        // Сетка функций 2x2
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Загружайте фото
                FeatureCard(
                    modifier = Modifier.weight(1f),
                    emoji = "📷",
                    title = "Загружайте\nфото",
                    onClick = onNavigateToCreateCharacter
                )
                
                // Создавайте персонажей
                FeatureCard(
                    modifier = Modifier.weight(1f),
                    emoji = "✨",
                    title = "Создавайте\nперсонажей",
                    onClick = onNavigateToCreateCharacter
                )
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Выбирайте шаблоны
                FeatureCard(
                    modifier = Modifier.weight(1f),
                    emoji = "🎨",
                    title = "Выбирайте\nшаблоны",
                    onClick = onNavigateToSelectTemplate
                )
                
                // Делитесь с друзьями
                FeatureCard(
                    modifier = Modifier.weight(1f),
                    emoji = "📤",
                    title = "Делитесь с\nдрузьями",
                    onClick = onNavigateToComicGallery
                )
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Кнопка "Начать"
        Button(
            onClick = onNavigateToCreateCharacter,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF8E44AD).copy(alpha = 0.8f)
            ),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text(
                text = "Начать",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun FeatureCard(
    modifier: Modifier = Modifier,
    emoji: String,
    title: String,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .aspectRatio(1f)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.15f)
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Эмодзи в круге
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = emoji,
                    style = MaterialTheme.typography.headlineMedium
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
