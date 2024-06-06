package com.hearout.app.view.components

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp


//@Composable
//fun AdmobBanner(modifier: Modifier) {
//    AndroidView(modifier = modifier, factory = { context ->
//        AdView(context).apply {
//            setAdSize(AdSize.BANNER)
//            adUnitId = "ca-app-pub-4376590123999291/6943762773"
//            loadAd(AdRequest.Builder().build())
//        }
//
//    })
//}

val Int.sdp: Dp
    @Composable
    get() = this.sdpGet()

val Int.ssp: TextUnit
    @Composable get() = this.textSdp(density = LocalDensity.current)

@Composable
private fun Int.textSdp(density: Density): TextUnit = with(density) {
    this@textSdp.sdp.toSp()
}

@Composable
private fun Int.sdpGet(): Dp {

    val id = when (this) {
        in 1..600 -> "_${this}sdp"
        in (-60..-1) -> "_minus${this}sdp"
        else -> return this.dp
    }

    val resourceField = getFieldId(id)
    return if (resourceField != 0) dimensionResource(id = resourceField) else this.dp

}

@SuppressLint("DiscouragedApi")
@Composable
private fun getFieldId(id: String): Int {
    val context = LocalContext.current
    return context.resources.getIdentifier(id, "dimen", context.packageName)

}

@Composable
fun SingleDropDownMenu(
    modifier: Modifier,
    labelText: String = "",
    data: List<Pair<String, String>>,
    selected: String,
    onOptionSelected: (String, String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    OutlinedCard(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .height(height = 38.sdp)
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                expanded = true
            }
            .height(height = 38.sdp)
    ) {
        Row(
            modifier = modifier.align(Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                selected, fontSize = 12.ssp,
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
            )


            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth(0.5F)
            ) {
                data.forEach { (name, code) ->
                    DropdownMenuItem(text = {
                        Text(
                            text = name, fontSize = 12.ssp,
                            fontFamily = FontFamily.Serif
                        )
                    }, onClick = {
                        onOptionSelected(name, code)
                        expanded = false
                    })
                }
            }
        }
    }

}


@Composable
fun SingleDropDownMenu2(
    modifier: Modifier,
    labelText: String = "",
    data: List<Triple<String, String, Boolean>>,
    selected: String,
    onOptionSelected: (String, String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    OutlinedCard(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .height(height = 38.sdp)
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                expanded = true
           }
            .height(height = 38.sdp)
    ) {
        Row(
            modifier = modifier.align(Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                selected, fontSize = 12.ssp,
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
            )


            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth(0.5F)
            ) {
                data.forEach { (name, code, boolean) ->
                    DropdownMenuItem(text = {
                        Text(
                            text = name, fontSize = 12.ssp,
                            fontFamily = FontFamily.Serif
                        )
                    }, trailingIcon = {
                        if (boolean) {
                            Icon(
                                imageVector = Icons.Default.Download,
                                contentDescription = "Download Icon"
                            )
                        }
                    }, onClick = {
                        onOptionSelected(name, code)
                        expanded = false
                    })
                }
            }
        }
    }

}
