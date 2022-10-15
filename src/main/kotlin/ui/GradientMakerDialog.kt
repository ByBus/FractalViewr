package ui

import Localization
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.gradientmaker.GradientMaker
import ui.gradientmaker.controller.ColorPickerController
import ui.gradientmaker.controller.GradientSliderController


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GradientMakerDialog(
    openDialog: MutableState<Boolean>,
    colorPickerController: ColorPickerController,
    gradientSliderController: GradientSliderController,
    onConfirm: (String, List<Pair<Float, Int>>) -> Unit = { _, _ -> },
) {
    if (openDialog.value) {
        gradientSliderController.reset()
        var gradientName by remember { mutableStateOf("NEW Gradient") }
        var showError by remember { mutableStateOf(false) }
        AlertDialog(
            onDismissRequest = {},
            title = {
                Text(text = Localization.gradientMakerTitle.uppercase(), style = MaterialTheme.typography.h6)
            },
            text = {
                Column(modifier = Modifier.width(350.dp)) {
                    TextField(
                        value = gradientName,
                        onValueChange = { gradientName = it; showError = it.isBlank() },
                        label = { if (showError.not()) Text(Localization.gradientHint) else Text(Localization.gradientHintError) },
                        trailingIcon = {
                            if (showError)
                                Icon(Icons.Filled.Info, Localization.gradientHintError, tint = MaterialTheme.colors.error)
                        },
                        singleLine = true,
                        isError = showError,
                        modifier = Modifier.fillMaxWidth()
                    )
                    GradientMaker(colorPickerController, gradientSliderController, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (showError.not()) {
                            onConfirm.invoke(gradientName, gradientSliderController.requestGradient())
                            openDialog.value = false
                        }
                    }) {
                    Text(Localization.ok)
                }
            },
            dismissButton = {
                Button(onClick = { openDialog.value = false }) {
                    Text(Localization.cancel)
                }
            }
        )
    }
}
