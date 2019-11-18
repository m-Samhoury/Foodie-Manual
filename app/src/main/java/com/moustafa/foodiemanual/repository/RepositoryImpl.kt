package com.moustafa.foodiemanual.repository

import com.moustafa.foodiemanual.models.Restaurant
import com.moustafa.foodiemanual.models.RestaurantView
import com.moustafa.foodiemanual.repository.network.FoodieDataSource
import com.moustafa.foodiemanual.ui.restaurantlist.SortOption

/**
 * @author moustafasamhoury
 * created on Monday, 04 Nov, 2019
 */

class RepositoryImpl(private val foodieDataSource: FoodieDataSource) : Repository {

    override suspend fun fetchRestaurantsList(
        searchQuery: String,
        sortOption: SortOption,
        onError: (Exception) -> Unit
    ): List<RestaurantView> {

        val openedRestaurants =
            foodieDataSource.getRestaurantsList()
                .filter { it.openStatusSortValue == Restaurant.RESTAURANT_STATUS_OPENED }

        val orderAheadRestaurants =
            foodieDataSource.getRestaurantsList()
                .filter { it.openStatusSortValue == Restaurant.RESTAURANT_STATUS_ORDER_AHEAD }

        val closedRestaurants =
            foodieDataSource.getRestaurantsList()
                .filter { it.openStatusSortValue == Restaurant.RESTAURANT_STATUS_CLOSED }


        val openedRestaurantsViewList =
            applyRestaurantFiltersAndSortAndTransform(openedRestaurants, searchQuery, sortOption)

        val orderAheadRestaurantsViewList =
            applyRestaurantFiltersAndSortAndTransform(
                orderAheadRestaurants,
                searchQuery,
                sortOption
            )

        val closedRestaurantsViewList =
            applyRestaurantFiltersAndSortAndTransform(closedRestaurants, searchQuery, sortOption)

        return openedRestaurantsViewList
            .plus(orderAheadRestaurantsViewList)
            .plus(closedRestaurantsViewList)
    }

    private fun applyRestaurantFiltersAndSortAndTransform(
        list: List<Restaurant>,
        searchQuery: String,
        sortOption: SortOption
    ) =
        list.filter {
            it.name?.contains(searchQuery, ignoreCase = true) == true
        }.sortedWith(compareBy {
            when (sortOption) {
                SortOption.BestMatch -> it.sortingValues?.bestMatch ?: 0.0
                SortOption.Newest -> it.sortingValues?.newest ?: 0.0
                SortOption.Rating -> it.sortingValues?.ratingAverage ?: 0.0
                SortOption.Distance -> it.sortingValues?.distance ?: 0.0
                SortOption.Popularity -> it.sortingValues?.popularity ?: 0.0
                SortOption.Price -> it.sortingValues?.averageProductPrice ?: 0.0
                SortOption.DeliveryCost -> it.sortingValues?.deliveryCosts ?: 0.0
                SortOption.MinimumCharge -> it.sortingValues?.minCost ?: 0.0
            }
        }).map {
            val sortingValue: Double = when (sortOption) {
                SortOption.BestMatch -> it.sortingValues?.bestMatch ?: 0.0
                SortOption.Newest -> it.sortingValues?.newest ?: 0.0
                SortOption.Rating -> it.sortingValues?.ratingAverage ?: 0.0
                SortOption.Distance -> it.sortingValues?.distance ?: 0.0
                SortOption.Popularity -> it.sortingValues?.popularity ?: 0.0
                SortOption.Price -> it.sortingValues?.averageProductPrice ?: 0.0
                SortOption.DeliveryCost -> it.sortingValues?.deliveryCosts ?: 0.0
                SortOption.MinimumCharge -> it.sortingValues?.minCost ?: 0.0
            }
            RestaurantView(restaurant = it, isFavorite = false, sortingValue = sortingValue)
        }
}
