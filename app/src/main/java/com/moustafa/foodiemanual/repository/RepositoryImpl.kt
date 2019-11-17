package com.moustafa.foodiemanual.repository

import com.moustafa.foodiemanual.models.RestaurantView
import com.moustafa.foodiemanual.repository.network.FoodieDataSource

/**
 * @author moustafasamhoury
 * created on Monday, 04 Nov, 2019
 */

class RepositoryImpl(private val foodieDataSource: FoodieDataSource) : Repository {

    override suspend fun fetchRestaurantsList(
        searchQuery: String,
        onError: (Exception) -> Unit
    ): List<RestaurantView> {
        return foodieDataSource.getRestaurantsList()
            .filter {
                it.name?.contains(searchQuery, ignoreCase = true) == true
            }
            .map { RestaurantView(restaurant = it, isFavorite = false) }
    }

}
