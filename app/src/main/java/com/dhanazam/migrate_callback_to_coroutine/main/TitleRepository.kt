package com.dhanazam.migrate_callback_to_coroutine.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.dhanazam.migrate_callback_to_coroutine.util.BACKGROUND

class TitleRepository(val network: MainNetwork, val titleDao: TitleDao) {

    val title: LiveData<String?> = titleDao.titleLiveData.map { it?.title }

    fun refreshTitleWithCallbacks(titleRefreshCallback: TitleRefreshCallback) {
        BACKGROUND.submit {
            try {
                val result = network.fetchNextTilte().execute()
                if (result.isSuccessful) {
                    titleDao.inserTitle(Title(result.body()!!))
                    titleRefreshCallback.onCompleted()
                } else {
                    titleRefreshCallback.onError(
                        TitleRefreshError("Unable to refresh title", null)
                    )
                }
            } catch (cause: Throwable) {
                titleRefreshCallback.onError(
                    TitleRefreshError("Unable to refresh title", cause)
                )
            }
        }
    }
}

class TitleRefreshError(message: String, cause: Throwable?) : Throwable(message, cause)

interface TitleRefreshCallback {
    fun onCompleted()

    fun onError(cause: Throwable)
}

