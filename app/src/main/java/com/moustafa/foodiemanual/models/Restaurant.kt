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
)

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
    val averageProductPrice: Int? = null,
    @field:Json(name = "deliveryCosts")
    val deliveryCosts: Int? = null,
    @field:Json(name = "minCost")
    val minCost: Int? = null
)
