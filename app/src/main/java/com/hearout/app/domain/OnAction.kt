package com.hearout.app.domain

sealed class OnAction {

    data class SpeakText(
        val text: String,
        val languageCode: String,
        val countryCode: String,
        val voiceName: String
    ) : OnAction()

    data class SpeakText2(
        val text: String,
        val languageCode: String,
        val countryCode: String,
        val voiceName: String
    ) : OnAction()

    data class SaveAsMp3(
        val text: String, val fileName: String, val selectedLanguage: String
    ) : OnAction()

    data object Stop : OnAction()
    data object Stop2 : OnAction()
    data class ChangeName(val name: String) : OnAction()
    data class ChangeName2(val name: String) : OnAction()
    data class ChangeText(val text: String) : OnAction()
    data class ChangeText2(val text: String) : OnAction()
    data class IsSpeaking(val ttsType: TtsType,val speaking: Boolean) : OnAction()
    data class IsSpeaking2(val ttsType: TtsType,val speaking: Boolean) : OnAction()
    data class SelectedLanguage(val language: String) : OnAction()
    data class SelectedLanguage2(val language: String) : OnAction()
    data class SelectedVoice(val name: String) : OnAction()
    data class SelectedVoice2(val name: String) : OnAction()
    data class VoiceName(val name: String) : OnAction()
    data class VoiceName2(val name: String) : OnAction()
    data class SelectedCode(val languageCode: String, val countryCode: String) : OnAction()
    data class SelectedCode2(val languageCode: String, val countryCode: String) : OnAction()
    data class OnGetVoices(
        val languageCode: String,
        val countryCode: String
    ) : OnAction()
    data class OnGetVoices2(
        val languageCode: String,
        val countryCode: String
    ) : OnAction()

    data class SetLanguage(
        val languageCode: String,
        val countryCode: String
    ) : OnAction()
    data class SetLanguage2(
        val languageCode: String,
        val countryCode: String
    ) : OnAction()

    data class Convert(val text:String,val selectedLanguage:String,val selectedLanguage2:String) : OnAction()
    data object OpenDialog : OnAction()
    data object OpenDialog2 : OnAction()
    data object CloseDialog : OnAction()
    data object CloseDialog2 : OnAction()
}