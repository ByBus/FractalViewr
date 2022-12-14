package ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource

@Composable
fun AppIcon() = painterResource("images/mandelbrot_icon.png")
@Composable
fun SaveIcon() = painterResource("images/save_FILL_wght400_GRAD0_opsz48.svg")

@Composable
fun SaveIconOutlined() = painterResource("images/save_OUTLINE_FILL0_wght400_GRAD0_opsz48.svg")

@Composable
fun AddGradientIcon() = painterResource("images/paletteAdd_FILL0_wght400_GRAD0_opsz48.svg")

@Composable
fun UndoIcon() = painterResource("images/undo_FILL0_wght400_GRAD0_opsz48.svg")

@Composable
fun ResetIcon() = painterResource("images/device_reset_FILL0_wght400_GRAD0_opsz48.svg")

@Composable
fun ExpandMore() = painterResource("images/expand_more_FILL0_wght400_GRAD0_opsz48.svg")

@Composable
fun ExpandLess() = painterResource("images/expand_less_FILL0_wght400_GRAD0_opsz48.svg")
