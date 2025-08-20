package com.hearout.app.view.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CloudDownload
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hearout.app.R
import com.hearout.app.domain.OnAction
import com.hearout.app.domain.TtsType
import com.hearout.app.view.components.SingleDropDownMenu
import com.hearout.app.view.components.SingleDropDownMenu2
import com.hearout.app.view.components.ssp
import com.hearout.app.view.utils.Utils
import com.hearout.app.viewmodel.TTSViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.Locale


data class MainScreenState(
    val text: String = "",
    val text2: String = "",
    val name: String = "",
    val name2: String = "",
    val isSpeaking: Boolean = false,
    val isSpeaking2: Boolean = false,
    val selectedLanguage: String = "English",
    val selectedLanguage2: String = "Hindi",
    val languageCode: String = "en",
    val languageCode2: String = "hi",
    val countryCode: String = "IN",
    val countryCode2: String = "IN",
    val voiceNameData: List<Triple<String, String, Boolean>> = listOf(Triple("Voice 1", "", true)),
    val voiceNameData2: List<Triple<String, String, Boolean>> = listOf(Triple("Voice 1", "", true)),
    val selectedVoice: String = "Voice 1",
    val selectedVoice2: String = "Voice 1",
    val voiceName: String = "",
    val voiceName2: String = "",
    val mp3File: File? = null,
    val convertLoading: Boolean = false,
    val openDialog: Boolean = false,
    val openDialog2: Boolean = false
)

