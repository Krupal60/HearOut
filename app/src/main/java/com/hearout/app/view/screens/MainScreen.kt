package com.hearout.app.view.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hearout.app.R
import com.hearout.app.domain.OnAction
import com.hearout.app.view.components.SingleDropDownMenu
import com.hearout.app.view.components.SingleDropDownMenu2
import com.hearout.app.view.components.ssp
import com.hearout.app.view.utils.Utils
import com.hearout.app.viewmodel.TTSViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File


data class MainScreenState(
    val text: String = "",
    val text2: String = "",
    val name: String = "",
    val isSpeaking: Boolean = false,
    val isSpeaking2: Boolean = false,
    val selectedLanguage: String = "English",
    val selectedLanguage2: String = "Hindi",
    val languageCode: String = "en",
    val languageCode2: String = "hi",
    val countryCode: String = "IN",
    val countryCode2: String = "IN",
    val voiceNameData: List<Triple<String, String, Boolean>> = listOf(Triple("Voice 1", "", true)),
    val voiceNameData2: List<Triple<String, String, Boolean>> = listOf(Triple("Voice 1", "", true)),
    val selectedVoice: String = "Voice 1",
    val selectedVoice2: String = "Voice 1",
    val voiceName: String = "",
    val voiceName2: String = "",
    val mp3File: File? = null,
    val convertLoading: Boolean = false,
    val openDialog: Boolean = false,
    val openDialog2: Boolean = false
)

