package com.moustafa.foodiemanual.di

import androidx.room.Room
import com.moustafa.foodiemanual.R
import com.moustafa.foodiemanual.models.RestaurantResponse
import com.moustafa.foodiemanual.repository.Repository
import com.moustafa.foodiemanual.repository.RepositoryImpl
import com.moustafa.foodiemanual.repository.databasesource.AppDatabase
import com.moustafa.foodiemanual.repository.filesource.DataSourceLocation
import com.moustafa.foodiemanual.repository.filesource.FoodieDataSource
import com.moustafa.foodiemanual.repository.filesource.NetworkLayerFactory
import com.moustafa.foodiemanual.repository.filesource.RestaurantsJsonString
import com.moustafa.foodiemanual.ui.restaurantlist.RestaurantsListViewModel
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * @author moustafasamhoury
 * created on Monday, 04 Nov, 2019
 */
val repositoryModule: Module = module {

    factory<DataSourceLocation> { DataSourceLocation(fileResourceId = R.raw.restaurants_json) }

    single<RestaurantsJsonString> {
        NetworkLayerFactory.loadData(
            context = androidContext(),
            dataSourceLocation = get()
        )
    }

    single<Moshi> { NetworkLayerFactory.createMoshiInstance() }

    single<JsonAdapter<RestaurantResponse>> {
        NetworkLayerFactory.makeRestaurantJsonAdapter(moshi = get())
    }

    single<FoodieDataSource> {
        NetworkLayerFactory.makeDataSource(restaurantsJsonString = get(), adapter = get())
    }

    single<AppDatabase> {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java, androidContext().resources.getString(R.string.db_name)
        ).build()
    }

    single<Repository> { RepositoryImpl(foodieDataSource = get(), roomDatabase = get()) }
}
val viewModelsModule: Module = module {
    viewModel { RestaurantsListViewModel(repository = get()) }
}
