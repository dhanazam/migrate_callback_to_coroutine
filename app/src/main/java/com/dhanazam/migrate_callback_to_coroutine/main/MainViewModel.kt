package com.dhanazam.migrate_callback_to_coroutine.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhanazam.migrate_callback_to_coroutine.util.BACKGROUND
import com.dhanazam.migrate_callback_to_coroutine.util.singleArgViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel(private val repository: TitleRepository): ViewModel(){

    companion object {
        val Factory = singleArgViewModelFactory(::MainViewModel)
    }

    private val _snackbar = MutableLiveData<String?>()

    val snackbar: LiveData<String?>
        get() = _snackbar

    val title = repository.title

    private val _spinner = MutableLiveData<Boolean>(false)

    val spinner: LiveData<Boolean>
        get() = _spinner

    private var tapCount = 0

    private val _taps = MutableLiveData<String>("$tapCount taps")

    val taps: LiveData<String>
        get() = _taps

    fun onMainViewClicked() {
        refreshTitle()
        updateTaps()
    }

//    private fun updateTaps() {
//        tapCount++
//        BACKGROUND.submit {
//            Thread.sleep(3_000)
//            _taps.postValue("$tapCount taps")
//        }
//    }

    fun updateTaps() {
        viewModelScope.launch {
            tapCount++
            delay(3_000)
            _taps.postValue("$tapCount taps")
        }
    }

    fun onSnackbarShown() {
        _snackbar.value = null
    }

//    private fun refreshTitle() {
//        _spinner.value = true
//        repository.refreshTitleWithCallbacks(object: TitleRefreshCallback {
//            override fun onCompleted() {
//                _spinner.postValue(false)
//            }
//
//            override fun onError(cause: Throwable) {
//                _snackbar.postValue(cause.message)
//                _spinner.postValue(false)
//            }
//        })
//    }

    fun refreshTitle() {
        viewModelScope.launch {
            try {
                _spinner.value = true
                repository.refreshTitle()
            } catch (error: TitleRefreshError) {
                _snackbar.value = error.message
            } finally {
                _spinner.value = false
            }
        }
    }
}