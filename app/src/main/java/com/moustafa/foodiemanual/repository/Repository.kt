package com.moustafa.foodiemanual.repository

import com.moustafa.foodiemanual.models.FavoriteRestaurant
import com.moustafa.foodiemanual.models.RestaurantView
import com.moustafa.foodiemanual.ui.restaurantlist.SortOption

/**
 * @author moustafasamhoury
 * created on Monday, 04 Nov, 2019
 */

interface Repository {
    suspend fun fetchRestaurantsList(
        searchQuery: String,
        sortOption: SortOption,
        onError: (Exception) -> Unit
    ): List<RestaurantView>

    suspend fun addRestaurantToFavorites(restaurantName: String): FavoriteRestaurant
    suspend fun removeRestaurantFromFavorites(restaurantName: String): FavoriteRestaurant
}
