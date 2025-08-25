# 📱 Моментальный Комикс

Kotlin Multiplatform приложение для создания комиксов из фотографий пользователей.

## 🎯 Концепция

**Моментальный Комикс** — это приложение, которое позволяет:
- Загружать фото себя и друзей
- Автоматически генерировать персонажей в стиле комикса
- Выбирать шаблоны с готовыми репликами и сценками
- Создавать смешные комиксы в несколько кликов
- Делиться результатами

## 🏗️ Архитектура

Проект следует **MVI (Model-View-Intent)** архитектуре с использованием:

### Основные компоненты:
- **ViewModel** — управление состоянием UI
- **Repository** — абстракция для доступа к данным
- **UseCase** — бизнес-логика приложения
- **DataSource** — источники данных (API, База данных)
- **Koin** — Dependency Injection
- **Realm** — локальная база данных
- **Compose Multiplatform** — UI фреймворк

### 📁 Структура проекта:

```
shared/src/commonMain/kotlin/ru/sad/comicscreator/
├── 📂 domain/
│   ├── 📂 model/              # Доменные модели
│   │   ├── Character.kt       # Персонаж комикса
│   │   ├── Comic.kt          # Готовый комикс
│   │   ├── ComicTemplate.kt  # Шаблон комикса
│   │   ├── Photo.kt          # Фотография
│   │   └── Result.kt         # Обертка для результатов
│   ├── 📂 repository/         # Интерфейсы репозиториев
│   │   ├── CharacterRepository.kt
│   │   ├── ComicRepository.kt
│   │   └── PhotoRepository.kt
│   └── 📂 usecase/           # Бизнес-логика
│       ├── CreateCharacterUseCase.kt
│       ├── CreateComicUseCase.kt
│       └── GetComicTemplatesUseCase.kt
├── 📂 presentation/
│   ├── 📂 viewmodel/         # ViewModels
│   │   ├── BaseViewModel.kt
│   │   └── MainViewModel.kt
│   ├── 📂 navigation/        # Навигация
│   │   └── Screen.kt
│   └── 📂 screens/          # UI экраны
│       └── HomeScreen.kt
└── 📂 di/                   # Dependency Injection
    ├── AppModule.kt
    ├── DataModule.kt
    ├── NetworkModule.kt
    └── KoinInitializer.kt
```

### 🔄 Поток данных:

```
UI (Compose) → ViewModel → UseCase → Repository → DataSource → API/Database
```

### 📊 Диаграмма архитектуры:

![Архитектура приложения](architecture-diagram)

Архитектура следует принципам Clean Architecture с четким разделением слоев:
- **UI Layer**: Compose UI компоненты
- **Presentation Layer**: ViewModels с MVI паттерном  
- **Domain Layer**: UseCase классы с бизнес-логикой
- **Repository Layer**: Абстракции для доступа к данным
- **Data Layer**: Конкретные реализации DataSource

## 📱 Экраны приложения

### 1. **Главный экран (Home)**
- Приветствие пользователя
- Быстрые действия (создать персонажа/комикс)
- Мои персонажи (горизонтальный список)
- Популярные шаблоны
- Последние комиксы

### 2. **Создание персонажа (Create Character)**
- Загрузка фото из галереи/камеры
- Ввод имени персонажа
- Предпросмотр обработки
- Сохранение персонажа

### 3. **Галерея персонажей (Character Gallery)**
- Сетка всех созданных персонажей
- Поиск по имени
- Редактирование/удаление
- Добавление новых

### 4. **Выбор шаблона (Select Template)**
- Категории шаблонов (юмор, романтика, дружба)
- Фильтр по количеству персонажей
- Популярные и новые шаблоны
- Предпросмотр шаблона

### 5. **Создание комикса (Create Comic)**
- Выбор персонажей для ролей
- Настройка позиций персонажей
- Редактирование реплик
- Предпросмотр результата
- Генерация финального изображения

### 6. **Просмотр комикса (View Comic)**
- Полноэкранный просмотр
- Возможность редактирования
- Кнопки поделиться
- Сохранение в галерею

### 7. **Галерея комиксов (Comic Gallery)**
- Сетка всех созданных комиксов
- Сортировка по дате
- Поиск по названию
- Статистика (поделиться, лайки)

### 8. **Редактирование комикса (Edit Comic)**
- Изменение текста реплик
- Перестановка персонажей
- Изменение шаблона
- Сохранение изменений

## 🎨 Дизайн-система

### Цветовая схема:
- **Основной цвет**: Material 3 Dynamic Color
- **Стиль**: Modern, яркий, комиксовый
- **Шрифты**: Material 3 Typography

### UI Компоненты:
- **Cards** для контейнеров контента
- **FAB** для основных действий
- **Bottom Navigation** для переключения разделов
- **LazyGrid/LazyRow** для списков
- **Chips** для категорий и тегов

### Принципы UX:
1. **Простота** — минимум кликов до результата
2. **Визуальность** — много изображений и превью
3. **Быстрота** — мгновенная обратная связь
4. **Интуитивность** — понятные иконки и лейблы

## 🛠️ Технологический стек

### Frontend:
- **Kotlin Multiplatform Mobile**
- **Compose Multiplatform**
- **Koin** (Dependency Injection)
- **Navigation Compose**
- **ViewModel & StateFlow**
- **Realm** (Local Database)
- **Ktor Client** (HTTP Client)

### Backend:
- **Ktor Server** (REST API)
- **PostgreSQL** (Database)
- **Exposed** (ORM)
- **AI Image Processing API** (для генерации персонажей)

### Дополнительно:
- **Kotlinx Serialization** (JSON)
- **Kotlinx Coroutines** (Async)
- **Material 3** (Design System)

## 🚀 Как запустить

### Требования:
- Android Studio Arctic Fox+
- JDK 11+
- Xcode 13+ (для iOS)

### Сборка:
```bash
# Android
./gradlew composeApp:assembleDebug

# iOS
./gradlew iosApp:build

# Сервер
./gradlew server:run
```

### Тестирование:
```bash
# Все тесты
./gradlew test

# Только shared модуль
./gradlew shared:test

# Android тесты
./gradlew composeApp:testDebugUnitTest
```

## 📋 TODO

- [ ] Реализация репозиториев и data sources
- [ ] Настройка Realm database
- [ ] Интеграция с AI API для обработки изображений
- [ ] Создание остальных экранов UI
- [ ] Добавление тестов
- [ ] Настройка CI/CD

## 🏁 Модульная структура

### `/composeApp`
Compose Multiplatform UI слой с экранами и навигацией.

### `/shared`  
Общая бизнес-логика, доменные модели и архитектурные компоненты.

### `/server`
Ktor сервер для API и обработки изображений.

### `/iosApp`
iOS-специфичная конфигурация и entry point.

---

**Статус проекта**: 🏗️ В разработке

**Последнее обновление**: Базовая архитектура и навигация готовы