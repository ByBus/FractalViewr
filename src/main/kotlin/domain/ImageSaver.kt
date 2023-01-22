package domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class ImageSaver: FileSaver<BufferedImage> {
    private  val extensions = listOf("png", "jpg")

    override suspend fun save(content: BufferedImage, file: File) {
        val name = file.name.substringBeforeLast(".")
        val ext = file.name.substringAfterLast(".").lowercase()
        val format =  if(extensions.contains(ext)) ext else extensions[0]
        val fileWithExt = File(file.parent, "$name.$format")
        withContext(Dispatchers.IO) {
            ImageIO.write(content, format, fileWithExt)
        }
    }
}