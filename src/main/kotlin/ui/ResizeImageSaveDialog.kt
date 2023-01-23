package ui

import Localization
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import domain.imageprocessing.BufferedImageWrapper
import domain.imageprocessing.FileSaveDialogController
import java.awt.Cursor

private const val INITIAL_IMAGE_SIZE = "1000"
private const val MAX_IMAGE_SIZE = 46000
private const val ERROR = 1
private const val TOO_LARGE_NUMBER = 2
private const val NO_ERROR = 0

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ResizeImageSaveDialog(
    title: String,
    currentImage: BufferedImageWrapper,
    controller: FileSaveDialogController,
    selfClose: () -> Unit,
) {
    LaunchedEffect(Unit) {
        controller.update(currentImage)
    }
    val image by controller.image.collectAsState()
    val savingPercent by controller.savedPercentage.collectAsState()
    var showError by remember { mutableStateOf(NO_ERROR) }
    var text by remember { mutableStateOf(INITIAL_IMAGE_SIZE) }
    var isComputed by remember { mutableStateOf(false) }
    var showFileSaveDialog by remember { mutableStateOf(false) }
    if (showFileSaveDialog) {
        FileSaveDialog(
            title = Localization.fileSaveDialogTitle,
            onResult = { file ->
                file?.let {
                    controller.saveImage(it)
                }
                showFileSaveDialog = false
            }
        )
    }
    AlertDialog(
        onDismissRequest = {},
        title = {
            Text(text = title.uppercase(), style = MaterialTheme.typography.h6)
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth().height(IntrinsicSize.Min)
                ) {
                    OutlinedTextField(
                        value = text,
                        onValueChange = {
                            text = it.replaceFirst("^0+".toRegex(), "").take(10)
                            showError = when {
                                !text.matches("\\d+".toRegex()) -> ERROR
                                text.toInt() > MAX_IMAGE_SIZE -> TOO_LARGE_NUMBER
                                else -> NO_ERROR
                            }
                        },
                        label = {
                            val string = when (showError) {
                                ERROR -> Localization.notNumber
                                TOO_LARGE_NUMBER -> Localization.largeNumber + MAX_IMAGE_SIZE
                                else -> Localization.imageSize
                            }
                            Text(string)
                        },
                        trailingIcon = {
                            if (showError != NO_ERROR) {
                                Icon(
                                    Icons.Filled.Info,
                                    if (showError == ERROR) Localization.notNumber else Localization.largeNumber,
                                    tint = MaterialTheme.colors.error
                                )
                            } else {
                                val enabled = isComputed || text != INITIAL_IMAGE_SIZE
                                Button(
                                    enabled = enabled,
                                    onClick = {
                                        isComputed = true
                                        controller.cancelJob()
                                        controller.createImageprocessor(text.toInt(), text.toInt())
                                        controller.compute()
                                        controller.subscribe()
                                    },
                                    modifier = Modifier.padding(end = 10.dp)
                                        .pointerHoverIcon(
                                            PointerIcon(
                                                Cursor(
                                                    if (enabled) Cursor.HAND_CURSOR else Cursor.DEFAULT_CURSOR
                                                )
                                            )
                                        )
                                ) {
                                    Text(Localization.coputeNewSize)
                                }
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        isError = showError != NO_ERROR,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Canvas(modifier = Modifier.width(300.dp).height(300.dp)) {
                    drawImage(
                        ((if (isComputed) image else currentImage))
                            .upscale(300, 300).bufferedImage.toComposeImageBitmap()
                    )
                }
            }
        },
        confirmButton = {
            if (savingPercent == 100f) {
                Button(
                    onClick = {
                        controller.cancelJob()
                        showFileSaveDialog = true
                    },
                modifier = Modifier.width(90.dp)) {
                    Text(Localization.save)
                }
            } else {
                LoadingButton(percent = savingPercent, modifier = Modifier.width(90.dp))
            }
        },
        dismissButton = {
            Button(onClick = {
                controller.cancelJob()
                selfClose()
            }
            ) {
                Text(Localization.cancel)
            }
        },
    )
}