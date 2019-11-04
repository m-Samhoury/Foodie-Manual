package com.moustafa.foodiemanual

import android.app.Application
import com.moustafa.foodiemanual.di.repositoryModule
import com.moustafa.foodiemanual.di.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * @author moustafasamhoury
 * created on Monday, 04 Nov, 2019
 */

class FoodieManualApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@FoodieManualApplication)

            if (BuildConfig.DEBUG) {
                androidLogger()
            }
            modules(listOf(repositoryModule, viewModelsModule))
        }
    }
}