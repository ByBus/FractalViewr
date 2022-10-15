package ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.AwtWindow
import java.awt.FileDialog
import java.awt.Frame
import java.io.File

@Composable
fun FileDialog(
    parent: Frame? = null,
    title: String = "Choose a file",
    onCloseRequest: (result: File?) -> Unit,
) = AwtWindow(
    create = {
        object : FileDialog(parent, title, SAVE) {
            override fun setVisible(value: Boolean) {
                super.setVisible(value)
                if (value) {
                    val dir = directory
                    val fileName: String? = file
                    val file = fileName?.let { File(dir, it) }
                    onCloseRequest(file)
                }
            }
        }.apply {
            file = "*.png"
        }
    },
    dispose = FileDialog::dispose
)
