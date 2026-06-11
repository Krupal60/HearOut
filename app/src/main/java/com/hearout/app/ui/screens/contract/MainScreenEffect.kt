package com.hearout.app.ui.screens.contract

sealed interface MainScreenEffect {
    data class ShowToast(val message: String) : MainScreenEffect
}
