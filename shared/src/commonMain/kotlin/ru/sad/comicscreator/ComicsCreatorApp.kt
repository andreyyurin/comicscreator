package ru.sad.comicscreator

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import org.koin.compose.KoinApplication
import ru.sad.comicscreator.di.initKoin
import ru.sad.comicscreator.presentation.navigation.Screen
import ru.sad.comicscreator.presentation.screens.HomeScreen
import ru.sad.comicscreator.presentation.screens.PhotoSelectionScreen
import ru.sad.comicscreator.presentation.screens.CharacterCreationScreen

@Composable
fun ComicsCreatorApp() {
    KoinApplication(application = { initKoin() }) {
        MaterialTheme {
            ComicsCreatorNavigation()
        }
    }
}

@Composable
fun ComicsCreatorNavigation() {
    val navController = rememberNavController()
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    onNavigateToCreateCharacter = { 
                        navController.navigate(Screen.PhotoSelection.route) 
                    },
                    onNavigateToSelectTemplate = { 
                        navController.navigate(Screen.SelectTemplate.route) 
                    },
                    onNavigateToComicGallery = { 
                        navController.navigate(Screen.ComicGallery.route) 
                    }
                )
            }
            
            composable(Screen.PhotoSelection.route) {
                PhotoSelectionScreen(
                    onBackPressed = {
                        navController.navigateUp()
                    },
                    onContinueClick = { route ->
                        navController.navigate(route)
                    }
                )
            }
            
            composable(Screen.CreateCharacter.route) {
                CharacterCreationScreen(
                    onNavigate = { route ->
                        navController.navigate(route)
                    }
                )
            }
            
            composable(Screen.SelectTemplate.route) {
                PlaceholderScreen(
                    title = "Выбирайте шаблоны",
                    description = "Выберите шаблон комикса с готовыми репликами",
                    navController = navController
                )
            }
            
            composable(Screen.ComicGallery.route) {
                PlaceholderScreen(
                    title = "Делитесь с друзьями",
                    description = "Поделитесь своими комиксами в социальных сетях",
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun PlaceholderScreen(
    title: String,
    description: String,
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = { navController.navigateUp() }
        ) {
            Text("Назад")
        }
    }
}
