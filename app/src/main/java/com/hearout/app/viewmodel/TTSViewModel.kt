package com.hearout.app.viewmodel

import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.hearout.app.HearOut
import com.hearout.app.R
import com.hearout.app.data.model.TTS
import com.hearout.app.domain.OnAction
import com.hearout.app.domain.TtsType
import com.hearout.app.view.screens.MainScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.util.Locale

class TTSViewModel : ViewModel() {

    private val tts = TTS(HearOut.hearOut!!.applicationContext)


    private val _mainState = MutableStateFlow(MainScreenState())
    val mainState: StateFlow<MainScreenState> get() = _mainState.asStateFlow()


    init {
        viewModelScope.launch(Dispatchers.IO) {
            tts.setLanguage("en", Locale.getDefault().country)
        }
    }


    fun onActionTTS(onAction: OnAction) {
        when (onAction) {
            is OnAction.ChangeName -> changeName(onAction.name)
            is OnAction.ChangeText -> changeText(onAction.text)
            is OnAction.OnGetVoices -> onGetVoices(onAction.languageCode, onAction.countryCode)
            is OnAction.SaveAsMp3 -> saveAsMp3(onAction.text, onAction.fileName,onAction.selectedLanguage)
            is OnAction.SetLanguage -> setLanguage(onAction.languageCode, onAction.countryCode)
            is OnAction.SpeakText -> speakText(
                onAction.text,
                onAction.languageCode,
                onAction.countryCode,
                onAction.voiceName
            )

            is OnAction.SelectedLanguage -> selectedLanguage(onAction.language)
            OnAction.Stop -> stop()
            is OnAction.SelectedCode -> selectedCode(onAction.languageCode, onAction.countryCode)
            is OnAction.IsSpeaking -> isSpeaking(onAction.ttsType,onAction.speaking)
            is OnAction.SelectedVoice -> selectedVoice(onAction.name)
            is OnAction.VoiceName -> voiceName(onAction.name)
            is OnAction.ChangeText2 -> changeText2(onAction.text)
            is OnAction.IsSpeaking2 -> isSpeaking(onAction.ttsType,onAction.speaking)
            is OnAction.OnGetVoices2 -> onGetVoices2(onAction.languageCode, onAction.countryCode)
            is OnAction.SelectedCode2 -> selectedCode2(onAction.languageCode, onAction.countryCode)
            is OnAction.SelectedLanguage2 -> selectedLanguage2(onAction.language)
            is OnAction.SelectedVoice2 -> selectedVoice2(onAction.name)
            is OnAction.SetLanguage2 -> setLanguage2(onAction.languageCode, onAction.countryCode)
            is OnAction.SpeakText2 -> speakText2(
                onAction.text,
                onAction.languageCode,
                onAction.countryCode,
                onAction.voiceName
            )

            OnAction.Stop2 -> stop()
            is OnAction.VoiceName2 -> voiceName2(onAction.name)
            OnAction.OpenDialog -> openDialog()
            OnAction.CloseDialog -> closeDialog()
            is OnAction.Convert -> convertLanguage(
                onAction.text,
                onAction.selectedLanguage,
                onAction.selectedLanguage2
            )

            OnAction.CloseDialog2 -> closeDialog2()
            OnAction.OpenDialog2 -> openDialog2()
            is OnAction.ChangeName2 -> changeName2(onAction.name)
        }
    }

    private fun changeName2(name: String) {
        _mainState.value = _mainState.value.copy(name2 = name)
    }


