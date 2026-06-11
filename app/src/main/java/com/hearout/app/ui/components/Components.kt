package com.hearout.app.ui.components

import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.material3.OutlinedCard
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
    OutlinedCard(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .height(height = 38.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                expanded = true
            }
            .height(height = 38.dp)
    ) {
        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                selected, fontSize = 12.sp,
                fontFamily = FontFamily.Serif,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 15.dp, end = 15.dp)
            )
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "Set $labelText",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(end = 5.dp)
                    .graphicsLayer { rotationZ = rotate }
            )


            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth(0.5F),
                shape = RoundedCornerShape(10)
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

    OutlinedCard(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .height(height = 38.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                expanded = true
            }
            .height(height = 38.dp)
    ) {
        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                selected, fontSize = 12.sp,
                fontFamily = FontFamily.Serif,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 15.dp, end = 15.dp)
            )
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "Set $labelText",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(end = 5.dp)
                    .graphicsLayer { rotationZ = rotate }
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
