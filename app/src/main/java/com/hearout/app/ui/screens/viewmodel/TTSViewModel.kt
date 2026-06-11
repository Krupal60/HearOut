package com.hearout.app.ui.screens.viewmodel

import android.app.Application
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.hearout.app.R
import com.hearout.app.data.TTS
import com.hearout.app.domain.TtsType
import com.hearout.app.ui.screens.MainScreenState
import com.hearout.app.ui.screens.contract.MainScreenEffect
import com.hearout.app.ui.screens.contract.OnTTTSAction
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.util.Locale
import kotlin.time.Duration.Companion.milliseconds

class TTSViewModel(
    private val tts: TTS,
    application: Application
) : AndroidViewModel(application) {

    private val _mainState = MutableStateFlow(MainScreenState())
    val mainState: StateFlow<MainScreenState> get() = _mainState.asStateFlow()

    private val _effects = Channel<MainScreenEffect>(Channel.BUFFERED)
    val effects: Flow<MainScreenEffect> = _effects.receiveAsFlow()

    init {
        tts.setLanguage("en", Locale.getDefault().country)
    }

    fun onActionTTS(onTTTSAction: OnTTTSAction) {
        when (onTTTSAction) {
            is OnTTTSAction.ChangeName -> changeName(onTTTSAction.name)
            is OnTTTSAction.ChangeText -> changeText(onTTTSAction.text)
            is OnTTTSAction.OnTTTSGetVoices -> onGetVoices(
                onTTTSAction.languageCode,
                onTTTSAction.countryCode,
                false
            )

            is OnTTTSAction.SaveAsMp3 -> saveAsMp3(
                onTTTSAction.text,
                onTTTSAction.fileName,
                onTTTSAction.selectedLanguage
            )

            is OnTTTSAction.SetLanguage -> setLanguage(
                onTTTSAction.languageCode,
                onTTTSAction.countryCode,
                false
            )

            is OnTTTSAction.SpeakText -> speakText(
                onTTTSAction.text,
                onTTTSAction.languageCode,
                onTTTSAction.countryCode,
                onTTTSAction.voiceName
            )

            is OnTTTSAction.SelectedLanguage -> selectedLanguage(onTTTSAction.language)
            OnTTTSAction.Stop -> stop()
            is OnTTTSAction.SelectedCode -> selectedCode(
                onTTTSAction.languageCode,
                onTTTSAction.countryCode
            )

            is OnTTTSAction.IsSpeaking -> isSpeaking(onTTTSAction.ttsType, onTTTSAction.speaking)
            is OnTTTSAction.SelectedVoice -> selectedVoice(onTTTSAction.name)
            is OnTTTSAction.VoiceName -> voiceName(onTTTSAction.name)
            is OnTTTSAction.ChangeText2 -> changeText2(onTTTSAction.text)
            is OnTTTSAction.IsSpeaking2 -> isSpeaking(onTTTSAction.ttsType, onTTTSAction.speaking)
            is OnTTTSAction.OnTTTSGetVoices2 -> onGetVoices(
                onTTTSAction.languageCode,
                onTTTSAction.countryCode,
                true
            )

            is OnTTTSAction.SelectedCode2 -> selectedCode2(
                onTTTSAction.languageCode,
                onTTTSAction.countryCode
            )

            is OnTTTSAction.SelectedLanguage2 -> selectedLanguage2(onTTTSAction.language)
            is OnTTTSAction.SelectedVoice2 -> selectedVoice2(onTTTSAction.name)
            is OnTTTSAction.SetLanguage2 -> setLanguage(
                onTTTSAction.languageCode,
                onTTTSAction.countryCode,
                true
            )

            is OnTTTSAction.SpeakText2 -> speakText(
                onTTTSAction.text,
                onTTTSAction.languageCode,
                onTTTSAction.countryCode,
                onTTTSAction.voiceName
            )

            OnTTTSAction.Stop2 -> stop()
            is OnTTTSAction.VoiceName2 -> voiceName2(onTTTSAction.name)
            OnTTTSAction.OpenDialog -> openDialog()
            OnTTTSAction.CloseDialog -> closeDialog()
            is OnTTTSAction.Convert -> convertLanguage(
                onTTTSAction.text,
                onTTTSAction.selectedLanguage,
                onTTTSAction.selectedLanguage2
            )

            OnTTTSAction.CloseDialog2 -> closeDialog2()
            OnTTTSAction.OpenDialog2 -> openDialog2()
            is OnTTTSAction.ChangeName2 -> changeName2(onTTTSAction.name)
            is OnTTTSAction.ShowToast -> showToast(onTTTSAction.message)
        }
    }

    private fun showToast(message: String) {
        _effects.trySend(MainScreenEffect.ShowToast(message))
    }

    private fun changeName2(name: String) {
        viewModelScope.launch {
            _mainState.value = _mainState.value.copy(name2 = name)
        }
    }

    private fun convertLanguage(text: String, code1: String, code2: String) {
        viewModelScope.launch {
            _mainState.value = _mainState.value.copy(convertLoading = true)
            val options = TranslatorOptions.Builder()
                .setSourceLanguage(code1)
                .setTargetLanguage(code2)
                .build()
            val translator = Translation.getClient(options)
            translator.downloadModelIfNeeded()
                .addOnFailureListener {
                    _mainState.value = _mainState.value.copy(convertLoading = false)
                    Log.e("error", it.toString())
                }
                .addOnSuccessListener {
                    translator.translate(text)
                        .addOnFailureListener {
                            _mainState.value = _mainState.value.copy(convertLoading = false)
                            Log.e("error", it.toString())
                        }
                        .addOnSuccessListener {
                            _mainState.value = _mainState.value.copy(convertLoading = false)
                            _mainState.value = _mainState.value.copy(text2 = it)
                            translator.close()
                        }

                }
        }
    }

    private fun closeDialog2() {
        viewModelScope.launch {
            _mainState.value = _mainState.value.copy(openDialog2 = false, name2 = "")
        }
    }

    private fun closeDialog() {
        viewModelScope.launch {
            _mainState.value = _mainState.value.copy(openDialog = false, name = "")
        }
    }

    private fun openDialog2() {
        viewModelScope.launch {
            _mainState.value = _mainState.value.copy(openDialog2 = true)
        }
    }

    private fun openDialog() {
        viewModelScope.launch {
            _mainState.value = _mainState.value.copy(openDialog = true)
        }
    }

    private fun setLanguage(languageCode: String, countryCode: String, isSecond: Boolean) {
        viewModelScope.launch {
            tts.setLanguage(languageCode, countryCode)
            if (isSecond) {
                _mainState.value =
                    _mainState.value.copy(selectedVoice2 = "Voice 1", voiceName2 = "")
            } else {
                _mainState.value = _mainState.value.copy(selectedVoice = "Voice 1", voiceName = "")
            }
        }
    }

    private fun selectedLanguage2(language: String) {
        viewModelScope.launch {
            _mainState.value = _mainState.value.copy(selectedLanguage2 = language)
        }
    }

    private fun selectedCode2(languageCode: String, countryCode: String) {
        viewModelScope.launch {
            _mainState.value =
                _mainState.value.copy(languageCode2 = languageCode, countryCode2 = countryCode)
        }
    }

    private fun selectedVoice2(name: String) {
        viewModelScope.launch {
            _mainState.value = _mainState.value.copy(selectedVoice2 = name)
        }
    }

    private fun voiceName2(name: String) {
        viewModelScope.launch {
            _mainState.value = _mainState.value.copy(voiceName2 = name)
        }
    }

    private fun onGetVoices(languageCode: String, countryCode: String, isSecond: Boolean) {
        viewModelScope.launch {
            tts.getVoices(languageCode, countryCode).let { voices ->
                val mappedData = if (voices.isEmpty()) {
                    listOf(Triple("Voice 1", "", true))
                } else {
                    voices.sortedBy { it.isNetworkConnectionRequired }.mapIndexed { index, voice ->
                        Triple("Voice ${index + 1}", voice.name, voice.isNetworkConnectionRequired)
                    }
                }.toImmutableList()
                if (isSecond) {
                    _mainState.value = _mainState.value.copy(voiceNameData2 = mappedData)
                } else {
                    _mainState.value = _mainState.value.copy(voiceNameData = mappedData)
                }
            }
        }
    }

    private fun changeText2(text: String) {
        viewModelScope.launch {
            _mainState.value = _mainState.value.copy(text2 = text)
        }
    }

    private fun speakText(
        text: String,
        languageCode: String,
        countryCode: String,
        voiceName: String
    ) {
        viewModelScope.launch {
            delay(200L.milliseconds)
            tts.speakOut(text, languageCode, countryCode, voiceName)
        }
    }

    private fun changeName(name: String) {
        viewModelScope.launch {
            _mainState.value = _mainState.value.copy(name = name)
        }
    }

    private fun changeText(text: String) {
        viewModelScope.launch {
            _mainState.value = _mainState.value.copy(text = text)
        }
    }

    override fun onCleared() {
        tts.shutDown()
        super.onCleared()
    }

    private fun stop() {
        viewModelScope.launch {
            tts.stop()
        }
    }

    private fun voiceName(name: String) {
        viewModelScope.launch {
            _mainState.value = _mainState.value.copy(voiceName = name)
        }
    }

    private fun selectedVoice(name: String) {
        viewModelScope.launch {
            _mainState.value = _mainState.value.copy(selectedVoice = name)
        }
    }

    private fun saveAsMp3(text: String, fileName: String, selectedLanguage: String) {
        viewModelScope.launch {
            val folderName = getApplication<Application>().getString(R.string.app_name)
            val folderPath =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).absolutePath + File.separator + folderName + File.separator + selectedLanguage
            val folder = File(folderPath)

            if (!folder.exists()) {
                folder.mkdirs()
            }
            val mp3FileName = "$fileName.mp3"
            val file = File(folder, mp3FileName)
            val bundle = Bundle()
            val params = HashMap<String, String>()
            val utteranceId = "utteranceId"

            try {
                tts.setVoice(_mainState.value.voiceName)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    val fileOutputStream = FileOutputStream(file)
                    val parcelFileDescriptor = ParcelFileDescriptor.dup(fileOutputStream.fd)
                    tts.synthesizeToFile(text, bundle, parcelFileDescriptor, utteranceId)
                    fileOutputStream.close()
                } else {
                    params[TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID] = utteranceId
                    tts.synthesizeToFile(text, params, file.absolutePath)
                }
            } catch (e: Exception) {
                Log.e("TTSViewModel", "Error saving as MP3: ${e.message}", e)
            }

            _mainState.value = _mainState.value.copy(mp3File = file.absoluteFile)
        }
    }

    private var currentJob: Job? = null

    private fun isSpeaking(ttsType: TtsType, speaking: Boolean) {
        currentJob?.cancel()

        if (speaking) {
            currentJob = viewModelScope.launch {
                delay(100L.milliseconds)
                var consecutiveFalseCount = 0
                val maxConsecutiveFalseCount = 25
                val maxRepeats = 5000

                repeat(maxRepeats) {
                    delay(80L.milliseconds)
                    val ttsSpeaking = tts.isSpeaking()

                    _mainState.value = when (ttsType) {
                        TtsType.TTS1 -> _mainState.value.copy(
                            isSpeaking = ttsSpeaking,
                            isSpeaking2 = false
                        )

                        TtsType.TTS2 -> _mainState.value.copy(
                            isSpeaking = false,
                            isSpeaking2 = ttsSpeaking
                        )
                    }

                    if (ttsSpeaking) {
                        consecutiveFalseCount = 0
                    } else {
                        consecutiveFalseCount++
                    }

                    if (consecutiveFalseCount >= maxConsecutiveFalseCount) {
                        return@launch
                    }
                }

                _mainState.value = _mainState.value.copy(
                    isSpeaking = false,
                    isSpeaking2 = false
                )
            }
        } else {
            _mainState.value = _mainState.value.copy(
                isSpeaking = false,
                isSpeaking2 = false
            )
        }
    }

    private fun selectedCode(languageCode: String, countryCode: String) {
        viewModelScope.launch {
            _mainState.value =
                _mainState.value.copy(languageCode = languageCode, countryCode = countryCode)
        }
    }

    private fun selectedLanguage(language: String) {
        viewModelScope.launch {
            _mainState.value = _mainState.value.copy(selectedLanguage = language)
        }
    }
}
