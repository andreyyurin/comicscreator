package ru.sad.comicscreator.data

import ru.sad.comicscreator.domain.model.SelectedPhoto

/**
 * Глобальное хранилище для передачи фотографий между экранами
 */
class GlobalPhotoStorage {
    
    private var selectedPhotos: List<SelectedPhoto> = emptyList()
    
    fun setSelectedPhotos(photos: List<SelectedPhoto>) {
        println("DEBUG: GlobalPhotoStorage.setSelectedPhotos called with ${photos.size} photos")
        selectedPhotos = photos
        println("DEBUG: GlobalPhotoStorage now contains ${selectedPhotos.size} photos")
    }
    
    fun getSelectedPhotos(): List<SelectedPhoto> {
        println("DEBUG: GlobalPhotoStorage.getSelectedPhotos called, returning ${selectedPhotos.size} photos")
        return selectedPhotos
    }
    
    fun clear() {
        println("DEBUG: GlobalPhotoStorage.clear called")
        selectedPhotos = emptyList()
    }
}
