package ru.sad.comicscreator

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import ru.sad.comicscreator.di.initKoin

@Composable
@Preview
fun App() {
    KoinApplication(application = { initKoin() }) {
        MaterialTheme {
            ComicsCreatorApp()
        }
    }
}

@Composable
fun ComicsCreatorApp() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        WelcomeScreen()
    }
}

@Composable
fun WelcomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "📱 Моментальный Комикс",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Создавайте комиксы из своих фотографий!",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(32.dp))
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "🎯 Статус проекта",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "✅ Базовая архитектура готова\n" +
                            "✅ MVI с Koin DI\n" +
                            "✅ Доменные модели созданы\n" +
                            "✅ UseCase и Repository интерфейсы\n" +
                            "✅ Навигация настроена\n" +
                            "📝 Готово для дальнейшей разработки",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}