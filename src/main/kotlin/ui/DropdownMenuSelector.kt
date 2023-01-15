package ui

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import java.awt.Cursor

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DropdownMenuSelector(items: List<String>, initialSelection: Int = 0, label: String, modifier: Modifier = Modifier, onSelect: (Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedPosition by remember { mutableStateOf(items[initialSelection]) }
    var dropDownWidth by remember { mutableStateOf(0) }
    val iconLess = ExpandLess()
    val iconMore = ExpandMore()
    var iconImage by remember { mutableStateOf(iconMore) }

    iconImage = if (expanded) iconLess else iconMore

    Column(modifier = modifier.padding(8.dp)) {
        OutlinedTextField(
            value = if (items.contains(selectedPosition)) selectedPosition.uppercase() else items[0].uppercase(),
            onValueChange = { },
            modifier = Modifier.fillMaxWidth()
                .pointerHoverIcon(PointerIcon(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)), true)
                .onSizeChanged {
                    dropDownWidth = it.width
                }.noRippleClickable {
                    expanded = !expanded
                },
            label = { Text(label) },
            trailingIcon = {
                AnimatedContent(targetState = iconImage,
                    transitionSpec = {
                        (scaleIn() + fadeIn() with
                                scaleOut() + fadeOut())
                            .using(SizeTransform(clip = true))
                    }) { icon ->
                    Icon(icon, "show or hide fractal list", Modifier.clickable { expanded = !expanded; })
                }
            },
            shape = MaterialTheme.shapes.medium,
            readOnly = true,
            enabled = false,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                disabledTextColor = Color.Black
            )
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { dropDownWidth.toDp() })
        ) {
            items.forEachIndexed{ index, itemText ->
                if (index == 0 && items.contains(selectedPosition).not()) selectedPosition = itemText
                DropdownMenuItem(
                    onClick = {
                        selectedPosition = itemText
                        expanded = false
                        onSelect.invoke(index)
                    },
                    modifier = if (selectedPosition == itemText) Modifier.background(MaterialTheme.colors.secondary) else Modifier
                ) {
                    Text(
                        text = itemText,
                        style = MaterialTheme.typography.h6,
                        color = if (selectedPosition == itemText) MaterialTheme.colors.onPrimary else Color.Unspecified
                    )
                }
            }
        }
    }
}
