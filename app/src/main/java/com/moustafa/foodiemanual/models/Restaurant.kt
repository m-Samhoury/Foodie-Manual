package com.moustafa.foodiemanual.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @author moustafasamhoury
 * created on Monday, 04 Nov, 2019
 */
@JsonClass(generateAdapter = true)
data class Restaurant(
    @field:Json(name = "name")
    val name: String? = null,
    @field:Json(name = "status")
    val status: String? = null,
    @field:Json(name = "sortingValues")
    val sortingValues: SortingValues? = null
) {
    val isOpened: Boolean
        get() = status?.toLowerCase() == "open"
    val isOrderAhead: Boolean
        get() = status?.toLowerCase() == "order ahead"
    val isClosed: Boolean
        get() = status?.toLowerCase() == "closed"

    val openStatusSortValue =
        when {
            isOpened -> RESTAURANT_STATUS_OPENED
            isOrderAhead -> RESTAURANT_STATUS_ORDER_AHEAD
            else -> RESTAURANT_STATUS_CLOSED
        }

    companion object {
        const val RESTAURANT_STATUS_OPENED = 0
        const val RESTAURANT_STATUS_ORDER_AHEAD = 1
        const val RESTAURANT_STATUS_CLOSED = 2
    }

}

@JsonClass(generateAdapter = true)
data class SortingValues(
    @field:Json(name = "bestMatch")
    val bestMatch: Double? = null,
    @field:Json(name = "newest")
    val newest: Double? = null,
    @field:Json(name = "ratingAverage")
    val ratingAverage: Double? = null,
    @field:Json(name = "distance")
    val distance: Double? = null,
    @field:Json(name = "popularity")
    val popularity: Double? = null,
    @field:Json(name = "averageProductPrice")
    val averageProductPrice: Double? = null,
    @field:Json(name = "deliveryCosts")
    val deliveryCosts: Double? = null,
    @field:Json(name = "minCost")
    val minCost: Double? = null
)

data class RestaurantView(
    val restaurant: Restaurant,
    var isFavorite: Boolean,
    val sortingValue: Double
)

@JsonClass(generateAdapter = true)
data class RestaurantResponse(
    @field:Json(name = "restaurants")
    val restaurantsList: List<Restaurant>
)
