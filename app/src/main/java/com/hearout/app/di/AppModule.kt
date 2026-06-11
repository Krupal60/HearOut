package com.hearout.app.di

import com.hearout.app.data.TTS
import com.hearout.app.ui.screens.viewmodel.TTSViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single { TTS() }
    viewModelOf(::TTSViewModel)
}