@Composable
fun MainScreenImpl(viewModel: TTSViewModel = viewModel()) {
    val mainState =
        viewModel.mainState.collectAsStateWithLifecycle()
    MainScreen(mainState, viewModel::onActionTTS)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(mainState: State<MainScreenState>, onActionTTS: (OnAction) -> Unit) {

    val context = LocalContext.current
    val focus = LocalFocusManager.current


    Scaffold(
        topBar = {
            // Customize your top app bar here
            TopAppBar(
                title = {
                    Text(
                        text = "Welcome To ${stringResource(id = R.string.app_name)}",
                        textDecoration = TextDecoration.None,
                        style = TextStyle(
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.ssp,
                            lineHeight = 22.ssp
                        )
                    )
                }
            )
        },
        modifier = Modifier.safeDrawingPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(it)
                .padding(start = 14.dp, end = 14.dp, bottom = 10.dp)
                .background(MaterialTheme.colorScheme.surface)
        ) {

            val languages = listOf(
                "English" to "en",
                "English(US)" to "en",
                "Hindi" to "hi",
                "Gujarati" to "gu",
                "Bengali" to "bn",
                "Telugu" to "te",
                "Marathi" to "mr",
                "Tamil" to "ta",
                "Urdu" to "ur",
                "Kannada" to "kn",
                "Punjabi" to "pa",
                "French" to "fr",
                "Spanish" to "es"
            )

            LaunchedEffect(Unit) {
                CoroutineScope(Dispatchers.Main).launch {
                    onActionTTS(OnAction.OnGetVoices("en", Locale.getDefault().country))
                    onActionTTS(OnAction.OnGetVoices2("hi", Locale.getDefault().country))
                }
            }


            val loading2 = rememberSaveable {
                mutableStateOf(false)
            }
            val loading = rememberSaveable {
                mutableStateOf(false)
            }
            LaunchedEffect(mainState.value.isSpeaking) {
                if (mainState.value.isSpeaking) loading.value = false
            }
            LaunchedEffect(mainState.value.isSpeaking2) {
                if (mainState.value.isSpeaking2) loading2.value = false
            }


            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp)
                    .wrapContentHeight()
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 15.dp)
                ) {
                    Row {
                        SingleDropDownMenu(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 5.dp),
                            data = languages,
                            selected = mainState.value.selectedLanguage,
                            onOptionSelected = { language, languageCode ->
                                val country = when (language) {
                                    "English(US)" -> {
                                        "US"
                                    }

                                    "French" -> {
                                        "FR"
                                    }

                                    "Spanish" -> {
                                        "ES"
                                    }

                                    else -> {
                                        "IN"
                                    }
                                }
                                onActionTTS(OnAction.SelectedLanguage(language))
                                onActionTTS(OnAction.SelectedCode(languageCode, country))
                                CoroutineScope(Dispatchers.Main).launch {
                                    onActionTTS(OnAction.SetLanguage(languageCode, country))
                                    onActionTTS(OnAction.OnGetVoices(languageCode, country))
                                }
                            })


                        SingleDropDownMenu2(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 5.dp),
                            data = mainState.value.voiceNameData,
                            selected = mainState.value.selectedVoice,
                            onOptionSelected = { voice, voiceCode ->
                                CoroutineScope(Dispatchers.Main).launch {
                                    onActionTTS(OnAction.SelectedVoice(voice))
                                    onActionTTS(OnAction.VoiceName(voiceCode))
                                }
                            })

                    }

                    TextField(
                        value = mainState.value.text,
                        onValueChange = { string ->

                            onActionTTS(OnAction.ChangeText(string))
                        },
                        shape = RoundedCornerShape(12.dp),
                        placeholder = {
                            Text(
                                text = "Enter text in ${mainState.value.selectedLanguage} language",
                                color = Color.Gray,
                                fontFamily = FontFamily.Serif
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(135.dp)
                            .padding(top = 12.dp),
                        colors = TextFieldDefaults.colors(
                            errorIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        )
                    )

                    Row(modifier = Modifier.padding(top = 12.dp)) {
                        Button(
                            onClick = {
                                if (mainState.value.text.isEmpty() || mainState.value.text.isBlank()) {
                                    Toast.makeText(context, "Enter text", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                if (!mainState.value.isSpeaking && !loading.value) {
                                    loading.value = true
                                    onActionTTS(
                                        OnAction.SpeakText(
                                            mainState.value.text,
                                            mainState.value.languageCode,
                                            mainState.value.countryCode,
                                            mainState.value.voiceName
                                        )
                                    )
                                    onActionTTS(OnAction.IsSpeaking(TtsType.TTS1, true))
                                } else {
                                    onActionTTS(OnAction.Stop)
                                    loading.value = false
                                    onActionTTS(OnAction.IsSpeaking(TtsType.TTS1, false))
                                }
                            }, modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .padding(end = 5.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Text(
                                    if (mainState.value.isSpeaking) "Stop" else "Speak",
                                    fontSize = 12.ssp,
                                    fontFamily = FontFamily.Serif
                                )
                                Spacer(modifier = Modifier.padding(horizontal = 5.dp))
                                Icon(
                                    imageVector = if (mainState.value.isSpeaking) Icons.Rounded.Stop else Icons.Rounded.PlayArrow,
                                    contentDescription = if (mainState.value.isSpeaking) "Stop" else "Speak"
                                )

                                AnimatedVisibility(
                                    visible = loading.value,
                                    modifier = Modifier.wrapContentHeight()
                                ) {
                                    CircularProgressIndicator(
                                        color = MaterialTheme.colorScheme.inverseOnSurface,
                                        modifier = Modifier
                                            .size(32.dp)
                                            .wrapContentHeight()
                                            .padding(start = 8.dp)
                                    )
                                }
                            }
                        }


                        Button(
                            onClick = {
                                if (mainState.value.text.isEmpty() || mainState.value.text.isBlank()) {
                                    Toast.makeText(context, "Enter text", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }

                                onActionTTS(OnAction.OpenDialog)

                            }, modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .imePadding()
                                .padding(start = 5.dp)

                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Text(
                                    "Save .mp3",
                                    fontSize = 12.ssp,
                                    fontFamily = FontFamily.Serif
                                )
                                Spacer(modifier = Modifier.padding(horizontal = 5.dp))
                                Icon(imageVector = Icons.Rounded.Save, contentDescription = "Save")
                            }
                        }
                    }
                }

            }

            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .align(Alignment.CenterHorizontally)
                    .height(55.dp)
                    .padding(top = 15.dp), onClick = {

                    if (!Utils.isNetworkAvailable(context = context)) {
                        Toast.makeText(context, "Need Internet Connection", Toast.LENGTH_SHORT)
                            .show()
                        return@OutlinedButton
                    }
                    if (mainState.value.text.isEmpty() || mainState.value.text.isBlank()) {
                        Toast.makeText(context, "Enter Text", Toast.LENGTH_SHORT).show()
                        return@OutlinedButton
                    }
                    onActionTTS(
                        OnAction.Convert(
                            mainState.value.text,
                            mainState.value.languageCode,
                            mainState.value.languageCode2
                        )
                    )
                }
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Convert To ${mainState.value.selectedLanguage2}",
                        fontSize = 14.ssp,
                        fontFamily = FontFamily.Serif
                    )
                    Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                    Icon(
                        imageVector = Icons.Rounded.CloudDownload,
                        contentDescription = "Download icon"
                    )
                    AnimatedVisibility(visible = mainState.value.convertLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(40.dp)
                                .padding(start = 15.dp, end = 5.dp)
                        )
                    }
                }


            }



            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp)
                    .wrapContentHeight()
                    .imePadding()
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 15.dp)
                ) {
                    Row {
                        SingleDropDownMenu(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 5.dp),
                            data = languages,
                            selected = mainState.value.selectedLanguage2,
                            onOptionSelected = { language, languageCode ->
                                val country = when (language) {
                                    "English(US)" -> {
                                        "US"
                                    }

                                    "French" -> {
                                        "FR"
                                    }

                                    "Spanish" -> {
                                        "ES"
                                    }

                                    else -> {
                                        "IN"
                                    }
                                }
                                onActionTTS(OnAction.SelectedLanguage2(language))
                                onActionTTS(OnAction.SelectedCode2(languageCode, country))
                                CoroutineScope(Dispatchers.Main).launch {
                                    onActionTTS(OnAction.SetLanguage2(languageCode, country))
                                    onActionTTS(OnAction.OnGetVoices2(languageCode, country))
                                }
                            })


                        SingleDropDownMenu2(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 5.dp),
                            data = mainState.value.voiceNameData2,
                            selected = mainState.value.selectedVoice2,
                            onOptionSelected = { voice, voiceCode ->
                                CoroutineScope(Dispatchers.Main).launch {
                                    onActionTTS(OnAction.SelectedVoice2(voice))
                                    onActionTTS(OnAction.VoiceName2(voiceCode))
                                }
                            })

                    }

                    TextField(
                        value = mainState.value.text2,
                        onValueChange = { string ->

                            onActionTTS(OnAction.ChangeText2(string))
                        },
                        placeholder = {
                            Text(
                                text = "Enter text in ${mainState.value.selectedLanguage2} language",
                                color = Color.Gray,
                                fontFamily = FontFamily.Serif
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(135.dp)
                            .padding(top = 12.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.colors(
                            errorIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        )
                    )

                    Row(
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .imePadding()
                    ) {
                        Button(
                            onClick = {
                                if (mainState.value.text2.isEmpty() || mainState.value.text2.isBlank()) {
                                    Toast.makeText(context, "Enter text", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                if (!Utils.isNetworkAvailable(context = context)) {
                                    Toast.makeText(
                                        context,
                                        "Need Internet Connection",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    return@Button
                                }
                                if (!mainState.value.isSpeaking2 && !loading2.value) {
                                    loading2.value = true
                                    onActionTTS(
                                        OnAction.SpeakText2(
                                            mainState.value.text2,
                                            mainState.value.languageCode2,
                                            mainState.value.countryCode2,
                                            mainState.value.voiceName2
                                        )
                                    )
                                    onActionTTS(OnAction.IsSpeaking2(TtsType.TTS2, true))

                                } else {
                                    onActionTTS(OnAction.Stop2)
                                    loading2.value = false
                                    onActionTTS(OnAction.IsSpeaking2(TtsType.TTS2, false))
                                }
                            }, modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .padding(end = 5.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Text(
                                    if (mainState.value.isSpeaking2) "Stop" else "Speak",
                                    fontSize = 12.ssp,
                                    fontFamily = FontFamily.Serif
                                )
                                Spacer(modifier = Modifier.padding(horizontal = 5.dp))
                                Icon(
                                    imageVector = if (mainState.value.isSpeaking2) Icons.Rounded.Stop else Icons.Rounded.PlayArrow,
                                    contentDescription = if (mainState.value.isSpeaking) "Stop" else "Speak"
                                )

                                AnimatedVisibility(
                                    loading2.value,
                                    modifier = Modifier.wrapContentHeight()
                                ) {
                                    CircularProgressIndicator(
                                        color = MaterialTheme.colorScheme.inverseOnSurface,
                                        modifier = Modifier
                                            .size(32.dp)
                                            .wrapContentHeight()
                                            .padding(start = 8.dp)
                                    )

                                }
                            }
                        }


                        Button(
                            onClick = {
                                if (mainState.value.text2.isEmpty() || mainState.value.text2.isBlank()) {
                                    Toast.makeText(context, "Enter text", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }

                                onActionTTS(OnAction.OpenDialog2)

                            }, modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .padding(start = 5.dp)

                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Text(
                                    "Save .mp3",
                                    fontSize = 12.ssp,
                                    fontFamily = FontFamily.Serif
                                )
                                Spacer(modifier = Modifier.padding(horizontal = 5.dp))
                                Icon(imageVector = Icons.Rounded.Save, contentDescription = "Save")
                            }
                        }
                    }
                }

            }


            if (mainState.value.openDialog) {
                Dialog(onDismissRequest = { onActionTTS(OnAction.CloseDialog) }) {
                    Card {
                        Column(modifier = Modifier.padding(horizontal = 15.dp, vertical = 20.dp)) {
                            Text(
                                text = "Enter File Name :",
                                textDecoration = TextDecoration.None,
                                style = TextStyle(
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.ssp
                                )
                            )




                            OutlinedTextField(
                                value = mainState.value.name,
                                onValueChange = { string ->
                                    onActionTTS(OnAction.ChangeName(string))
                                },
                                placeholder = {
                                    Text(
                                        text = "Enter name of .mp3 file",
                                        color = Color.Gray,
                                        fontFamily = FontFamily.Serif,
                                        fontSize = 15.ssp,
                                    )
                                },
                                shape = RoundedCornerShape(12.dp),
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                keyboardActions = KeyboardActions(onDone = {
                                    focus.clearFocus()
                                    if (mainState.value.text.isEmpty() || mainState.value.text.isBlank()) {
                                        Toast.makeText(
                                            context,
                                            "Enter text",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                        return@KeyboardActions
                                    }
                                    if (mainState.value.name.isNotEmpty() || mainState.value.name.isNotBlank()) {
                                        onActionTTS(
                                            OnAction.SaveAsMp3(
                                                mainState.value.text,
                                                mainState.value.name,
                                                mainState.value.selectedLanguage
                                            )
                                        )
                                        return@KeyboardActions
                                    }
                                }),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 15.dp)
                            )

                            Row(
                                modifier = Modifier
                                    .padding(top = 15.dp)
                                    .imePadding(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        onActionTTS(OnAction.CloseDialog)
                                    }, modifier = Modifier
                                        .weight(1f)

                                ) {
                                    Text(
                                        "Cancel",
                                        fontSize = 13.ssp,
                                        fontFamily = FontFamily.Serif
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Button(
                                    onClick = {

                                        if (mainState.value.text.isEmpty() || mainState.value.text.isBlank()) {
                                            Toast.makeText(
                                                context,
                                                "Enter text",
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                            return@Button
                                        }

                                        if (mainState.value.name.isNotEmpty() || mainState.value.name.isNotBlank()) {
                                            onActionTTS(
                                                OnAction.SaveAsMp3(
                                                    mainState.value.text,
                                                    mainState.value.name,
                                                    mainState.value.selectedLanguage
                                                )
                                            )
                                            onActionTTS(OnAction.CloseDialog)
                                            return@Button
                                        }
                                        if (mainState.value.name.isEmpty() || mainState.value.name.isBlank()) {
                                            Toast.makeText(
                                                context,
                                                "Enter file name",
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                            return@Button
                                        }

                                    }, modifier = Modifier
                                        .weight(1f)

                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        Text(
                                            "Save .mp3",
                                            fontSize = 12.ssp,
                                            fontFamily = FontFamily.Serif
                                        )
                                        Spacer(modifier = Modifier.padding(horizontal = 5.dp))
                                        Icon(
                                            imageVector = Icons.Rounded.Save,
                                            contentDescription = "Save"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (mainState.value.openDialog2) {
                Dialog(onDismissRequest = { onActionTTS(OnAction.CloseDialog2) }) {
                    Card {
                        Column(modifier = Modifier.padding(horizontal = 15.dp, vertical = 20.dp)) {
                            Text(
                                text = "Enter File Name :",
                                textDecoration = TextDecoration.None,
                                style = TextStyle(
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.ssp
                                )
                            )



                            OutlinedTextField(
                                value = mainState.value.name2,
                                onValueChange = { string ->
                                    onActionTTS(OnAction.ChangeName2(string))
                                },
                                shape = RoundedCornerShape(12.dp),
                                placeholder = {
                                    Text(
                                        text = "Enter name of .mp3 file",
                                        color = Color.Gray,
                                        fontFamily = FontFamily.Serif,
                                        fontSize = 15.ssp,
                                    )
                                },
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                keyboardActions = KeyboardActions(onDone = {
                                    focus.clearFocus()
                                    if (mainState.value.text2.isEmpty() || mainState.value.text2.isBlank()) {
                                        Toast.makeText(context, "Enter text", Toast.LENGTH_SHORT)
                                            .show()
                                        return@KeyboardActions
                                    }
                                    if (mainState.value.name2.isNotEmpty() || mainState.value.name2.isNotBlank()) {
                                        onActionTTS(
                                            OnAction.SaveAsMp3(
                                                mainState.value.text2,
                                                mainState.value.name2,
                                                mainState.value.selectedLanguage2
                                            )
                                        )
                                        onActionTTS(OnAction.CloseDialog2)
                                        return@KeyboardActions
                                    }
                                }),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 15.dp)
                                    .imePadding()
                            )

                            Row(
                                modifier = Modifier.padding(top = 15.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        onActionTTS(OnAction.CloseDialog2)
                                    }, modifier = Modifier
                                        .weight(1f)


                                ) {
                                    Text(
                                        "Cancel",
                                        fontSize = 13.ssp,
                                        fontFamily = FontFamily.Serif
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Button(
                                    onClick = {


                                        if (mainState.value.name2.isNotEmpty() || mainState.value.name2.isNotBlank()) {
                                            onActionTTS(
                                                OnAction.SaveAsMp3(
                                                    mainState.value.text2,
                                                    mainState.value.name2,
                                                    mainState.value.selectedLanguage2
                                                )

                                            )
                                            onActionTTS(OnAction.CloseDialog2)
                                            return@Button
                                        }
                                        if (mainState.value.name.isEmpty() || mainState.value.name.isBlank()) {
                                            Toast.makeText(
                                                context,
                                                "Enter file name",
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                            return@Button
                                        }

                                    }, modifier = Modifier
                                        .weight(1f)


                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        Text(
                                            "Save .mp3",
                                            fontSize = 12.ssp,
                                            fontFamily = FontFamily.Serif
                                        )
                                        Spacer(modifier = Modifier.padding(horizontal = 5.dp))
                                        Icon(
                                            imageVector = Icons.Rounded.Save,
                                            contentDescription = "Save"
                                        )
                                    }
                                }

                            }
                        }
                    }
                }
            }

            Text(
                text = if (mainState.value.mp3File != null) {
                    "File saved at ${mainState.value.mp3File!!.absolutePath}"
                } else {
                    "No file saved"
                },
                textDecoration = TextDecoration.None,
                style = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.ssp,
                    lineHeight = 20.ssp
                ),
                modifier = Modifier.padding(top = 15.dp)
            )
        }
    }

}


