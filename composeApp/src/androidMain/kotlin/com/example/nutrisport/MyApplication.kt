package com.example.nutrisport

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.initialize
import com.nutrisport.di.initializeKoin
import org.koin.android.ext.koin.androidContext

// Essa classe herda da classe Application do Android.
// Isso significa que ela é executada antes de qualquer Activity.

class MyApplication : Application(){
    override fun onCreate() {
        super.onCreate()

        initializeKoin(
            config = {
                androidContext(this@MyApplication)
            }
        )

        Firebase.initialize(context = this)
    }
}