package com.moustafa.foodiemanual.ui.restaurantlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moustafa.foodiemanual.models.RestaurantView
import com.moustafa.foodiemanual.repository.Repository
import com.moustafa.foodiemanual.ui.misc.AsyncState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @author moustafasamhoury
 * created on Sunday, 20 Oct, 2019
 */

data class RestaurantsListState(
    val loadRestaurantsList: AsyncState<List<RestaurantView>> = AsyncState.Init
)

class RestaurantsListViewModel(
    private val repository: Repository,
    private val state: RestaurantsListState = RestaurantsListState()
) : ViewModel() {
    private var searchFor = ""
    private var currentSortOption: SortOption = SortOption.BestMatch

    private val _stateLiveData = MutableLiveData<RestaurantsListState>()
    val stateLiveData: LiveData<RestaurantsListState> = _stateLiveData

    init {
        fetchRestaurantsList()
    }

    private fun fetchRestaurantsList() {
        if (state.loadRestaurantsList is AsyncState.Loading) return

        _stateLiveData.value = state.copy(loadRestaurantsList = AsyncState.Loading)

        viewModelScope.launch(Dispatchers.Main) {
            val currentList =
                repository.fetchRestaurantsList(
                    searchQuery = searchFor,
                    sortOption = currentSortOption
                ) { exception: Exception ->
                    _stateLiveData.value =
                        state.copy(loadRestaurantsList = AsyncState.Failed(exception))
                }
            _stateLiveData.value =
                state.copy(loadRestaurantsList = AsyncState.Loaded(currentList))
        }
    }

    fun queriedFetchRestaurants(searchQuery: String) {
        val searchText = searchQuery.trim()
        if (searchText == searchFor) {
            return
        }

        searchFor = searchText
        viewModelScope.launch(Dispatchers.Main) {
            delay(400)  //debounce timeOut
            if (searchQuery != searchFor) {
                return@launch
            }

            fetchRestaurantsList()
        }
    }

    fun applySort(sortOption: SortOption) {
        currentSortOption = sortOption
        fetchRestaurantsList()
    }

    fun toggleFavoriteRestaurant(restaurantView: RestaurantView) {
        viewModelScope.launch(Dispatchers.Main) {
            if (!restaurantView.restaurant.name.isNullOrBlank()) {
                if (!restaurantView.isFavorite) {
                    repository.addRestaurantToFavorites(restaurantView.restaurant.name)
                } else {
                    repository.removeRestaurantFromFavorites(restaurantView.restaurant.name)
                }
                //Refresh all list to reflect the new changes
                fetchRestaurantsList()
            }
        }

    }
}

sealed class SortOption {
    object BestMatch : SortOption()
    object Newest : SortOption()
    object Rating : SortOption()
    object Distance : SortOption()
    object Popularity : SortOption()
    object Price : SortOption()
    object DeliveryCost : SortOption()
    object MinimumCharge : SortOption()
}
