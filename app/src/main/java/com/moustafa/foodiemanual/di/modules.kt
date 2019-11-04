package com.moustafa.foodiemanual.di

import com.moustafa.foodiemanual.models.Restaurant
import com.moustafa.foodiemanual.repository.Repository
import com.moustafa.foodiemanual.repository.RepositoryImpl
import com.moustafa.foodiemanual.repository.network.DataLocation
import com.moustafa.foodiemanual.repository.network.FoodieDataSource
import com.moustafa.foodiemanual.repository.network.NetworkLayerFactory
import com.moustafa.foodiemanual.repository.network.RestaurantsJsonString
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * @author moustafasamhoury
 * created on Monday, 04 Nov, 2019
 */
val repositoryModule: Module = module {

    factory<DataLocation> { DataLocation(string = "restaurants_json.json") }

    single<RestaurantsJsonString> {
        NetworkLayerFactory.loadData(
            context = androidContext(),
            dataLocation = get()
        )
    }

    single<Moshi> { NetworkLayerFactory.createMoshiInstance() }

    single<JsonAdapter<List<Restaurant>>> {
        NetworkLayerFactory.makeRestaurantJsonAdapter(moshi = get())
    }

    single<FoodieDataSource> {
        NetworkLayerFactory.makeDataSource(restaurantsJsonString = get(), adapter = get())
    }

    single<Repository> { RepositoryImpl(get()) }
}
val viewModelsModule: Module = module {

}