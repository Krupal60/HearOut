package com.hearout.app.data.model

import android.content.Context
import android.content.Intent
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import android.util.Log
import android.widget.Toast
import java.util.Locale


class TTS(
    private val context: Context
) : TextToSpeech.OnInitListener {

    private var _tts: TextToSpeech? = null
    val tts get() = _tts!!
    private var locale: Locale = Locale.getDefault()

    init {
        _tts = TextToSpeech(context, this)
    }

    // Function to set language
    fun setLanguage(languageCode: String, countryCode: String) {
        locale = Locale.Builder().setLanguage(languageCode).setRegion(countryCode).build()
        _tts?.language = locale
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // Set the language on initialization
            val result = _tts?.setLanguage(locale)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(context, "This Language is not supported", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "This Language needed to download", Toast.LENGTH_LONG)
                    .show()
                val installIntent = Intent()
                installIntent.action = TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA
                context.startActivity(installIntent)
            }
        } else {
            Toast.makeText(context, "Initialization Failed!", Toast.LENGTH_SHORT).show()
        }
    }

    // Function to speak out the message
    fun speakOut(message: String, languageCode: String, countryCode: String, name: String) {
        try {
            _tts?.let { tts ->
                // Set language first
                tts.language =
                    Locale.Builder().setLanguage(languageCode).setRegion(countryCode).build()

                // If a specific voice name is provided, try to set it
                val availableVoices = tts.voices
                val selectedVoice = availableVoices.find { it.name == name }
                if (selectedVoice != null) {
                    tts.voice = selectedVoice
                } else {
                    Log.w("TTS", "Voice with name '$name' not found")
                }

                // Now speak the message
                tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, null)
            } ?: run {
                Log.e("TTS", "TextToSpeech engine not initialized yet")
            }
        } catch (e: Exception) {
            Log.e("error", e.toString())
        }
    }


    fun onGetVoices(languageCode: String, countryCode: String): Collection<Voice> {
        val filteredVoices = _tts?.voices?.filter {
            it.locale == Locale.Builder().setLanguage(languageCode).setRegion(countryCode).build()
        } ?: emptyList()
        return filteredVoices.toMutableList()
    }

    fun isSpeaking(): Boolean {
        return _tts?.isSpeaking ?: false
    }

    // Function to stop TTS
    fun stop() {
        _tts?.stop()
    }

    fun shutDown() {
        _tts?.shutdown()
    }
}
