package com.butul0ve.urbanslang.utils

import android.app.Activity
import hotchemi.android.rate.AppRate

class AppRateImpl {

    fun init(activity: Activity) {
        AppRate.with(activity.applicationContext)
            .setInstallDays(1)
            .setLaunchTimes(5)
            .setShowLaterButton(true)
            .monitor()

        AppRate.showRateDialogIfMeetsConditions(activity)
    }
}