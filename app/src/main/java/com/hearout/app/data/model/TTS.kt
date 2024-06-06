package com.hearout.app.data.model

import android.content.Context
import android.content.Intent
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import java.util.Locale


class TTS(
    private val context: Context
) : TextToSpeech.OnInitListener {

    private var _tts: TextToSpeech? = null
    val tts get() = _tts!!
    private var locale: Locale = Locale.getDefault() // Default locale

    init {
        _tts = TextToSpeech(context, this)
    }

    // Function to set language
    fun setLanguage(language: String, country: String) {
        locale = Locale(language, country)
        _tts?.setLanguage(locale) // Set the language of the TTS instance
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
                startActivity(context, installIntent, null)
            }
        } else {
            Toast.makeText(context, "Initialization Failed!", Toast.LENGTH_SHORT).show()
        }
    }

    // Function to speak out the message
    fun speakOut(message: String, languageCode: String, countryCode: String, name: String) {
        try{
        _tts?.let { tts ->
            // Set language first
            tts.setLanguage(Locale(languageCode, countryCode))

            // If a specific voice name is provided, try to set it
            val availableVoices = tts.voices
            val selectedVoice = availableVoices.find { it.name == name }
            if (selectedVoice != null) {
                tts.voice = selectedVoice  // Set the selected voice
            } else {
                Log.w("TTS", "Voice with name '$name' not found")
            }

            // Now speak the message
            tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, null)
        } ?: run {
            Log.e("TTS", "TextToSpeech engine not initialized yet")
        }}catch (e :Exception){
            Log.e("error",e.toString())
        }
    }


    fun onGetVoices(languageCode: String, countryCode: String): Collection<Voice> {
        val filteredVoices = _tts?.voices?.filter {
            it.locale == Locale(
                languageCode,
                countryCode
            )
        } ?: emptyList()
        return filteredVoices.toMutableList()
    }

    fun isSpeaking(): Boolean {
        Log.e("TTS", "isSpeaking: ${_tts!!.isSpeaking}")
             return _tts!!.isSpeaking

    }
//    fun isSpeaking2(): Boolean {
//        Log.e("TTS", "isSpeaking: ${_tts!!.isSpeaking}")
//             return _tts!!.isSpeaking
//
//    }

    // Function to stop TTS
    fun stop() {
        _tts?.stop()
    }

    fun shutDown() {
        _tts?.shutdown()
    }
}
