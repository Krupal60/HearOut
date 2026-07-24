package com.hearout.app.ui.screens.viewmodel

import android.app.Application
import android.media.MediaPlayer
import android.os.Environment
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.hearout.app.R
import com.hearout.app.ui.screens.contract.FileListAction
import com.hearout.app.ui.screens.contract.FileListEffect
import com.hearout.app.ui.screens.contract.FileListViewState
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import kotlin.time.Duration.Companion.milliseconds

class FileListViewModel(application: Application) : AndroidViewModel(application) {

    private val _viewState = MutableStateFlow(FileListViewState())
    val viewState: StateFlow<FileListViewState> = _viewState.onStart {
        refreshFiles()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000.milliseconds),
        initialValue = FileListViewState()
    )

    val effects: SharedFlow<FileListEffect>
        field = MutableSharedFlow<FileListEffect>()

    private var mediaPlayer: MediaPlayer? = null
    private var progressJob: Job? = null

    fun onAction(action: FileListAction) {
        when (action) {
            is FileListAction.RefreshFiles -> refreshFiles()
            is FileListAction.DeleteFile -> deleteFile(action.file)
            is FileListAction.PlayFile -> playFile(action.file)
            is FileListAction.PausePlayback -> pausePlayback()
            is FileListAction.ResumePlayback -> resumePlayback()
            is FileListAction.StopPlayback -> stopPlayback()
            is FileListAction.SeekTo -> seekTo(action.position)
        }
    }

    private fun refreshFiles() {
        viewModelScope.launch {
            val folderName = getApplication<Application>().getString(R.string.app_name)
            val rootFolder = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC),
                folderName
            )
            if (rootFolder.exists()) {
                val allFiles = rootFolder.walkTopDown()
                    .filter { it.isFile && it.extension == "mp3" }
                    .sortedByDescending { it.lastModified() }
                    .toList()
                _viewState.value = _viewState.value.copy(savedFiles = allFiles.toImmutableList())
            } else {
                _viewState.value = _viewState.value.copy(savedFiles = persistentListOf())
            }
        }
    }

    private fun deleteFile(file: File) {
        viewModelScope.launch {
            if (file.exists()) {
                if (file.delete()) {
                    showToast("File deleted successfully")
                    refreshFiles()
                    if (_viewState.value.currentlyPlayingFile == file) {
                        stopPlayback()
                    }
                } else {
                    showToast("Failed to delete file")
                }
            }
        }
    }

    private fun playFile(file: File) {
        viewModelScope.launch {
            stopPlayback()
            try {
                mediaPlayer = MediaPlayer().apply {
                    setDataSource(file.absolutePath)
                    prepare()
                    start()
                    _viewState.value = _viewState.value.copy(
                        currentlyPlayingFile = file,
                        playbackDuration = duration.toLong(),
                        isPaused = false
                    )
                    startProgressTracker()
                    setOnCompletionListener {
                        stopPlayback()
                    }
                }
            } catch (e: Exception) {
                Log.e("FileListViewModel", "Error playing file", e)
                showToast("Error playing file")
            }
        }
    }

    private fun pausePlayback() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                _viewState.value = _viewState.value.copy(isPaused = true)
                stopProgressTracker()
            }
        }
    }

    private fun resumePlayback() {
        mediaPlayer?.let {
            if (!it.isPlaying) {
                it.start()
                _viewState.value = _viewState.value.copy(isPaused = false)
                startProgressTracker()
            }
        }
    }

    private fun stopPlayback() {
        stopProgressTracker()
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        _viewState.value = _viewState.value.copy(
            currentlyPlayingFile = null,
            isPaused = false,
            playbackPosition = 0L,
            playbackDuration = 0L
        )
    }

    private fun seekTo(position: Long) {
        mediaPlayer?.seekTo(position.toInt())
        _viewState.value = _viewState.value.copy(playbackPosition = position)
    }

    private fun startProgressTracker() {
        progressJob?.cancel()
        progressJob = viewModelScope.launch {
            while (true) {
                mediaPlayer?.let {
                    if (it.isPlaying) {
                        _viewState.value =
                            _viewState.value.copy(playbackPosition = it.currentPosition.toLong())
                    }
                }
                delay(200.milliseconds)
            }
        }
    }

    private fun stopProgressTracker() {
        progressJob?.cancel()
        progressJob = null
    }

    private suspend fun showToast(message: String) {
        effects.emit(FileListEffect.ShowToast(message))
    }

    override fun onCleared() {
        stopPlayback()
    }
}
