package com.moustafa.foodiemanual

import com.moustafa.foodiemanual.models.FavoriteRestaurant
import com.moustafa.foodiemanual.models.Restaurant
import com.moustafa.foodiemanual.models.RestaurantView
import com.moustafa.foodiemanual.repository.Repository
import com.moustafa.foodiemanual.ui.restaurantlist.SortOption

/**
 * @author moustafasamhoury
 * created on Friday, 22 Nov, 2019
 */
class TestingRepository : Repository {
    override suspend fun fetchRestaurantsList(
        searchQuery: String,
        sortOption: SortOption,
        onError: (Exception) -> Unit
    ): List<RestaurantView> {
        val restaurantsList: List<Restaurant> = listOf(
            Restaurant("res 1"),
            Restaurant("res 2"),
            Restaurant("res 3"),
            Restaurant("res 4"),
            Restaurant("res 5")
        )
        val openedRestaurants =
            restaurantsList
                .filter { it.openStatusSortValue == Restaurant.RESTAURANT_STATUS_OPENED }

        val orderAheadRestaurants =
            restaurantsList
                .filter { it.openStatusSortValue == Restaurant.RESTAURANT_STATUS_ORDER_AHEAD }

        val closedRestaurants =
            restaurantsList
                .filter { it.openStatusSortValue == Restaurant.RESTAURANT_STATUS_CLOSED }

        val favoriteRestaurants = emptyList<FavoriteRestaurant>()

        val openedRestaurantsViewList =
            applyRestaurantFiltersAndSortAndTransform(
                openedRestaurants,
                searchQuery,
                sortOption,
                favoriteRestaurants
            ).groupBy { it.isFavorite }

        val orderAheadRestaurantsViewList =
            applyRestaurantFiltersAndSortAndTransform(
                orderAheadRestaurants,
                searchQuery,
                sortOption,
                favoriteRestaurants
            ).groupBy { it.isFavorite }

        val closedRestaurantsViewList =
            applyRestaurantFiltersAndSortAndTransform(
                closedRestaurants,
                searchQuery,
                sortOption,
                favoriteRestaurants
            ).groupBy { it.isFavorite }
        //true to get the favorite part of the list and false to get the unfavorite part of the list
        return (openedRestaurantsViewList[true] ?: emptyList())
            .plus(orderAheadRestaurantsViewList[true] ?: emptyList())
            .plus(closedRestaurantsViewList[true] ?: emptyList())
            .plus(openedRestaurantsViewList[false] ?: emptyList())
            .plus(orderAheadRestaurantsViewList[false] ?: emptyList())
            .plus(closedRestaurantsViewList[false] ?: emptyList())

    }

    override suspend fun addRestaurantToFavorites(restaurantName: String): FavoriteRestaurant {
        TODO()
    }

    override suspend fun removeRestaurantFromFavorites(restaurantName: String): FavoriteRestaurant {
        TODO()
    }

    private fun applyRestaurantFiltersAndSortAndTransform(
        list: List<Restaurant>,
        searchQuery: String,
        sortOption: SortOption,
        favoriteRestaurants: List<FavoriteRestaurant>?
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
            RestaurantView(
                restaurant = it,
                isFavorite = favoriteRestaurants?.any { favoriteRestaurant ->
                    favoriteRestaurant.restaurantName == it.name
                } ?: false,
                sortingValue = sortingValue
            )
        }
}
