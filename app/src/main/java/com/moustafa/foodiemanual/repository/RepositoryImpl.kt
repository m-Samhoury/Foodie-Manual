package com.moustafa.foodiemanual.repository

import com.moustafa.foodiemanual.models.FavoriteRestaurant
import com.moustafa.foodiemanual.models.Restaurant
import com.moustafa.foodiemanual.models.RestaurantView
import com.moustafa.foodiemanual.repository.databasesource.AppDatabase
import com.moustafa.foodiemanual.repository.filesource.FoodieDataSource
import com.moustafa.foodiemanual.ui.restaurantlist.SortOption

/**
 * @author moustafasamhoury
 * created on Monday, 04 Nov, 2019
 */

class RepositoryImpl(
    private val foodieDataSource: FoodieDataSource,
    private val roomDatabase: AppDatabase
) : Repository {

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

        val favoriteRestaurants = roomDatabase.favoriteRestaurantDao().getAllFavoriteRestaurants()

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

    override suspend fun addRestaurantToFavorites(restaurantName: String): FavoriteRestaurant {
        val favoriteRestaurant = FavoriteRestaurant(restaurantName)
        roomDatabase.favoriteRestaurantDao()
            .insertRestaurantToFavorites(favoriteRestaurant)
        return favoriteRestaurant
    }

    override suspend fun removeRestaurantFromFavorites(restaurantName: String): FavoriteRestaurant {
        val favoriteRestaurant = FavoriteRestaurant(restaurantName)
        roomDatabase.favoriteRestaurantDao()
            .removeRestaurantFromFavorites(FavoriteRestaurant(restaurantName))
        return favoriteRestaurant
    }
}