    private fun convertLanguage(text: String, code1: String, code2: String) {
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

    private fun closeDialog2() {
        _mainState.value = _mainState.value.copy(openDialog2 = false)
    }

    private fun closeDialog() {
        _mainState.value = _mainState.value.copy(openDialog = false)
    }

    private fun openDialog2() {
        _mainState.value = _mainState.value.copy(openDialog2 = true)
    }

    private fun openDialog() {
        _mainState.value = _mainState.value.copy(openDialog = true)
    }

    private fun speakText2(
        text: String,
        languageCode: String,
        countryCode: String,
        voiceName: String
    ) {


        // Call the TTS method to speak out the text
        viewModelScope.launch {
                delay(200L)
                    tts.speakOut(text, languageCode, countryCode, voiceName)
        }
    }

    private fun setLanguage2(languageCode: String, countryCode: String) {
        viewModelScope.launch(Dispatchers.IO) {
            tts.setLanguage(languageCode, countryCode)
            _mainState.value = _mainState.value.copy(selectedVoice2 = "Voice 1")
            _mainState.value = _mainState.value.copy(voiceName2 = "")
        }
    }

    private fun selectedLanguage2(language: String) {
        _mainState.value = _mainState.value.copy(selectedLanguage2 = language)
    }

    private fun selectedCode2(languageCode: String, countryCode: String) {
        _mainState.value =
            _mainState.value.copy(languageCode2 = languageCode, countryCode2 = countryCode)
    }

    private fun selectedVoice2(name: String) {
        _mainState.value = _mainState.value.copy(selectedVoice2 = name)
    }

    private fun voiceName2(name: String) {
        _mainState.value = _mainState.value.copy(voiceName2 = name)
    }

    private fun onGetVoices2(languageCode: String, countryCode: String) {
        viewModelScope.launch(Dispatchers.IO) {
            tts.onGetVoices(languageCode, countryCode).toList().let { voices ->
                val mappedData = if (voices.isEmpty()) {
                    listOf(Triple("Voice 1", "", true))
                } else {
                    voices.sortedBy { it.isNetworkConnectionRequired }.mapIndexed { index, voice ->
                        Triple("Voice ${index + 1}", voice.name, voice.isNetworkConnectionRequired)
                    }
                }
                // Update voiceNameData with the mapped data
                viewModelScope.launch(Dispatchers.IO) {
                    _mainState.value = _mainState.value.copy(voiceNameData2 = mappedData)
                }
            }
        }
    }



//    private fun isSpeaking2(speaking: Boolean) {
//        if (speaking) {
//            viewModelScope.launch {
//                delay(100L)
//                repeat(15) { // Check 15 times with a delay
//                    delay(100L) // Short delay between checks
//                    val isTtsSpeaking = tts.isSpeaking2()
//                    if (isTtsSpeaking) {
//                        _mainState.value = _mainState.value.copy(
//                            isSpeaking2 = true,
//                            isSpeaking = false
//                        )
//                    }
//                }
//            }
//            return
//        }
//        if (!speaking) {
//            _mainState.value = _mainState.value.copy(
//                isSpeaking2 = false,
//                isSpeaking = false,
//            )
//        }
//
//    }


    private fun changeText2(text: String) {
        _mainState.value = _mainState.value.copy(text2 = text)
    }


    private fun speakText(
        text: String,
        languageCode: String,
        countryCode: String,
        voiceName: String
    ) {
        // Call the TTS method to speak out the text
        viewModelScope.launch {
            delay(200L)
            tts.speakOut(text, languageCode, countryCode, voiceName)
        }
    }

    private fun onGetVoices(languageCode: String, countryCode: String) {
        viewModelScope.launch(Dispatchers.IO) {
            tts.onGetVoices(languageCode, countryCode).toList().let { voices ->
                // Map voice names to Pair<String, String> ("Voice N" to "voiceName")
                val mappedData = if (voices.isEmpty()) {
                    listOf(Triple("Voice 1", "", true))
                } else {
                    voices.sortedBy { it.isNetworkConnectionRequired }.mapIndexed { index, voice ->
                        Triple("Voice ${index + 1}", voice.name, voice.isNetworkConnectionRequired)
                    }
                }
                Log.e("voices", mappedData.toString())
                // Update voiceNameData with the mapped data
                viewModelScope.launch(Dispatchers.IO) {
                    _mainState.value = _mainState.value.copy(voiceNameData = mappedData)
                }

            }
        }
    }

    private fun setLanguage(language: String, country: String) {
        viewModelScope.launch(Dispatchers.IO) {
            tts.setLanguage(language, country)
            _mainState.value = _mainState.value.copy(selectedVoice = "Voice 1")
            _mainState.value = _mainState.value.copy(voiceName = "")
        }
    }


    private fun changeName(name: String) {
        _mainState.value = _mainState.value.copy(name = name)
    }

    private fun changeText(text: String) {
        _mainState.value = _mainState.value.copy(text = text)
    }

    override fun onCleared() {
        _mainState.value = _mainState.value
        tts.shutDown()
        super.onCleared()
    }

    private fun stop() {
        tts.stop()
    }


    private fun voiceName(name: String) {
        _mainState.value = _mainState.value.copy(voiceName = name)
    }


    private fun selectedVoice(name: String) {
        _mainState.value = _mainState.value.copy(selectedVoice = name)
    }

    private fun saveAsMp3(text: String, fileName: String, selectedLanguage: String) {
        val folderName = HearOut.hearOut!!.getString(R.string.app_name)
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val fileOutputStream = FileOutputStream(file)
            val parcelFileDescriptor = ParcelFileDescriptor.dup(fileOutputStream.fd)
            val availableVoices = tts.tts.voices
            val selectedVoice = availableVoices.find { it.name == _mainState.value.selectedVoice }
            if (selectedVoice != null) {
                tts.tts.voice = selectedVoice  // Set the selected voice
            } else {
                Log.w("TTS", "Voice with name '${_mainState.value.selectedVoice}' not found")
            }
            tts.tts.synthesizeToFile(
                text, bundle, parcelFileDescriptor, utteranceId
            )
        } else {
            val availableVoices = tts.tts.voices
            val selectedVoice = availableVoices.find { it.name == _mainState.value.selectedVoice }
            if (selectedVoice != null) {
                tts.tts.voice = selectedVoice  // Set the selected voice
            } else {
                Log.w("TTS", "Voice with name '${_mainState.value.selectedVoice}' not found")
            }
            params[TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID] = utteranceId
            @Suppress("DEPRECATION") tts.tts.synthesizeToFile(
                text, params, file.absolutePath
            )
        }
        _mainState.value = _mainState.value.copy(mp3File = file.absoluteFile)
    }

//    private fun isSpeaking(speaking: Boolean) {
//        if (speaking) {
//            viewModelScope.launch {
//                delay(100L)
//                repeat(15) { // Check 15 times with a delay)
//                    delay(100L) // Short delay between checks
//                    val ttsSpeaking = tts.isSpeaking()
//                    if (ttsSpeaking) {
//                        _mainState.value = _mainState.value.copy(
//                            isSpeaking2 = false,
//                            isSpeaking = true
//                        )
//                    }
//
//                }
//            }
//            return
//        }
//        if (!speaking) {
//            _mainState.value = _mainState.value.copy(
//                isSpeaking = false,
//                isSpeaking2 = false,
//            )
//        }
//
//    }

