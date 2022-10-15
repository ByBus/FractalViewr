package ui

import androidx.compose.runtime.Composable
import java.io.File
import javax.swing.JFileChooser
import javax.swing.UIManager
import javax.swing.filechooser.FileNameExtensionFilter

@Composable
fun FileSaveDialog(
    title: String,
    formats: List<String> = listOf("png", "jpg"),
    defaultFileName: String = "MyImage.png",
    onResult: (result: File?) -> Unit,
) {
    try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
    } catch (_: Exception) { }
    val fileChooser = JFileChooser(File(System.getProperty("user.dir")) ).apply {
        dialogTitle = title
        fileSelectionMode = JFileChooser.FILES_ONLY
        fileFilter = FileNameExtensionFilter(formats.joinToString(separator = ", *.", prefix = "*."), *formats.toTypedArray())
        isAcceptAllFileFilterUsed = false
        selectedFile = File(defaultFileName)
    }

    if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
        val file = fileChooser.selectedFile
        val name = file.name.substringBeforeLast(".")
        val oldExtension = file.name.substringBeforeLast(".")
        val extension = if (formats.any { it.equals(oldExtension, ignoreCase = true) }) oldExtension else formats[0]
        val fileWithExt = File(file.parent, "$name.$extension")
        onResult(fileWithExt)
    } else {
        onResult(null)
    }
}