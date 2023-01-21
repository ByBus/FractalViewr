package domain.imageprocessing

import java.awt.Image
import java.awt.image.BufferedImage

class BufferedImageWrapper(val bufferedImage: BufferedImage) {
    fun upscale(
        targetWidth: Int,
        targetHeight: Int,
    ): BufferedImageWrapper {
        val resultingImage: Image = bufferedImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_FAST)
        return BufferedImageWrapper(
            BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB).apply {
                graphics.drawImage(resultingImage, 0, 0, null)
            })
    }
}