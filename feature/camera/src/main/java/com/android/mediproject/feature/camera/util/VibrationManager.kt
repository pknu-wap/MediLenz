package com.android.mediproject.feature.camera.util

import android.content.Context
import android.os.CombinedVibration
import android.os.VibrationEffect
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VibrationManager @Inject constructor(@ApplicationContext context: Context) {
    private val vibrator = context.getSystemService(
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) Context.VIBRATOR_MANAGER_SERVICE
        else Context.VIBRATOR_SERVICE,
    )

    fun vibrate(millis: Long, amp: Int = 100) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            val vibrationEffect = VibrationEffect.createOneShot(millis, amp)
            val combinedVibration = CombinedVibration.createParallel(vibrationEffect)
            (vibrator as android.os.VibratorManager).vibrate(combinedVibration)
        } else {
            (vibrator as android.os.Vibrator).vibrate(VibrationEffect.createOneShot(millis, amp))
        }
    }
}
