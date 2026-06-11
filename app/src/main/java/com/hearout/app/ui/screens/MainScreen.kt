package com.hearout.app.ui.screens

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hearout.app.R
import com.hearout.app.domain.TtsType
import com.hearout.app.ui.components.SingleDropDownMenu
import com.hearout.app.ui.components.SingleDropDownMenu2
import com.hearout.app.ui.screens.contract.OnTTTSAction
import com.hearout.app.ui.screens.viewmodel.TTSViewModel
import com.hearout.app.utils.Utils
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
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
    val voiceNameData: ImmutableList<Triple<String, String, Boolean>> = persistentListOf(
        Triple(
            "Voice 1",
            "",
            true
        )
    ),
    val voiceNameData2: ImmutableList<Triple<String, String, Boolean>> = persistentListOf(
        Triple(
            "Voice 1",
            "",
            true
        )
    ),
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
fun MainScreenImpl(modifier: Modifier = Modifier, viewModel: TTSViewModel = koinViewModel()) {
    val mainState =
        viewModel.mainState.collectAsStateWithLifecycle()
    MainScreen(mainState, viewModel::onActionTTS, modifier)
}

@Suppress("EffectKeys")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    mainState: State<MainScreenState>,
    onActionTTS: (OnTTTSAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val focus = LocalFocusManager.current
    val currentOnActionTTS by rememberUpdatedState(onActionTTS)

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Welcome To ${stringResource(id = R.string.app_name)}",
                        textDecoration = TextDecoration.None,
                        style = TextStyle(
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            lineHeight = 22.sp
                        )
                    )
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(rememberScrollState())
                .padding(start = 14.dp, end = 14.dp, bottom = 10.dp)
                .background(MaterialTheme.colorScheme.surface)
        ) {

            val languages = persistentListOf(
                "Arabic" to "ar",
                "Bengali" to "bn",
                "Chinese" to "zh",
                "English" to "en",
                "English(US)" to "en",
                "French" to "fr",
                "German" to "de",
                "Gujarati" to "gu",
                "Hindi" to "hi",
                "Indonesian" to "id",
                "Italian" to "it",
                "Japanese" to "ja",
                "Kannada" to "kn",
                "Korean" to "ko",
                "Malayalam" to "ml",
                "Marathi" to "mr",
                "Portuguese" to "pt",
                "Punjabi" to "pa",
                "Russian" to "ru",
                "Spanish" to "es",
                "Swahili" to "sw",
                "Tamil" to "ta",
                "Telugu" to "te",
                "Urdu" to "ur"
            )

            LaunchedEffect(Unit) {
                currentOnActionTTS(OnTTTSAction.OnTTTSGetVoices("en", Locale.getDefault().country))
                currentOnActionTTS(OnTTTSAction.OnTTTSGetVoices2("hi", Locale.getDefault().country))
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
                            onOptionSelect = { language, languageCode ->
                                val country = when (language) {
                                    "English(US)" -> "US"
                                    "French" -> "FR"
                                    "Spanish" -> "ES"
                                    "Chinese" -> "CN"
                                    "Japanese" -> "JP"
                                    "Korean" -> "KR"
                                    "German" -> "DE"
                                    "Italian" -> "IT"
                                    "Portuguese" -> "PT"
                                    "Russian" -> "RU"
                                    "Arabic" -> "AE"
                                    else -> "IN"
                                }
                                onActionTTS(OnTTTSAction.SelectedLanguage(language))
                                onActionTTS(OnTTTSAction.SelectedCode(languageCode, country))
                                CoroutineScope(Dispatchers.Main).launch {
                                    onActionTTS(OnTTTSAction.SetLanguage(languageCode, country))
                                    onActionTTS(OnTTTSAction.OnTTTSGetVoices(languageCode, country))
                                }
                            })


                        SingleDropDownMenu2(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 5.dp),
                            data = mainState.value.voiceNameData,
                            selected = mainState.value.selectedVoice,
                            onOptionSelect = { voice, voiceCode ->
                                CoroutineScope(Dispatchers.Main).launch {
                                    onActionTTS(OnTTTSAction.SelectedVoice(voice))
                                    onActionTTS(OnTTTSAction.VoiceName(voiceCode))
                                }
                            })

                    }

                    TextField(
                        value = mainState.value.text,
                        onValueChange = { string ->

                            onActionTTS(OnTTTSAction.ChangeText(string))
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
                                when {
                                    !mainState.value.isSpeaking && !loading.value -> {
                                        loading.value = true
                                        onActionTTS(
                                            OnTTTSAction.SpeakText(
                                                mainState.value.text,
                                                mainState.value.languageCode,
                                                mainState.value.countryCode,
                                                mainState.value.voiceName
                                            )
                                        )
                                        onActionTTS(OnTTTSAction.IsSpeaking(TtsType.TTS1, true))
                                    }

                                    else -> {
                                        onActionTTS(OnTTTSAction.Stop)
                                        loading.value = false
                                        onActionTTS(OnTTTSAction.IsSpeaking(TtsType.TTS1, false))
                                    }
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
                                    fontSize = 12.sp,
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

                                onActionTTS(OnTTTSAction.OpenDialog)

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
                                    fontSize = 12.sp,
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
                        Toast.makeText(context, "Enter Text in Above Box", Toast.LENGTH_SHORT)
                            .show()
                        return@OutlinedButton
                    }
                    onActionTTS(
                        OnTTTSAction.Convert(
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
                        fontSize = 14.sp,
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
                            onOptionSelect = { language, languageCode ->
                                val country = when (language) {
                                    "English(US)" -> "US"
                                    "French" -> "FR"
                                    "Spanish" -> "ES"
                                    "Chinese" -> "CN"
                                    "Japanese" -> "JP"
                                    "Korean" -> "KR"
                                    "German" -> "DE"
                                    "Italian" -> "IT"
                                    "Portuguese" -> "PT"
                                    "Russian" -> "RU"
                                    "Arabic" -> "AE"
                                    else -> "IN"
                                }
                                onActionTTS(OnTTTSAction.SelectedLanguage2(language))
                                onActionTTS(OnTTTSAction.SelectedCode2(languageCode, country))
                                CoroutineScope(Dispatchers.Main).launch {
                                    onActionTTS(OnTTTSAction.SetLanguage2(languageCode, country))
                                    onActionTTS(
                                        OnTTTSAction.OnTTTSGetVoices2(
                                            languageCode,
                                            country
                                        )
                                    )
                                }
                                if (!Utils.isNetworkAvailable(context = context) && (mainState.value.text.isEmpty() || mainState.value.text.isBlank())) {
                                    return@SingleDropDownMenu
                                }
                                onActionTTS(
                                    OnTTTSAction.Convert(
                                        mainState.value.text,
                                        mainState.value.languageCode,
                                        languageCode
                                    )
                                )
                            })


                        SingleDropDownMenu2(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 5.dp),
                            data = mainState.value.voiceNameData2,
                            selected = mainState.value.selectedVoice2,
                            onOptionSelect = { voice, voiceCode ->
                                CoroutineScope(Dispatchers.Main).launch {
                                    onActionTTS(OnTTTSAction.SelectedVoice2(voice))
                                    onActionTTS(OnTTTSAction.VoiceName2(voiceCode))
                                }
                            })

                    }

                    TextField(
                        value = mainState.value.text2,
                        onValueChange = { string ->

                            onActionTTS(OnTTTSAction.ChangeText2(string))
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
                                when {
                                    !mainState.value.isSpeaking2 && !loading2.value -> {
                                        loading2.value = true
                                        onActionTTS(
                                            OnTTTSAction.SpeakText2(
                                                mainState.value.text2,
                                                mainState.value.languageCode2,
                                                mainState.value.countryCode2,
                                                mainState.value.voiceName2
                                            )
                                        )
                                        onActionTTS(OnTTTSAction.IsSpeaking2(TtsType.TTS2, true))

                                    }

                                    else -> {
                                        onActionTTS(OnTTTSAction.Stop2)
                                        loading2.value = false
                                        onActionTTS(OnTTTSAction.IsSpeaking2(TtsType.TTS2, false))
                                    }
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
                                    fontSize = 12.sp,
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

                                onActionTTS(OnTTTSAction.OpenDialog2)

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
                                    fontSize = 12.sp,
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
                Dialog(onDismissRequest = { onActionTTS(OnTTTSAction.CloseDialog) }) {
                    Card {
                        Column(modifier = Modifier.padding(horizontal = 15.dp, vertical = 20.dp)) {
                            Text(
                                text = "Enter File Name :",
                                textDecoration = TextDecoration.None,
                                style = TextStyle(
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            )




                            OutlinedTextField(
                                value = mainState.value.name,
                                onValueChange = { string ->
                                    onActionTTS(OnTTTSAction.ChangeName(string))
                                },
                                placeholder = {
                                    Text(
                                        text = "Enter name of .mp3 file",
                                        color = Color.Gray,
                                        fontFamily = FontFamily.Serif,
                                        fontSize = 15.sp,
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
                                            OnTTTSAction.SaveAsMp3(
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
                                        onActionTTS(OnTTTSAction.CloseDialog)
                                    }, modifier = Modifier
                                        .height(46.dp)
                                ) {
                                    Text(
                                        "Cancel",
                                        fontSize = 13.sp,
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
                                                OnTTTSAction.SaveAsMp3(
                                                    mainState.value.text,
                                                    mainState.value.name,
                                                    mainState.value.selectedLanguage
                                                )
                                            )
                                            onActionTTS(OnTTTSAction.CloseDialog)
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
                                        .height(46.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        Text(
                                            "Save .mp3",
                                            fontSize = 12.sp,
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
                Dialog(onDismissRequest = { onActionTTS(OnTTTSAction.CloseDialog2) }) {
                    Card {
                        Column(modifier = Modifier.padding(horizontal = 15.dp, vertical = 20.dp)) {
                            Text(
                                text = "Enter File Name :",
                                textDecoration = TextDecoration.None,
                                style = TextStyle(
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            )



                            OutlinedTextField(
                                value = mainState.value.name2,
                                onValueChange = { string ->
                                    onActionTTS(OnTTTSAction.ChangeName2(string))
                                },
                                shape = RoundedCornerShape(12.dp),
                                placeholder = {
                                    Text(
                                        text = "Enter name of .mp3 file",
                                        color = Color.Gray,
                                        fontFamily = FontFamily.Serif,
                                        fontSize = 15.sp,
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
                                            OnTTTSAction.SaveAsMp3(
                                                mainState.value.text2,
                                                mainState.value.name2,
                                                mainState.value.selectedLanguage2
                                            )
                                        )
                                        onActionTTS(OnTTTSAction.CloseDialog2)
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
                                        onActionTTS(OnTTTSAction.CloseDialog2)
                                    }, modifier = Modifier
                                        .height(46.dp)

                                ) {
                                    Text(
                                        "Cancel",
                                        fontSize = 13.sp,
                                        fontFamily = FontFamily.Serif
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Button(
                                    onClick = {
                                        if (mainState.value.name2.isNotEmpty() || mainState.value.name2.isNotBlank()) {
                                            onActionTTS(
                                                OnTTTSAction.SaveAsMp3(
                                                    mainState.value.text2,
                                                    mainState.value.name2,
                                                    mainState.value.selectedLanguage2
                                                )

                                            )
                                            onActionTTS(OnTTTSAction.CloseDialog2)
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
                                        .height(46.dp)


                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        Text(
                                            "Save .mp3",
                                            fontSize = 12.sp,
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
                    "Last Saved File at ${mainState.value.mp3File!!.absolutePath}"
                } else {
                    "No file saved"
                },
                textDecoration = TextDecoration.None,
                style = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                ),
                modifier = Modifier.padding(top = 15.dp)
            )
        }
    }

}

