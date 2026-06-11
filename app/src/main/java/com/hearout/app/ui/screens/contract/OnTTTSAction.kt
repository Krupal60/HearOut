package com.hearout.app.ui.screens.contract

import com.hearout.app.domain.TtsType

sealed class OnTTTSAction {

    data class SpeakText(
        val text: String,
        val languageCode: String,
        val countryCode: String,
        val voiceName: String
    ) : OnTTTSAction()

    data class SpeakText2(
        val text: String,
        val languageCode: String,
        val countryCode: String,
        val voiceName: String
    ) : OnTTTSAction()

    data class SaveAsMp3(
        val text: String, val fileName: String, val selectedLanguage: String
    ) : OnTTTSAction()

    data object Stop : OnTTTSAction()
    data object Stop2 : OnTTTSAction()
    data class ChangeName(val name: String) : OnTTTSAction()
    data class ChangeName2(val name: String) : OnTTTSAction()
    data class ChangeText(val text: String) : OnTTTSAction()
    data class ChangeText2(val text: String) : OnTTTSAction()
    data class IsSpeaking(val ttsType: TtsType, val speaking: Boolean) : OnTTTSAction()
    data class IsSpeaking2(val ttsType: TtsType, val speaking: Boolean) : OnTTTSAction()
    data class SelectedLanguage(val language: String) : OnTTTSAction()
    data class SelectedLanguage2(val language: String) : OnTTTSAction()
    data class SelectedVoice(val name: String) : OnTTTSAction()
    data class SelectedVoice2(val name: String) : OnTTTSAction()
    data class VoiceName(val name: String) : OnTTTSAction()
    data class VoiceName2(val name: String) : OnTTTSAction()
    data class SelectedCode(val languageCode: String, val countryCode: String) : OnTTTSAction()
    data class SelectedCode2(val languageCode: String, val countryCode: String) : OnTTTSAction()
    data class OnTTTSGetVoices(
        val languageCode: String,
        val countryCode: String
    ) : OnTTTSAction()

    data class OnTTTSGetVoices2(
        val languageCode: String,
        val countryCode: String
    ) : OnTTTSAction()

    data class SetLanguage(
        val languageCode: String,
        val countryCode: String
    ) : OnTTTSAction()

    data class SetLanguage2(
        val languageCode: String,
        val countryCode: String
    ) : OnTTTSAction()

    data class Convert(
        val text: String,
        val selectedLanguage: String,
        val selectedLanguage2: String
    ) : OnTTTSAction()

    data object OpenDialog : OnTTTSAction()
    data object OpenDialog2 : OnTTTSAction()
    data object CloseDialog : OnTTTSAction()
    data object CloseDialog2 : OnTTTSAction()
    data class ShowToast(val message: String) : OnTTTSAction()
}