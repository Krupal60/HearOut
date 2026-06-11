package com.hearout.app.data

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.Locale

class TTS : TextToSpeech.OnInitListener, KoinComponent {

    private val context: Context by inject()
    private var _tts: TextToSpeech? = null
    private var locale: Locale = Locale.getDefault()

    init {
        _tts = TextToSpeech(context, this)
    }

    private fun createLocale(languageCode: String, countryCode: String): Locale {
        return Locale.Builder().setLanguage(languageCode).setRegion(countryCode).build()
    }

    // Function to set language
    fun setLanguage(languageCode: String, countryCode: String) {
        locale = createLocale(languageCode, countryCode)
        _tts?.language = locale
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // Set the language on initialization
            val result = _tts?.setLanguage(locale)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                showToast("This Language is not supported", Toast.LENGTH_SHORT)
                showToast("This Language needs to be downloaded", Toast.LENGTH_LONG)

                val installIntent = Intent().apply {
                    action = TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(installIntent)
            }
        } else {
            showToast("Initialization Failed!", Toast.LENGTH_SHORT)
        }
    }

    private fun showToast(message: String, duration: Int) {
        Toast.makeText(context, message, duration).show()
    }

    // Function to speak out the message
    fun speakOut(message: String, languageCode: String, countryCode: String, voiceName: String) {
        try {
            _tts?.let { tts ->
                // Set language first
                val targetLocale = createLocale(languageCode, countryCode)
                if (tts.voice?.locale != targetLocale) {
                    tts.language = targetLocale
                }

                // If a specific voice name is provided, try to set it
                val selectedVoice = tts.voices?.find { it.name == voiceName }
                if (selectedVoice != null) {
                    tts.voice = selectedVoice
                } else if (voiceName.isNotBlank()) {
                    Log.w("TTS", "Voice with name '$voiceName' not found")
                }

                // Now speak the message
                tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, null)
            } ?: Log.e("TTS", "TextToSpeech engine not initialized yet")
        } catch (e: Exception) {
            Log.e("TTS", "Error in speakOut: ${e.message}", e)
        }
    }

    fun getVoices(languageCode: String, countryCode: String): List<Voice> {
        val targetLocale = createLocale(languageCode, countryCode)
        return _tts?.voices?.filter { it.locale == targetLocale } ?: emptyList()
    }

    fun isSpeaking(): Boolean {
        return _tts?.isSpeaking ?: false
    }

    fun stop() {
        _tts?.stop()
    }

    fun shutDown() {
        _tts?.shutdown()
        _tts = null
    }

    fun setVoice(voiceName: String?) {
        if (voiceName.isNullOrBlank()) return
        val voice = _tts?.voices?.find { it.name == voiceName }
        if (voice != null) {
            _tts?.voice = voice
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun synthesizeToFile(
        text: String,
        bundle: Bundle,
        pfd: ParcelFileDescriptor,
        utteranceId: String
    ) {
        _tts?.synthesizeToFile(text, bundle, pfd, utteranceId)
    }

    fun synthesizeToFile(text: String, params: HashMap<String, String>, filePath: String) {
        @Suppress("DEPRECATION")
        _tts?.synthesizeToFile(text, params, filePath)
    }
}