    private var currentJob: Job? = null
//    private fun isSpeaking(ttsType: TtsType, speaking: Boolean) {
//        currentJob?.cancel()
//
//        if (speaking) {
//            currentJob = viewModelScope.launch(Dispatchers.IO) {
//                delay(100L)
//                repeat(5000) { // Check 15 times with a delay
//                    delay(80L) // Short delay between checks
//                    val ttsSpeaking = tts.isSpeaking()
//                        _mainState.value = when (ttsType) {
//                            TtsType.TTS1 -> _mainState.value.copy(
//                                isSpeaking = ttsSpeaking,
//                                isSpeaking2 = false
//                            )
//                            TtsType.TTS2 -> _mainState.value.copy(
//                                isSpeaking = false,
//                                isSpeaking2 = ttsSpeaking
//                            )
//                        }
//
//
//                }
//            }
//            return
//        }
//        if (!speaking) {
//            _mainState.value = _mainState.value.copy(
//                isSpeaking = false,
//                isSpeaking2 = false,
//            )
//        }
//        return
//    }

    private fun isSpeaking(ttsType: TtsType, speaking: Boolean) {
        // Cancel any ongoing job
        currentJob?.cancel()

        if (speaking) {
            currentJob = viewModelScope.launch {
                delay(100L)
                var consecutiveFalseCount = 0
                val maxConsecutiveFalseCount = 25 // Adjust as needed
                val maxRepeats = 5000 // Adjust repeat count as needed

                repeat(maxRepeats) {
                    delay(80L) // Short delay between checks
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
                        // Reset consecutive false count if TTS is speaking
                        consecutiveFalseCount = 0
                    } else {
                        // Increment consecutive false count if TTS is not speaking
                        consecutiveFalseCount++
                    }

                    // Break the loop if TTS has not spoken for too many consecutive times
                    if (consecutiveFalseCount >= maxConsecutiveFalseCount) {
                        return@launch
                    }
                }

                // Update UI one last time after the loop
                _mainState.value = when (ttsType) {
                    TtsType.TTS1 -> _mainState.value.copy(
                        isSpeaking = false,
                        isSpeaking2 = false
                    )
                    TtsType.TTS2 -> _mainState.value.copy(
                        isSpeaking = false,
                        isSpeaking2 = false
                    )
                }
            }
        } else {
            _mainState.value = _mainState.value.copy(
                isSpeaking = false,
                isSpeaking2 = false
            )
        }
    }




    private fun selectedCode(languagecode: String, countryCode: String) {
        _mainState.value =
            _mainState.value.copy(languageCode = languagecode, countryCode = countryCode)
    }

    private fun selectedLanguage(language: String) {
        _mainState.value = _mainState.value.copy(selectedLanguage = language)
    }


}