package ui

import Localization
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ui.gradientmaker.GradientMaker
import ui.gradientmaker.controller.ColorPickerController
import ui.gradientmaker.controller.GradientSliderController


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GradientMakerDialog(
    title: String,
    colorPickerController: ColorPickerController,
    gradientSliderController: GradientSliderController,
    defaultGradientName: String,
    dialogWidth: Dp = 420.dp,
    startGradient: List<Pair<Float, Int>>,
    resetOnOpen: Boolean = false,
    selfClosing: () -> Unit,
    onConfirm: (String, List<Pair<Float, Int>>) -> Unit = { _, _ -> },
) {
    LaunchedEffect(Unit) {
        if (resetOnOpen) gradientSliderController.reset()
        gradientSliderController.setGradient(startGradient)
    }
    var gradientNameRemember by remember { mutableStateOf(defaultGradientName) }
    var showError by remember { mutableStateOf(false) }
    AlertDialog(
        modifier = Modifier.width(dialogWidth),
        onDismissRequest = {},
        title = {
            Text(text = title.uppercase(), style = MaterialTheme.typography.h6)
        },
        text = {
            Column {
                TextField(
                    value = gradientNameRemember,
                    onValueChange = { gradientNameRemember = it; showError = it.isBlank() },
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
                        onConfirm.invoke(gradientNameRemember, gradientSliderController.requestGradient())
                        selfClosing()
                    }
                }) {
                Text(Localization.ok)
            }
        },
        dismissButton = {
            Button(onClick = { selfClosing() }) {
                Text(Localization.cancel)
            }
        }
    )
}

enum class GradientDialog {
    CLOSED, CREATE, EDIT
}

data class GradientDialogConfig(
    val id: Int = -1000,
    val name: String = "",
    val gradient: List<Pair<Float, Int>> = emptyList(),
    val state: GradientDialog = GradientDialog.CLOSED,
) {
    fun isOpened() = state != GradientDialog.CLOSED

    fun isEditMode() = state == GradientDialog.EDIT

    fun isCreationMode() = state == GradientDialog.CREATE
    fun close(): GradientDialogConfig = copy(gradient = emptyList(), state = GradientDialog.CLOSED)

    fun create(): GradientDialogConfig = copy(state = GradientDialog.CREATE)

    fun edit(): GradientDialogConfig = copy(state = GradientDialog.EDIT)

    fun withName(newName: String) = copy(name = newName)
}