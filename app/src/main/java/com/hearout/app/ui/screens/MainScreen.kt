package com.hearout.app.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hearout.app.R
import com.hearout.app.domain.TtsType
import com.hearout.app.ui.components.SingleDropDownMenu
import com.hearout.app.ui.components.SingleDropDownMenu2
import com.hearout.app.ui.screens.contract.MainScreenEffect
import com.hearout.app.ui.screens.contract.OnTTTSAction
import com.hearout.app.ui.screens.viewmodel.TTSViewModel
import com.hearout.app.ui.theme.HearOutAiTheme
import com.hearout.app.utils.Utils
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.koin.androidx.compose.koinViewModel
import java.io.File

private val LANGUAGES: ImmutableList<Pair<String, String>> = persistentListOf(
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

private fun countryForLanguage(language: String): String = when (language) {
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
        Triple("Voice 1", "", true)
    ),
    val voiceNameData2: ImmutableList<Triple<String, String, Boolean>> = persistentListOf(
        Triple("Voice 1", "", true)
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

@Suppress("MultipleContentEmitters", "EffectKeys")
@Composable
fun MainScreenImpl(modifier: Modifier = Modifier, viewModel: TTSViewModel = koinViewModel()) {
    val state by viewModel.mainState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is MainScreenEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    val currentOnAction by rememberUpdatedState(viewModel::onActionTTS)
    LaunchedEffect(Unit) {
        currentOnAction(OnTTTSAction.OnTTTSGetVoices("en", "IN"))
        currentOnAction(OnTTTSAction.OnTTTSGetVoices2("hi", "IN"))
    }
    MainScreen(
        state = state,
        onAction = viewModel::onActionTTS,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    state: MainScreenState,
    onAction: (OnTTTSAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Welcome To ${stringResource(id = R.string.app_name)}",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        MainContent(
            state = state,
            onAction = onAction,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
private fun MainContent(
    state: MainScreenState,
    onAction: (OnTTTSAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {

        TtsSourceSection(
            state = state,
            onAction = onAction
        )

        Spacer(modifier = Modifier.height(16.dp))

        ConvertButton(
            state = state,
            onAction = onAction
        )

        Spacer(modifier = Modifier.height(16.dp))

        TtsTargetSection(
            state = state,
            onAction = onAction
        )

        Text(
            text = if (state.mp3File != null) {
                "Last Saved File at ${state.mp3File.absolutePath}"
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

    if (state.openDialog) {
        SaveFileDialog(
            fileName = state.name,
            onFileNameChange = { onAction(OnTTTSAction.ChangeName(it)) },
            inputText = state.text,
            selectedLanguage = state.selectedLanguage,
            onDismiss = { onAction(OnTTTSAction.CloseDialog) },
            onAction = onAction
        )
    }

    if (state.openDialog2) {
        SaveFileDialog(
            fileName = state.name2,
            onFileNameChange = { onAction(OnTTTSAction.ChangeName2(it)) },
            inputText = state.text2,
            selectedLanguage = state.selectedLanguage2,
            onDismiss = { onAction(OnTTTSAction.CloseDialog2) },
            onAction = onAction
        )
    }
}

@Suppress("MultipleContentEmitters")
@Composable
private fun TtsSourceSection(
    state: MainScreenState,
    onAction: (OnTTTSAction) -> Unit
) {
    var loading by remember { mutableStateOf(false) }

    LaunchedEffect(state.isSpeaking) {
        if (state.isSpeaking) loading = false
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                SingleDropDownMenu(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp),
                    data = LANGUAGES,
                    selected = state.selectedLanguage,
                    onOptionSelect = { language, languageCode ->
                        val country = countryForLanguage(language)
                        onAction(OnTTTSAction.SelectedLanguage(language))
                        onAction(OnTTTSAction.SelectedCode(languageCode, country))
                        onAction(OnTTTSAction.SetLanguage(languageCode, country))
                        onAction(OnTTTSAction.OnTTTSGetVoices(languageCode, country))
                    })

                SingleDropDownMenu2(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp),
                    data = state.voiceNameData,
                    selected = state.selectedVoice,
                    onOptionSelect = { voice, voiceCode ->
                        onAction(OnTTTSAction.SelectedVoice(voice))
                        onAction(OnTTTSAction.VoiceName(voiceCode))
                    })
            }

            TextField(
                value = state.text,
                onValueChange = { onAction(OnTTTSAction.ChangeText(it)) },
                shape = RoundedCornerShape(16.dp),
                placeholder = {
                    Text(
                        text = "Enter text in ${state.selectedLanguage} language",
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        fontFamily = FontFamily.Serif
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .padding(top = 12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    errorIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )

            Row(modifier = Modifier.padding(top = 16.dp)) {
                SpeakStopButton(
                    isSpeaking = state.isSpeaking,
                    loading = loading,
                    enabled = state.text.isNotBlank(),
                    onSpeak = {
                        loading = true
                        onAction(
                            OnTTTSAction.SpeakText(
                                state.text,
                                state.languageCode,
                                state.countryCode,
                                state.voiceName
                            )
                        )
                        onAction(OnTTTSAction.IsSpeaking(TtsType.TTS1, true))
                    },
                    onStop = {
                        onAction(OnTTTSAction.Stop)
                        loading = false
                        onAction(OnTTTSAction.IsSpeaking(TtsType.TTS1, false))
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .padding(end = 4.dp)
                )

                SaveMp3Button(
                    enabled = state.text.isNotBlank(),
                    onSave = { onAction(OnTTTSAction.OpenDialog) },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .padding(start = 4.dp)
                )
            }
        }
    }
}

@Suppress("MultipleContentEmitters")
@Composable
private fun TtsTargetSection(
    state: MainScreenState,
    onAction: (OnTTTSAction) -> Unit
) {
    val context = LocalContext.current
    var loading by remember { mutableStateOf(false) }

    LaunchedEffect(state.isSpeaking2) {
        if (state.isSpeaking2) loading = false
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .imePadding(),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row {
                SingleDropDownMenu(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp),
                    data = LANGUAGES,
                    selected = state.selectedLanguage2,
                    onOptionSelect = { language, languageCode ->
                        val country = countryForLanguage(language)
                        onAction(OnTTTSAction.SelectedLanguage2(language))
                        onAction(OnTTTSAction.SelectedCode2(languageCode, country))
                        onAction(OnTTTSAction.SetLanguage2(languageCode, country))
                        onAction(OnTTTSAction.OnTTTSGetVoices2(languageCode, country))
                        if (state.text.isNotBlank()) {
                            onAction(
                                OnTTTSAction.Convert(
                                    state.text,
                                    state.languageCode,
                                    languageCode
                                )
                            )
                        }
                    })

                SingleDropDownMenu2(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp),
                    data = state.voiceNameData2,
                    selected = state.selectedVoice2,
                    onOptionSelect = { voice, voiceCode ->
                        onAction(OnTTTSAction.SelectedVoice2(voice))
                        onAction(OnTTTSAction.VoiceName2(voiceCode))
                    })
            }

            TextField(
                value = state.text2,
                onValueChange = { onAction(OnTTTSAction.ChangeText2(it)) },
                placeholder = {
                    Text(
                        text = "Enter text in ${state.selectedLanguage2} language",
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        fontFamily = FontFamily.Serif
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .padding(top = 12.dp),
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    errorIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )

            Row(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .imePadding()
            ) {
                SpeakStopButton(
                    isSpeaking = state.isSpeaking2,
                    loading = loading,
                    enabled = state.text2.isNotBlank(),
                    onSpeak = {
                        if (!Utils.isNetworkAvailable(context)) {
                            onAction(OnTTTSAction.ShowToast("Need Internet Connection"))
                        } else {
                            loading = true
                            onAction(
                                OnTTTSAction.SpeakText2(
                                    state.text2,
                                    state.languageCode2,
                                    state.countryCode2,
                                    state.voiceName2
                                )
                            )
                            onAction(OnTTTSAction.IsSpeaking2(TtsType.TTS2, true))
                        }
                    },
                    onStop = {
                        onAction(OnTTTSAction.Stop2)
                        loading = false
                        onAction(OnTTTSAction.IsSpeaking2(TtsType.TTS2, false))
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .padding(end = 4.dp)
                )

                SaveMp3Button(
                    enabled = state.text2.isNotBlank(),
                    onSave = { onAction(OnTTTSAction.OpenDialog2) },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .padding(start = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun SpeakStopButton(
    isSpeaking: Boolean,
    loading: Boolean,
    enabled: Boolean,
    onSpeak: () -> Unit,
    onStop: () -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.Button(
        onClick = {
            if (!enabled) {
                return@Button
            }
            if (!isSpeaking && !loading) {
                onSpeak()
            } else {
                onStop()
            }
        },
        modifier = modifier,
        enabled = (enabled || isSpeaking),
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                if (isSpeaking) "Stop" else "Speak",
                fontSize = 12.sp,
                fontFamily = FontFamily.Serif
            )
            Spacer(modifier = Modifier.padding(horizontal = 5.dp))
            Icon(
                imageVector = if (isSpeaking) Icons.Rounded.Stop else Icons.Rounded.PlayArrow,
                contentDescription = if (isSpeaking) "Stop" else "Speak"
            )

            AnimatedVisibility(
                visible = loading,
                modifier = Modifier.wrapContentHeight()
            ) {
                CircularWavyProgressIndicator(
                    color = MaterialTheme.colorScheme.inverseOnSurface,
                    modifier = Modifier
                        .size(32.dp)
                        .wrapContentHeight()
                        .padding(start = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun SaveMp3Button(
    enabled: Boolean,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.Button(
        onClick = {
            if (!enabled) {
                return@Button
            }
            onSave()
        },
        modifier = modifier,
        enabled = enabled,
        shape = RoundedCornerShape(24.dp)
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

@Composable
private fun ConvertButton(
    state: MainScreenState,
    onAction: (OnTTTSAction) -> Unit
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        FilledTonalButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            onClick = {
                if (!Utils.isNetworkAvailable(context = context)) {
                    onAction(OnTTTSAction.ShowToast("Need Internet Connection"))
                    return@FilledTonalButton
                }
                if (state.text.isBlank()) {
                    onAction(OnTTTSAction.ShowToast("Enter Text in Above Box"))
                    return@FilledTonalButton
                }
                onAction(
                    OnTTTSAction.Convert(
                        state.text,
                        state.languageCode,
                        state.languageCode2
                    )
                )
            },
            shape = RoundedCornerShape(24.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Convert To ${state.selectedLanguage2}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                Icon(
                    imageVector = Icons.Rounded.CloudDownload,
                    contentDescription = "Download icon"
                )
                AnimatedVisibility(visible = state.convertLoading) {
                    CircularWavyProgressIndicator(
                        modifier = Modifier
                            .size(32.dp)
                            .padding(start = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun SaveFileDialog(
    fileName: String,
    onFileNameChange: (String) -> Unit,
    inputText: String,
    selectedLanguage: String,
    onDismiss: () -> Unit,
    onAction: (OnTTTSAction) -> Unit
) {
    val focus = LocalFocusManager.current

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            tonalElevation = 6.dp
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "Save as MP3",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Enter file name",
                    style = MaterialTheme.typography.bodyMedium
                )

                OutlinedTextField(
                    value = fileName,
                    onValueChange = onFileNameChange,
                    shape = RoundedCornerShape(16.dp),
                    placeholder = {
                        Text(
                            text = "e.g. speech_audio",
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            fontFamily = FontFamily.Serif
                        )
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        focus.clearFocus()
                        if (inputText.isBlank()) {
                            onAction(OnTTTSAction.ShowToast("Enter text"))
                            return@KeyboardActions
                        }
                        if (fileName.isNotBlank()) {
                            onAction(
                                OnTTTSAction.SaveAsMp3(inputText, fileName, selectedLanguage)
                            )
                            onDismiss()
                        }
                    }),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .imePadding()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    androidx.compose.material3.TextButton(onClick = onDismiss) {
                        Text("Cancel", fontFamily = FontFamily.Serif)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    androidx.compose.material3.Button(
                        onClick = {
                            if (inputText.isBlank()) {
                                onAction(OnTTTSAction.ShowToast("Enter text"))
                                return@Button
                            }
                            if (fileName.isNotBlank()) {
                                onAction(
                                    OnTTTSAction.SaveAsMp3(inputText, fileName, selectedLanguage)
                                )
                                onDismiss()
                                return@Button
                            }
                            if (fileName.isBlank()) {
                                onAction(OnTTTSAction.ShowToast("Enter file name"))
                            }
                        },
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text("Save", fontFamily = FontFamily.Serif)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun MainScreenPreviewDark() {
    HearOutAiTheme(true) {
        MainScreen(
            state = MainScreenState(
                text = "Hello, this is a sample text for preview",
                text2 = "नमस्ते, यह पूर्वावलोकन के लिए एक नमूना पाठ है"
            ),
            onAction = {}
        )
    }
}

@Preview
@Composable
private fun MainScreenPreviewLight() {
    HearOutAiTheme(false) {
        MainScreen(
            state = MainScreenState(
                text = "Hello, this is a sample text for preview",
                text2 = "नमस्ते, यह पूर्वावलोकन के लिए एक नमूना पाठ है"
            ),
            onAction = {}
        )
    }
}
