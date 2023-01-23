package domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import javax.imageio.ImageWriter
import javax.imageio.event.IIOWriteProgressListener


class ImageSaver: FileSaver<BufferedImage> {
    private  val extensionsToMimeType = mapOf("png" to "image/png", "jpg" to "image/jpeg", "jpeg" to "image/jpeg")
    private var progressConsumer: (Float) -> Unit  = {}

    override suspend fun save(content: BufferedImage, file: File) {
        val name = file.name.substringBeforeLast(".")
        val ext = file.name.substringAfterLast(".").lowercase()
        val format =  if(extensionsToMimeType.contains(ext)) ext else "png"
        val fileWithExt = File(file.parent, "$name.$format")
        val imageWriter = ImageIO.getImageWritersByMIMEType(extensionsToMimeType[format]).next()
        imageWriter.addIIOWriteProgressListener(listener)
        withContext(Dispatchers.IO) {
            //ImageIO.write(content, format, fileWithExt)
            ImageIO.createImageOutputStream(fileWithExt).use { ios ->
                imageWriter.output = ios
                imageWriter.write(content)
            }
        }
    }

    private val listener
        get() = object : IIOWriteProgressListener{
            override fun imageProgress(source: ImageWriter?, percentageDone: Float) {
                progressConsumer.invoke(percentageDone)
            }

            override fun imageComplete(source: ImageWriter?) = progressConsumer.invoke(100f)
            override fun imageStarted(source: ImageWriter?, imageIndex: Int) = Unit
            override fun thumbnailStarted(source: ImageWriter?, imageIndex: Int, thumbnailIndex: Int) = Unit
            override fun thumbnailProgress(source: ImageWriter?, percentageDone: Float) = Unit
            override fun thumbnailComplete(source: ImageWriter?) = Unit
            override fun writeAborted(source: ImageWriter?) = Unit
        }

    override fun progressProvider(progressConsumer: (Float) -> Unit) {
        this.progressConsumer = progressConsumer
    }
}