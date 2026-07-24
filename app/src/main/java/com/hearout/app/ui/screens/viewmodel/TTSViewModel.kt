package com.hearout.app.ui.screens.viewmodel

import android.app.Application
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.hearout.app.R
import com.hearout.app.data.TTS
import com.hearout.app.domain.TtsType
import com.hearout.app.ui.screens.TtsScreenState
import com.hearout.app.ui.screens.contract.MainScreenEffect
import com.hearout.app.ui.screens.contract.OnTTTSAction
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import kotlin.time.Duration.Companion.milliseconds

class TTSViewModel(
    private val tts: TTS,
    application: Application
) : AndroidViewModel(application) {

    val mainState: StateFlow<TtsScreenState>
        field = MutableStateFlow(TtsScreenState())

    val effects: SharedFlow<MainScreenEffect>
        field = MutableSharedFlow<MainScreenEffect>()

    private var mediaPlayer: MediaPlayer? = null
    private var progressJob: Job? = null

    init {
        tts.setLanguage("en", "IN")
        tts.setProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                mainState.value = mainState.value.copy(isSaving = true)
            }

            override fun onDone(utteranceId: String?) {
                mainState.value = mainState.value.copy(isSaving = false)
                refreshLastSaved()
            }

            @Deprecated("Deprecated in Java")
            override fun onError(utteranceId: String?) {
                mainState.value = mainState.value.copy(isSaving = false)
            }
        })
        refreshLastSaved()
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
            is OnTTTSAction.ShowToast -> showToastUI(onTTTSAction.message)
            is OnTTTSAction.PlayFile -> playFile(onTTTSAction.file)
            OnTTTSAction.StopPlayback -> stopPlayback()
            OnTTTSAction.PausePlayback -> pausePlayback()
            OnTTTSAction.ResumePlayback -> resumePlayback()
            is OnTTTSAction.SeekTo -> seekTo(onTTTSAction.position)
            OnTTTSAction.RefreshLastSaved -> refreshLastSaved()
        }
    }

    private fun refreshLastSaved() {
        viewModelScope.launch {
            val folderName = getApplication<Application>().getString(R.string.app_name)
            val rootFolder = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC),
                folderName
            )
            if (rootFolder.exists()) {
                val mostRecentFile = rootFolder.walkTopDown()
                    .filter { it.isFile && it.extension == "mp3" }
                    .maxByOrNull { it.lastModified() }

                if (mostRecentFile != null && mostRecentFile.exists()) {
                    mainState.value = mainState.value.copy(mp3File = mostRecentFile)
                } else {
                    mainState.value = mainState.value.copy(mp3File = null)
                }
            } else {
                mainState.value = mainState.value.copy(mp3File = null)
            }
        }
    }

    private fun playFile(file: File) {
        viewModelScope.launch {
            stopPlayback()
            try {
                mediaPlayer = MediaPlayer().apply {
                    setDataSource(file.absolutePath)
                    prepare()
                    start()
                    mainState.value = mainState.value.copy(
                        isMP3Playing = true,
                        isPaused = false,
                        playbackDuration = duration.toLong()
                    )
                    startProgressTracker()
                    setOnCompletionListener {
                        stopPlayback()
                    }
                }
            } catch (e: Exception) {
                Log.e("TTSViewModel", "Error playing file", e)
                showToast("Error playing file")
            }
        }
    }

    private fun pausePlayback() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                mainState.value = mainState.value.copy(isPaused = true)
                stopProgressTracker()
            }
        }
    }

    private fun resumePlayback() {
        mediaPlayer?.let {
            if (!it.isPlaying) {
                it.start()
                mainState.value = mainState.value.copy(isPaused = false)
                startProgressTracker()
            }
        }
    }

    private fun stopPlayback() {
        stopProgressTracker()
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        mainState.value = mainState.value.copy(
            isMP3Playing = false,
            isPaused = false,
            playbackPosition = 0L,
            playbackDuration = 0L
        )
    }

    private fun seekTo(position: Long) {
        mediaPlayer?.seekTo(position.toInt())
        mainState.value = mainState.value.copy(playbackPosition = position)
    }

    private fun startProgressTracker() {
        progressJob?.cancel()
        progressJob = viewModelScope.launch {
            while (true) {
                mediaPlayer?.let {
                    if (it.isPlaying) {
                        mainState.value =
                            mainState.value.copy(playbackPosition = it.currentPosition.toLong())
                    }
                }
                delay(200.milliseconds)
            }
        }
    }

    private fun stopProgressTracker() {
        progressJob?.cancel()
        progressJob = null
    }

    private suspend fun showToast(message: String) {
        effects.emit(MainScreenEffect.ShowToast(message))
    }

    private fun showToastUI(message: String) {
        effects.tryEmit(MainScreenEffect.ShowToast(message))
    }

    private fun changeName2(name: String) {
        viewModelScope.launch {
            mainState.value = mainState.value.copy(name2 = name)
        }
    }

    private fun convertLanguage(text: String, code1: String, code2: String) {
        viewModelScope.launch {
            mainState.value = mainState.value.copy(convertLoading = true)
            val options = TranslatorOptions.Builder()
                .setSourceLanguage(code1)
                .setTargetLanguage(code2)
                .build()
            val translator = Translation.getClient(options)
            translator.downloadModelIfNeeded()
                .addOnFailureListener {
                    mainState.value = mainState.value.copy(convertLoading = false)
                    Log.e("error", it.toString())
                }
                .addOnSuccessListener {
                    translator.translate(text)
                        .addOnFailureListener {
                            mainState.value = mainState.value.copy(convertLoading = false)
                            Log.e("error", it.toString())
                        }
                        .addOnSuccessListener {
                            mainState.value = mainState.value.copy(convertLoading = false)
                            mainState.value = mainState.value.copy(text2 = it)
                            translator.close()
                        }

                }
        }
    }

    private fun closeDialog2() {
        viewModelScope.launch {
            mainState.value = mainState.value.copy(openDialog2 = false, name2 = "")
        }
    }

    private fun closeDialog() {
        viewModelScope.launch {
            mainState.value = mainState.value.copy(openDialog = false, name = "")
        }
    }

    private fun openDialog2() {
        viewModelScope.launch {
            mainState.value = mainState.value.copy(openDialog2 = true)
        }
    }

    private fun openDialog() {
        viewModelScope.launch {
            mainState.value = mainState.value.copy(openDialog = true)
        }
    }

    private fun setLanguage(languageCode: String, countryCode: String, isSecond: Boolean) {
        viewModelScope.launch {
            tts.setLanguage(languageCode, countryCode)
            if (isSecond) {
                mainState.value =
                    mainState.value.copy(selectedVoice2 = "Voice 1", voiceName2 = "")
            } else {
                mainState.value = mainState.value.copy(selectedVoice = "Voice 1", voiceName = "")
            }
        }
    }

    private fun selectedLanguage2(language: String) {
        viewModelScope.launch {
            mainState.value = mainState.value.copy(selectedLanguage2 = language)
        }
    }

    private fun selectedCode2(languageCode: String, countryCode: String) {
        viewModelScope.launch {
            mainState.value =
                mainState.value.copy(languageCode2 = languageCode, countryCode2 = countryCode)
        }
    }

    private fun selectedVoice2(name: String) {
        viewModelScope.launch {
            mainState.value = mainState.value.copy(selectedVoice2 = name)
        }
    }

    private fun voiceName2(name: String) {
        viewModelScope.launch {
            mainState.value = mainState.value.copy(voiceName2 = name)
        }
    }

    private fun onGetVoices(languageCode: String, countryCode: String, isSecond: Boolean) {
        viewModelScope.launch {
            tts.isInitialized.first { it }
            tts.getVoices(languageCode, countryCode).let { voices ->
                val mappedData = if (voices.isEmpty()) {
                    listOf(Triple("Voice 1", "", true))
                } else {
                    voices.sortedBy { it.isNetworkConnectionRequired }.mapIndexed { index, voice ->
                        Triple("Voice ${index + 1}", voice.name, voice.isNetworkConnectionRequired)
                    }
                }.toImmutableList()
                if (isSecond) {
                    mainState.value = mainState.value.copy(voiceNameData2 = mappedData)
                } else {
                    mainState.value = mainState.value.copy(voiceNameData = mappedData)
                }
            }
        }
    }

    private fun changeText2(text: String) {
        viewModelScope.launch {
            mainState.value = mainState.value.copy(text2 = text)
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
            mainState.value = mainState.value.copy(name = name)
        }
    }

    private fun changeText(text: String) {
        viewModelScope.launch {
            mainState.value = mainState.value.copy(text = text)
        }
    }

    override fun onCleared() {
        tts.shutDown()
        mediaPlayer?.release()
    }

    private fun stop() {
        viewModelScope.launch {
            tts.stop()
        }
    }

    private fun voiceName(name: String) {
        viewModelScope.launch {
            mainState.value = mainState.value.copy(voiceName = name)
        }
    }

    private fun selectedVoice(name: String) {
        viewModelScope.launch {
            mainState.value = mainState.value.copy(selectedVoice = name)
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
                tts.setVoice(mainState.value.voiceName)

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

            mainState.value = mainState.value.copy(mp3File = file.absoluteFile)
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

                    mainState.value = when (ttsType) {
                        TtsType.TTS1 -> mainState.value.copy(
                            isSpeaking = ttsSpeaking,
                            isSpeaking2 = false
                        )

                        TtsType.TTS2 -> mainState.value.copy(
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

                mainState.value = mainState.value.copy(
                    isSpeaking = false,
                    isSpeaking2 = false
                )
            }
        } else {
            mainState.value = mainState.value.copy(
                isSpeaking = false,
                isSpeaking2 = false
            )
        }
    }

    private fun selectedCode(languageCode: String, countryCode: String) {
        viewModelScope.launch {
            mainState.value =
                mainState.value.copy(languageCode = languageCode, countryCode = countryCode)
        }
    }

    private fun selectedLanguage(language: String) {
        viewModelScope.launch {
            mainState.value = mainState.value.copy(selectedLanguage = language)
        }
    }
}