@Composable
fun MainScreenImpl(viewModel: TTSViewModel = viewModel()) {
    val mainState = viewModel.mainState.collectAsStateWithLifecycle()
    MainScreen(mainState, viewModel::onActionTTS)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(mainState: State<MainScreenState>, onActionTTS: (OnAction) -> Unit) {

    val context = LocalContext.current
    val focus = LocalFocusManager.current


//    MobileAds.initialize(context)
//
//    LaunchedEffect(Unit) {
//        loadInterstitial(context)
//    }


    Scaffold(
        topBar = {
            // Customize your top app bar here
            TopAppBar(
                title = {
                    Text(
                        text = "Welcome To ${stringResource(id = R.string.app_name)}",
                        textDecoration = TextDecoration.None,
                        style = TextStyle(
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.ssp,
                            lineHeight = 22.ssp
                        )
                    )
                }, actions = {
//
//                    val dropDownData = listOf("Term of Use", "Privacy Policy")
//
//                    var expanded by remember { mutableStateOf(false) }
//
//                    IconButton(
//                        onClick = {
//                            expanded = true
//                        },
//                        modifier = Modifier
//                            .align(Alignment.CenterVertically)
//                    ) {
//                        Icon(
//                            imageVector = Icons.Filled.MoreVert,
//                            contentDescription = "more"
//                        )
//                    }
//                    DropdownMenu(
//                        expanded = expanded,
//                        onDismissRequest = { expanded = false },
//                        modifier = Modifier.fillMaxWidth(0.5F)
//                    ) {
//                        dropDownData.forEach { name ->
//                            DropdownMenuItem(text = {
//                                Text(
//                                    text = name, fontSize = 12.ssp,
//                                    fontFamily = FontFamily.Serif
//                                )
//                            }, onClick = {
//                                expanded = false
//                            })
//                        }
//                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = it.calculateTopPadding(), start = 14.dp, end = 14.dp, bottom = 10.dp)
                .background(MaterialTheme.colorScheme.surface)
        ) {

            val languages = listOf(
                "English" to "en",
                "English(US)" to "en",
                "Hindi" to "hi",
                "Gujarati" to "gu",
                "Bengali" to "bn",
                "Telugu" to "te",
                "Marathi" to "mr",
                "Tamil" to "ta",
                "Urdu" to "ur",
                "Kannada" to "kn",
                "Punjabi" to "pa",
                "French" to "fr",
                "Spanish" to "es"
            )



            LaunchedEffect(Unit) {
                CoroutineScope(Dispatchers.Main).launch {
                    onActionTTS(OnAction.OnGetVoices("en", "IN"))
                    onActionTTS(OnAction.OnGetVoices2("hi", "IN"))
                }
            }


            val loading2 = rememberSaveable {
                mutableStateOf(false)
            }
            val loading = rememberSaveable {
                mutableStateOf(false)
            }
            LaunchedEffect(mainState.value.isSpeaking) {
                    if (mainState.value.isSpeaking) loading.value = false
            }
            LaunchedEffect(mainState.value.isSpeaking2) {
                    if (mainState.value.isSpeaking2) loading2.value = false
            }


            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp)
                    .wrapContentHeight()
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 15.dp)
                ) {
                    Row {
                        SingleDropDownMenu(modifier = Modifier
                            .weight(1f)
                            .padding(end = 5.dp),
                            data = languages,
                            selected = mainState.value.selectedLanguage,
                            onOptionSelected = { language, languagecode ->
                                val country = when (language) {
                                    "English(US)" -> {
                                        "US"
                                    }

                                    "French" -> {
                                        "FR"
                                    }

                                    "Spanish" -> {
                                        "ES"
                                    }

                                    else -> {
                                        "IN"
                                    }
                                }
                                onActionTTS(OnAction.SelectedLanguage(language))
                                onActionTTS(OnAction.SelectedCode(languagecode, country))
                                CoroutineScope(Dispatchers.Main).launch {
                                    onActionTTS(OnAction.SetLanguage(languagecode, country))
                                    onActionTTS(OnAction.OnGetVoices(languagecode, country))
                                }
                            })


                        SingleDropDownMenu2(modifier = Modifier
                            .weight(1f)
                            .padding(start = 5.dp),
                            data = mainState.value.voiceNameData,
                            selected = mainState.value.selectedVoice,
                            onOptionSelected = { voice, voiceCode ->
                                CoroutineScope(Dispatchers.Main).launch {
                                    onActionTTS(OnAction.SelectedVoice(voice))
                                    onActionTTS(OnAction.VoiceName(voiceCode))
                                }
                            })

                    }

                    OutlinedTextField(
                        value = mainState.value.text,
                        onValueChange = { string ->

                            onActionTTS(OnAction.ChangeText(string))
                        },
                        label = {
                            Text(
                                text = "Enter text in ${mainState.value.selectedLanguage} language",
                                color = Color.Gray,
                                fontFamily = FontFamily.Serif
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(135.dp)
                            .padding(top = 12.dp)
                    )

                    Row(modifier = Modifier.padding(top = 12.dp)) {
                        Button(
                            onClick = {
                                if (mainState.value.text.isEmpty() || mainState.value.text.isBlank()) {
                                    Toast.makeText(context, "Enter text", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                if (!mainState.value.isSpeaking && !loading.value) {
                                    loading.value = true
                                    onActionTTS(
                                        OnAction.SpeakText(
                                            mainState.value.text,
                                            mainState.value.languageCode,
                                            mainState.value.countryCode,
                                            mainState.value.voiceName
                                        )
                                    )
                                    onActionTTS(OnAction.IsSpeaking(true))
                                } else {
                                    onActionTTS(OnAction.Stop)
                                    loading.value = false
                                    onActionTTS(OnAction.IsSpeaking(false))
                                }
                            }, modifier = Modifier
                                .weight(1f)
                                .padding(end = 5.dp)
                        ) {
                            Text(
                                if (mainState.value.isSpeaking) "Reset" else "Speak",
                                fontSize = 12.ssp,
                                fontFamily = FontFamily.Serif
                            )
                            AnimatedVisibility(visible = loading.value) {
                                CircularProgressIndicator(color = MaterialTheme.colorScheme.inverseOnSurface)
                            }
                        }


                        Button(
                            onClick = {
                                if (mainState.value.text.isEmpty() || mainState.value.text.isBlank()) {
                                    Toast.makeText(context, "Enter text", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }

                                onActionTTS(OnAction.OpenDialog)

                            }, modifier = Modifier
                                .weight(1f)
                                .imePadding()
                                .padding(start = 5.dp)

                        ) {
                            Text(
                                "Save as MP3",
                                fontSize = 12.ssp,
                                fontFamily = FontFamily.Serif
                            )
                        }
                    }
                }

            }

            OutlinedButton(modifier = Modifier
                .fillMaxWidth(0.9f)
                .align(Alignment.CenterHorizontally)
                .height(55.dp)
                .padding(top = 15.dp), onClick = {

                if (!Utils.isNetworkAvailable(context = context)) {
                    Toast.makeText(context, "Need Internet Connection", Toast.LENGTH_SHORT).show()
                    return@OutlinedButton
                }
                if (mainState.value.text.isEmpty() || mainState.value.text.isBlank()) {
                    Toast.makeText(context, "Enter Text", Toast.LENGTH_SHORT).show()
                    return@OutlinedButton
                }
                onActionTTS(
                    OnAction.Convert(
                        mainState.value.text,
                        mainState.value.languageCode,
                        mainState.value.languageCode2
                    )
                )
            }
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Convert To ${mainState.value.selectedLanguage2}",
                        fontSize = 14.ssp,
                        fontFamily = FontFamily.Serif
                    )
                    Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                    Icon(
                        imageVector = Icons.Default.CloudDownload,
                        contentDescription = "Download icon"
                    )
                    AnimatedVisibility(visible = mainState.value.convertLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(40.dp)
                                .padding(start = 15.dp, end = 5.dp)
                        )
                    }
                }


            }



            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp)
                    .wrapContentHeight()
                    .imePadding()
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 15.dp)
                ) {
                    Row {
                        SingleDropDownMenu(modifier = Modifier
                            .weight(1f)
                            .padding(end = 5.dp),
                            data = languages,
                            selected = mainState.value.selectedLanguage2,
                            onOptionSelected = { language, languagecode ->
                                val country = when (language) {
                                    "English(US)" -> {
                                        "US"
                                    }

                                    "French" -> {
                                        "FR"
                                    }

                                    "Spanish" -> {
                                        "ES"
                                    }

                                    else -> {
                                        "IN"
                                    }
                                }
                                onActionTTS(OnAction.SelectedLanguage2(language))
                                onActionTTS(OnAction.SelectedCode2(languagecode, country))
                                CoroutineScope(Dispatchers.Main).launch {
                                    onActionTTS(OnAction.SetLanguage2(languagecode, country))
                                    onActionTTS(OnAction.OnGetVoices2(languagecode, country))
                                }
                            })


                        SingleDropDownMenu2(modifier = Modifier
                            .weight(1f)
                            .padding(start = 5.dp),
                            data = mainState.value.voiceNameData2,
                            selected = mainState.value.selectedVoice2,
                            onOptionSelected = { voice, voiceCode ->
                                CoroutineScope(Dispatchers.Main).launch {
                                    onActionTTS(OnAction.SelectedVoice2(voice))
                                    onActionTTS(OnAction.VoiceName2(voiceCode))
                                }
                            })

                    }

                    OutlinedTextField(
                        value = mainState.value.text2,
                        onValueChange = { string ->

                            onActionTTS(OnAction.ChangeText2(string))
                        },
                        label = {
                            Text(
                                text = "Enter text in ${mainState.value.selectedLanguage2} language",
                                color = Color.Gray,
                                fontFamily = FontFamily.Serif
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(135.dp)
                            .padding(top = 12.dp)
                    )

                    Row(
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .imePadding()
                    ) {
                        Button(
                            onClick = {
                                if (mainState.value.text2.isEmpty() || mainState.value.text2.isBlank()) {
                                    Toast.makeText(context, "Enter text", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                if (!mainState.value.isSpeaking2 && !loading2.value) {
                                    loading2.value = true
                                    onActionTTS(
                                        OnAction.SpeakText2(
                                            mainState.value.text2,
                                            mainState.value.languageCode2,
                                            mainState.value.countryCode2,
                                            mainState.value.voiceName2
                                        )
                                    )
                                    onActionTTS(OnAction.IsSpeaking2(true))

                                } else {
                                    onActionTTS(OnAction.Stop2)
                                    loading2.value = false
                                    onActionTTS(OnAction.IsSpeaking2(false))
                                }
                            }, modifier = Modifier
                                .weight(1f)
                                .padding(end = 5.dp)
                        ) {
                            Text(
                                if (mainState.value.isSpeaking2) "Reset" else "Speak",
                                fontSize = 12.ssp,
                                fontFamily = FontFamily.Serif
                            )
                            AnimatedVisibility(loading2.value) {
                                CircularProgressIndicator(color = MaterialTheme.colorScheme.inverseOnSurface)
                            }
                        }


                        Button(
                            onClick = {
                                if (mainState.value.text2.isEmpty() || mainState.value.text2.isBlank()) {
                                    Toast.makeText(context, "Enter text", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }

                                onActionTTS(OnAction.OpenDialog2)

                            }, modifier = Modifier
                                .weight(1f)
                                .padding(start = 5.dp)

                        ) {
                            Text(
                                "Save as MP3",
                                fontSize = 12.ssp,
                                fontFamily = FontFamily.Serif
                            )
                        }
                    }
                }

            }


            if (mainState.value.openDialog) {
                Dialog(onDismissRequest = { onActionTTS(OnAction.CloseDialog) }) {
                    Card {
                        Column(modifier = Modifier.padding(horizontal = 15.dp, vertical = 20.dp)) {
                            Text(
                                text = "Enter File Name :",
                                textDecoration = TextDecoration.None,
                                style = TextStyle(
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.ssp
                                )
                            )




                            OutlinedTextField(
                                value = mainState.value.name,
                                onValueChange = { string ->
                                    onActionTTS(OnAction.ChangeName(string))
                                },
                                placeholder = {
                                    Text(
                                        text = "Enter name of .mp3 file",
                                        color = Color.Gray,
                                        fontFamily = FontFamily.Serif,
                                        fontSize = 15.ssp,
                                    )
                                },
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                keyboardActions = KeyboardActions(onDone = {
                                    focus.clearFocus()
                                    if (mainState.value.text.isEmpty() || mainState.value.text.isBlank()) {
                                        Toast.makeText(
                                            context,
                                            "Enter text",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                        return@KeyboardActions
                                    }
                                    if (mainState.value.name.isNotEmpty() || mainState.value.name.isNotBlank()) {
                                        onActionTTS(
                                            OnAction.SaveAsMp3(
                                                mainState.value.text,
                                                mainState.value.name + "_${mainState.value.selectedLanguage}"
                                            )
                                        )
                                        //                        showInterstitial(context) {
                                        //
                                        //                        }
                                        return@KeyboardActions
                                    }
                                }),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 15.dp)
                            )

                            Row(
                                modifier = Modifier
                                    .padding(top = 15.dp)
                                    .imePadding(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        onActionTTS(OnAction.CloseDialog)
                                    }, modifier = Modifier
                                        .weight(1f)

                                ) {
                                    Text(
                                        "Cancel",
                                        fontSize = 13.ssp,
                                        fontFamily = FontFamily.Serif
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Button(
                                    onClick = {

                                        if (mainState.value.text.isEmpty() || mainState.value.text.isBlank()) {
                                            Toast.makeText(
                                                context,
                                                "Enter text",
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                            return@Button
                                        }

                                        if (mainState.value.name.isNotEmpty() || mainState.value.name.isNotBlank()) {
                                            onActionTTS(
                                                OnAction.SaveAsMp3(
                                                    mainState.value.text,
                                                    mainState.value.name + "_${mainState.value.selectedLanguage}"
                                                )
                                            )
                                            onActionTTS(OnAction.CloseDialog)
                                            return@Button
                                        }
                                        if (mainState.value.name.isEmpty() || mainState.value.name.isBlank()) {
                                            Toast.makeText(
                                                context,
                                                "Enter file name",
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                            return@Button
                                        }

                                    }, modifier = Modifier
                                        .weight(1f)

                                ) {
                                    Text(
                                        "Save as MP3",
                                        fontSize = 13.ssp,
                                        fontFamily = FontFamily.Serif
                                    )
                                }
                            }
                        }
                    }
                }

            }
            if (mainState.value.openDialog2) {
                Dialog(onDismissRequest = { onActionTTS(OnAction.CloseDialog2) }) {
                    Card {
                        Column(modifier = Modifier.padding(horizontal = 15.dp, vertical = 20.dp)) {
                            Text(
                                text = "Enter File Name :",
                                textDecoration = TextDecoration.None,
                                style = TextStyle(
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.ssp
                                )
                            )



                            OutlinedTextField(
                                value = mainState.value.name,
                                onValueChange = { string ->
                                    onActionTTS(OnAction.ChangeName(string))
                                },
                                placeholder = {
                                    Text(
                                        text = "Enter name of .mp3 file",
                                        color = Color.Gray,
                                        fontFamily = FontFamily.Serif,
                                        fontSize = 15.ssp,
                                    )
                                },
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                keyboardActions = KeyboardActions(onDone = {
                                    focus.clearFocus()
                                    if (mainState.value.text.isEmpty() || mainState.value.text.isBlank()) {
                                        Toast.makeText(context, "Enter text", Toast.LENGTH_SHORT)
                                            .show()
                                        return@KeyboardActions
                                    }
                                    if (mainState.value.name.isNotEmpty() || mainState.value.name.isNotBlank()) {
                                        onActionTTS(
                                            OnAction.SaveAsMp3(
                                                mainState.value.text,
                                                mainState.value.name + "_${mainState.value.selectedLanguage2}"
                                            )
                                        )
                                        onActionTTS(OnAction.CloseDialog2)
//                        showInterstitial(context) {
//
//                        }
                                        return@KeyboardActions
                                    }
                                }),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 15.dp)
                                    .imePadding()
                            )

                            Row(
                                modifier = Modifier.padding(top = 15.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        onActionTTS(OnAction.CloseDialog2)
                                    }, modifier = Modifier
                                        .weight(1f)


                                ) {
                                    Text(
                                        "Cancel",
                                        fontSize = 13.ssp,
                                        fontFamily = FontFamily.Serif
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Button(
                                    onClick = {


                                        if (mainState.value.name.isNotEmpty() || mainState.value.name.isNotBlank()) {
                                            onActionTTS(
                                                OnAction.SaveAsMp3(
                                                    mainState.value.text2,
                                                    mainState.value.name + "_${mainState.value.selectedLanguage2}"
                                                )

                                            )
                                            onActionTTS(OnAction.CloseDialog2)
                                            return@Button
                                        }
                                        if (mainState.value.name.isEmpty() || mainState.value.name.isBlank()) {
                                            Toast.makeText(
                                                context,
                                                "Enter file name",
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                            return@Button
                                        }

                                    }, modifier = Modifier
                                        .weight(1f)


                                ) {
                                    Text(
                                        "Save as MP3",
                                        fontSize = 13.ssp,
                                        fontFamily = FontFamily.Serif,

                                        )
                                }

                            }
                        }
                    }
                }

            }





            Text(
                text = if (mainState.value.mp3File != null) {
                    "File saved at ${mainState.value.mp3File!!.absolutePath}"
                } else {
                    "No file saved"
                },
                textDecoration = TextDecoration.None,
                style = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.ssp,
                    lineHeight = 20.ssp
                ),
                modifier = Modifier.padding(top = 15.dp)
            )
//
//            AdmobBanner(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 12.dp)
//            )
        }
    }

}


//private var mInterstitialAd: InterstitialAd? = null
//
//fun loadInterstitial(context: Context) {
//    if (mInterstitialAd == null) {
//        InterstitialAd.load(context,
//            "ca-app-pub-4376590123999291/6874236744",
//            AdRequest.Builder().build(),
//            object : InterstitialAdLoadCallback() {
//                override fun onAdFailedToLoad(adError: LoadAdError) {
//                    mInterstitialAd = null
//                    Log.e("adError", adError.message)
//                }
//
//                override fun onAdLoaded(interstitialAd: InterstitialAd) {
//                    mInterstitialAd = interstitialAd
//                }
//            })
//    }
//}
//
//fun showInterstitial(context: Context, onAdDismissed: () -> Unit) {
//    if (mInterstitialAd != null) {
//        mInterstitialAd!!.fullScreenContentCallback = object : FullScreenContentCallback() {
//            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
//                mInterstitialAd = null
//            }
//
//            override fun onAdDismissedFullScreenContent() {
//                mInterstitialAd = null
//                onAdDismissed()
//                CoroutineScope(Dispatchers.Main).launch {
//                    loadInterstitial(context)
//                }
//            }
//        }
//        mInterstitialAd!!.show(context as Activity)
//    } else {
//        CoroutineScope(Dispatchers.Main).launch {
//            loadInterstitial(context)
//        }
//    }
//}


