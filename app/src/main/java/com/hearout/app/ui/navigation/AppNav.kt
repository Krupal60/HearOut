package com.hearout.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.hearout.app.ui.screens.FileListScreen
import com.hearout.app.ui.screens.TtsScreenImpl

@Composable
fun AppNav(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(AppRoute.Main)
    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<AppRoute.Main> {
                TtsScreenImpl(
                    onNavigateToFileList = {
                        backStack.add(AppRoute.FileList)
                    }
                )
            }

            entry<AppRoute.FileList> {
                FileListScreen(
                    onBack = {
                        backStack.removeLastOrNull()
                    }
                )
            }
        }
    )
}
