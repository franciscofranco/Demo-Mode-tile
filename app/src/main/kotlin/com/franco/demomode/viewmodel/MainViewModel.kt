package com.franco.demomode.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.franco.demomode.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

@ExperimentalTime
class MainViewModel : ViewModel() {

    fun isDumpPermissionUpdates(context: Context) = flow {
        while (true) {
            emit(Utils().isDumpPermissionGranted(context))
            delay(Random.nextLong(1.seconds.inWholeMilliseconds, 2.seconds.inWholeMilliseconds))
        }
    }.asLiveData(Dispatchers.IO)

    fun isWriteSecureSettingsPermissionUpdates(context: Context) = flow {
        while (true) {
            emit(Utils().isWriteSecureSettingsPermissionGranted(context))
            delay(Random.nextLong(1.seconds.inWholeMilliseconds, 2.seconds.inWholeMilliseconds))
        }
    }.asLiveData(Dispatchers.IO)
}