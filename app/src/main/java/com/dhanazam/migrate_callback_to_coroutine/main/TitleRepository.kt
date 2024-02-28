package com.dhanazam.migrate_callback_to_coroutine.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.dhanazam.migrate_callback_to_coroutine.util.BACKGROUND
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class TitleRepository(val network: MainNetwork, val titleDao: TitleDao) {

    val title: LiveData<String?> = titleDao.titleLiveData.map { it?.title }

//    fun refreshTitleWithCallbacks(titleRefreshCallback: TitleRefreshCallback) {
//        BACKGROUND.submit {
//            try {
//                val result = network.fetchNextTilte().execute()
//                if (result.isSuccessful) {
//                    titleDao.insertTitle(Title(result.body()!!))
//                    titleRefreshCallback.onCompleted()
//                } else {
//                    titleRefreshCallback.onError(
//                        TitleRefreshError("Unable to refresh title", null)
//                    )
//                }
//            } catch (cause: Throwable) {
//                titleRefreshCallback.onError(
//                    TitleRefreshError("Unable to refresh title", cause)
//                )
//            }
//        }
//    }

//    suspend fun refreshTitle() {
//        // interact with *blocling* network and IO calls from a coroutine
//        withContext(Dispatchers.IO) {
//            val result = try {
//                network.fetchNextTilte().execute()
//            } catch (cause: Throwable) {
//                throw TitleRefreshError("Unable to refresh title", cause)
//            }
//
//            if (result.isSuccessful) {
//                titleDao.insertTitle(Title(result.body()!!))
//            } else {
//                throw TitleRefreshError("Unable to refresh title", null)
//            }
//        }
//    }

    suspend fun refreshTitle() {
        try {
            // Make network request using a blocking call
            val result = network.fetchNextTilte()
            titleDao.insertTitle(Title(result))
        } catch (cause: Throwable) {
            // If anything throws an exception, inform the caller
            throw  TitleRefreshError("Unable to refresh title", cause)
        }
    }
}

class TitleRefreshError(message: String, cause: Throwable?) : Throwable(message, cause)

interface TitleRefreshCallback {
    fun onCompleted()

    fun onError(cause: Throwable)
}

