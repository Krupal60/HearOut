package com.hearout.app.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.collections.immutable.ImmutableList

@Composable
fun SingleDropDownMenu(
    data: ImmutableList<Pair<String, String>>,
    selected: String,
    onOptionSelect: (String, String) -> Unit,
    modifier: Modifier = Modifier,
    labelText: String = ""
) {
    var expanded by remember { mutableStateOf(false) }
    val rotate by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "ArrowRotation"
    )
    Surface(
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier
            .height(40.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                expanded = true
            }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = selected,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontFamily = FontFamily.Serif
                ),
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "Set $labelText",
                modifier = Modifier.graphicsLayer { rotationZ = rotate }
            )


            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth(0.5F),
                shape = RoundedCornerShape(12.dp)
            ) {
                data.forEach { (name, code) ->
                    val isSelected = selected == name
                    DropdownMenuItem(text = {
                        Text(
                            text = name,
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Serif,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Unspecified
                        )
                    }, trailingIcon = {
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = "Selected",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }, onClick = {
                        onOptionSelect(name, code)
                        expanded = false
                    })
                }
            }
        }
    }

}

@Composable
fun SingleDropDownMenu2(
    data: ImmutableList<Triple<String, String, Boolean>>,
    selected: String,
    onOptionSelect: (String, String) -> Unit,
    modifier: Modifier = Modifier,
    labelText: String = ""
) {
    var expanded by remember { mutableStateOf(false) }
    val rotate by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "ArrowRotation2"
    )

    Surface(
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier
            .height(40.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                expanded = true
            }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = selected,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontFamily = FontFamily.Serif
                ),
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "Set $labelText",
                modifier = Modifier.graphicsLayer { rotationZ = rotate }
            )


            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth(0.5F),
                shape = RoundedCornerShape(10)
            ) {
                data.forEach { (name, code, boolean) ->
                    val isSelected = selected == name
                    DropdownMenuItem(text = {
                        Text(
                            text = name,
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Serif,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Unspecified
                        )
                    }, trailingIcon = {
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = "Selected",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        } else if (boolean) {
                            Icon(
                                imageVector = Icons.Rounded.Download,
                                contentDescription = "Download Icon"
                            )
                        }
                    }, onClick = {
                        onOptionSelect(name, code)
                        expanded = false
                    })
                }
            }
        }
    }

}
