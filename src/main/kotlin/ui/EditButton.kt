package ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EditButton(
    visible: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier
            .offset(x = 8.dp)
    )
    {
        FloatingActionButton(
            onClick = onClick,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.size(18.dp)
        ) {
            Icon(
                Icons.Rounded.Edit, "", tint = MaterialTheme.colors.onSecondary,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}