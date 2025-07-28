package com.nutrisport.shared.util

actual fun getScreenWitdh(): Float {
    return android.content.res.Resources.getSystem().displayMetrics.widthPixels /
            android.content.res.Resources.getSystem().displayMetrics.density
}