package com.moustafa.foodiemanual.repository.network

import android.content.Context
import com.moustafa.foodiemanual.models.Restaurant
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types


/**
 * This helper class provide methods to setup retrofit in the application
 *
 * @author moustafasamhoury
 * created on Monday, 04 Nov, 2019
 */

object NetworkLayerFactory {

    fun createMoshiInstance() = Moshi.Builder()
        .build()
        .apply {

        }

    fun makeDataSource(
        restaurantsJsonString: RestaurantsJsonString,
        adapter: JsonAdapter<List<Restaurant>>
    ): FoodieDataSource {
        return FoodieDataSource(restaurantsJsonString, adapter)
    }

    fun makeRestaurantJsonAdapter(moshi: Moshi): JsonAdapter<List<Restaurant>> {
        val listType = Types.newParameterizedType(List::class.java, Restaurant::class.java)
        return moshi.adapter(listType)
    }

    fun loadData(context: Context,dataLocation: DataLocation): RestaurantsJsonString {
        val file = dataLocation.string
        return RestaurantsJsonString(context.assets.open(file).bufferedReader().use { it.readText() })
    }

}

inline class RestaurantsJsonString(val string: String)
inline class DataLocation(val string: String)