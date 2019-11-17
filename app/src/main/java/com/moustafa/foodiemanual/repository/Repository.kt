package com.moustafa.foodiemanual.repository

import com.moustafa.foodiemanual.models.RestaurantView

/**
 * @author moustafasamhoury
 * created on Monday, 04 Nov, 2019
 */

interface Repository {
    suspend fun fetchRestaurantsList(
        searchQuery: String,
        onError: (Exception) -> Unit
    ): List<RestaurantView>
}
