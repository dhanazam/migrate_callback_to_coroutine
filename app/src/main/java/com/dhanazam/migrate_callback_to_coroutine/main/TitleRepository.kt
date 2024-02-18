package com.dhanazam.migrate_callback_to_coroutine.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.map

class TitleRepository(val titleDao: TitleDao) {

    val title: LiveData<String?> = titleDao.titleLiveData.map { it?.title }


}