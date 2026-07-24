package com.hearout.app.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.rounded.CloudDownload
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
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
import java.util.Locale

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

data class TtsScreenState(
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
    val openDialog2: Boolean = false,
    val isMP3Playing: Boolean = false,
    val isPaused: Boolean = false,
    val playbackPosition: Long = 0L,
    val playbackDuration: Long = 0L,
    val isSaving: Boolean = false
)

@Suppress("MultipleContentEmitters", "EffectKeys")
@Composable
fun TtsScreenImpl(
    onNavigateToFileList: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TTSViewModel = koinViewModel()
) {
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
        currentOnAction(OnTTTSAction.RefreshLastSaved)
    }
    TtsScreen(
        state = state,
        onAction = viewModel::onActionTTS,
        onNavigateToFileList = onNavigateToFileList,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TtsScreen(
    state: TtsScreenState,
    onAction: (OnTTTSAction) -> Unit,
    onNavigateToFileList: () -> Unit,
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
                ),
                actions = {
                    FilledTonalButton(
                        onClick = onNavigateToFileList,
                        modifier = Modifier.padding(end = 8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.AutoMirrored.Rounded.List,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Recordings",
                            style = MaterialTheme.typography.bodyMediumEmphasized.copy(
                                fontFamily = FontFamily.Serif
                            )
                        )
                    }
                }
            )
        }
    ) { padding ->
        TtsContent(
            state = state,
            onAction = onAction,
            onNavigateToFileList = onNavigateToFileList,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
private fun TtsContent(
    state: TtsScreenState,
    onAction: (OnTTTSAction) -> Unit,
    onNavigateToFileList: () -> Unit,
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

        LastSavedFilePlayer(
            file = state.mp3File,
            isPlaying = state.isMP3Playing && !state.isPaused,
            playbackPosition = state.playbackPosition,
            playbackDuration = state.playbackDuration,
            isSaving = state.isSaving,
            onPlay = { state.mp3File?.let { onAction(OnTTTSAction.PlayFile(it)) } },
            onPause = { onAction(OnTTTSAction.PausePlayback) },
            onResume = { onAction(OnTTTSAction.ResumePlayback) },
            onStop = { onAction(OnTTTSAction.StopPlayback) },
            onSeek = { onAction(OnTTTSAction.SeekTo(it)) },
            modifier = Modifier.padding(top = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        HistoryCard(
            onClick = onNavigateToFileList,
            modifier = Modifier.padding(bottom = 16.dp)
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
    state: TtsScreenState,
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
        shape = MaterialTheme.shapes.extraLarge
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
                shape = MaterialTheme.shapes.large,
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
    state: TtsScreenState,
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
        shape = MaterialTheme.shapes.extraLarge
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
                shape = MaterialTheme.shapes.large,
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
    Button(
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
        shape = MaterialTheme.shapes.extraLarge
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
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(start = 8.dp)
            ) {
                CircularWavyProgressIndicator(
                    color = MaterialTheme.colorScheme.inverseOnSurface,
                    modifier = Modifier
                        .size(32.dp)
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
        shape = MaterialTheme.shapes.extraLarge
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
    state: TtsScreenState,
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
            shape = MaterialTheme.shapes.extraLarge
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
                    contentDescription = "Download icon",
                    modifier = Modifier.padding(end = 8.dp)
                )
                AnimatedVisibility(visible = state.convertLoading) {
                    CircularWavyProgressIndicator(
                        modifier = Modifier
                            .size(32.dp)
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
            shape = MaterialTheme.shapes.extraLarge,
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
                    shape = MaterialTheme.shapes.large,
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
                        shape = MaterialTheme.shapes.extraLarge
                    ) {
                        Text("Save", fontFamily = FontFamily.Serif)
                    }
                }
            }
        }
    }
}

@Composable
fun LastSavedFilePlayer(
    file: File?,
    isPlaying: Boolean,
    playbackPosition: Long,
    playbackDuration: Long,
    isSaving: Boolean,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onStop: () -> Unit,
    onSeek: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    if (isSaving) {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            androidx.compose.material3.CircularProgressIndicator(modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Saving .mp3...", style = MaterialTheme.typography.bodyMedium)
        }
    } else if (file == null) {
        Text(
            text = "No New file saved",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = modifier.padding(vertical = 8.dp)
        )
    } else {
        Card(
            modifier = modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            colors = androidx.compose.material3.CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Last Saved: ${file.name}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = file.parentFile?.name ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }

                    androidx.compose.material3.IconButton(
                        onClick = {
                            if (isPlaying) onPause()
                            else if (playbackPosition > 0) onResume()
                            else onPlay()
                        }
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Play",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    AnimatedVisibility(playbackPosition > 0 || isPlaying) {
                        androidx.compose.material3.IconButton(onClick = onStop) {
                            Icon(
                                imageVector = Icons.Rounded.Stop,
                                contentDescription = "Stop",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }

                AnimatedVisibility(playbackDuration > 0) {
                    Column {
                        Slider(
                            value = playbackPosition.toFloat(),
                            onValueChange = { onSeek(it.toLong()) },
                            valueRange = 0f..playbackDuration.toFloat(),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = formatDuration(playbackPosition),
                                style = MaterialTheme.typography.labelSmall
                            )
                            Text(
                                text = formatDuration(playbackDuration),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = androidx.compose.material3.CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.History,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "View Saved Recordings",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif
            )
        }
    }
}

private fun formatDuration(durationMs: Long): String {
    val seconds = (durationMs / 1000) % 60
    val minutes = (durationMs / (1000 * 60)) % 60
    return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
}

@Preview
@Composable
private fun LastSavedFilePlayerPreview() {
    HearOutAiTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            LastSavedFilePlayer(
                file = File("example.mp3"),
                isPlaying = false,
                playbackPosition = 0,
                playbackDuration = 30000,
                isSaving = false,
                onPlay = {},
                onPause = {},
                onResume = {},
                onStop = {},
                onSeek = {}
            )
            Spacer(modifier = Modifier.height(8.dp))
            LastSavedFilePlayer(
                file = File("example.mp3"),
                isPlaying = true,
                playbackPosition = 5000,
                playbackDuration = 30000,
                isSaving = false,
                onPlay = {},
                onPause = {},
                onResume = {},
                onStop = {},
                onSeek = {}
            )
        }
    }
}

@Preview
@Composable
private fun TtsScreenPreviewDark() {
    HearOutAiTheme(true) {
        TtsScreen(
            state = TtsScreenState(
                text = "Hello",
                text2 = "नमस्ते",
                mp3File = File("sample.mp3")
            ),
            onAction = {},
            onNavigateToFileList = {}
        )
    }
}

@Preview
@Composable
private fun TtsScreenPreviewLight() {
    HearOutAiTheme(false) {
        TtsScreen(
            state = TtsScreenState(
                text = "Hello",
                text2 = "नमस्ते",
                mp3File = File("sample.mp3")
            ),
            onAction = {},
            onNavigateToFileList = {}
        )
    }
}
