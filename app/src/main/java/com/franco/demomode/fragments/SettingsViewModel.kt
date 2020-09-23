package com.franco.demomode.fragments

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.franco.demomode.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlin.random.Random
import kotlin.time.ExperimentalTime
import kotlin.time.seconds

@ExperimentalTime
class SettingsViewModel : ViewModel() {

    fun isDumpPermissionUpdates(context: Context) = flow {
        while (true) {
            emit(Utils().isDumpPermissionGranted(context))
            delay(Random.nextLong(1.seconds.toLongMilliseconds(),
                    2.seconds.toLongMilliseconds()))
        }
    }.asLiveData(Dispatchers.IO)

    fun isWriteSecureSettingsPermissionUpdates(context: Context) = flow {
        while (true) {
            emit(Utils().isWriteSecureSettingsPermissionGranted(context))
            delay(Random.nextLong(1.seconds.toLongMilliseconds(),
                    2.seconds.toLongMilliseconds()))
        }
    }.asLiveData(Dispatchers.IO)
}