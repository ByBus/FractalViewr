package domain.imageprocessing

interface ImageProcessor {
    suspend fun computeImage()
}