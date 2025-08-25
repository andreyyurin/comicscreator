package ru.sad.comicscreator

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform