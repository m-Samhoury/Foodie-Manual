package com.moustafa.foodiemanual.repository.network

import com.moustafa.foodiemanual.models.Restaurant
import com.moustafa.foodiemanual.models.RestaurantResponse
import com.squareup.moshi.JsonAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * @author moustafasamhoury
 * created on Monday, 04 Nov, 2019
 */

class FoodieDataSource(
    private val restaurantsJsonString: RestaurantsJsonString,
    private val adapter: JsonAdapter<RestaurantResponse>
) {
    private lateinit var restaurantsList: List<Restaurant>

    init {
        GlobalScope.launch(Dispatchers.IO) {
            restaurantsList =
                adapter.fromJson(restaurantsJsonString.string)?.restaurantsList ?: emptyList()
        }
    }

    fun getRestaurantsList() = restaurantsList

}
