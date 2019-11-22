package com.moustafa.foodiemanual.repository.filesource

import android.content.Context
import com.moustafa.foodiemanual.models.RestaurantResponse
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi


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
            adapter<RestaurantResponse>(RestaurantResponse::class.java)
        }

    fun makeDataSource(
        restaurantsJsonString: RestaurantsJsonString,
        adapter: JsonAdapter<RestaurantResponse>
    ): FoodieDataSource {
        return FoodieDataSource(restaurantsJsonString, adapter)
    }

    fun makeRestaurantJsonAdapter(moshi: Moshi): JsonAdapter<RestaurantResponse> {
        return moshi.adapter(RestaurantResponse::class.java)
    }

    fun loadData(context: Context, dataSourceLocation: DataSourceLocation): RestaurantsJsonString {
        val fileResourceId = dataSourceLocation.fileResourceId
        return RestaurantsJsonString(
            context
                .resources
                .openRawResource(fileResourceId)
                .bufferedReader()
                .use { it.readText() }
        )
    }

}

inline class RestaurantsJsonString(val string: String)
inline class DataSourceLocation(val fileResourceId: Int)
