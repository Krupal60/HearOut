package com.hearout.app.ui.screens.contract

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.io.File

data class FileListViewState(
    val savedFiles: ImmutableList<File> = persistentListOf(),
    val currentlyPlayingFile: File? = null,
    val isPaused: Boolean = false,
    val playbackPosition: Long = 0L,
    val playbackDuration: Long = 0L
)

sealed class FileListAction {
    data object RefreshFiles : FileListAction()
    data class DeleteFile(val file: File) : FileListAction()
    data class PlayFile(val file: File) : FileListAction()
    data object PausePlayback : FileListAction()
    data object ResumePlayback : FileListAction()
    data object StopPlayback : FileListAction()
    data class SeekTo(val position: Long) : FileListAction()
}

sealed class FileListEffect {
    data class ShowToast(val message: String) : FileListEffect()
}
