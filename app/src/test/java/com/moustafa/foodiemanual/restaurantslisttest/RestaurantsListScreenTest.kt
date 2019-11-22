package com.moustafa.countrypicker.countrieslisttest

import android.accounts.NetworkErrorException
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.moustafa.foodiemanual.TestingRepository
import com.moustafa.foodiemanual.repository.Repository
import com.moustafa.foodiemanual.ui.misc.AsyncState
import com.moustafa.foodiemanual.ui.restaurantlist.RestaurantsListViewModel
import com.moustafa.foodiemanual.utils.CoroutineRule
import com.moustafa.foodiemanual.utils.LiveDataTestUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test

/**
 * Some tests to make sure the RecipeViewModel is emitting the right states for the UI
 * to be then rendered
 */
@UseExperimental(ExperimentalCoroutinesApi::class)
class RestaurantsListScreenTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutineRule()

    private val testingRepository: Repository = TestingRepository()

    private lateinit var restaurantsListViewModel: RestaurantsListViewModel

    private val currentViewModelState
        get() = LiveDataTestUtil.getValue(restaurantsListViewModel.stateLiveData)

    @Test
    fun `get restaurants list triggers loading state`() = coroutinesTestRule.runBlockingTest {
        coroutinesTestRule.pauseDispatcher()
        restaurantsListViewModel = RestaurantsListViewModel(testingRepository)

        assertThat(currentViewModelState.loadRestaurantsList is AsyncState.Loading).isTrue()
        coroutinesTestRule.resumeDispatcher()
    }

    @Test
    fun `get restaurants list and return success`() = coroutinesTestRule.runBlockingTest {
        coroutinesTestRule.pauseDispatcher()
        restaurantsListViewModel = RestaurantsListViewModel(testingRepository)

        assertThat(currentViewModelState.loadRestaurantsList is AsyncState.Loading).isTrue()

        coroutinesTestRule.resumeDispatcher()

        assertThat(currentViewModelState.loadRestaurantsList is AsyncState.Loaded).isTrue()
        assertThat((currentViewModelState.loadRestaurantsList as AsyncState.Loaded).result)
            .hasSize(5)
    }

    @Test
    fun `get restaurants list and return failure`() = coroutinesTestRule.runBlockingTest {

        coroutinesTestRule.pauseDispatcher()
        restaurantsListViewModel = RestaurantsListViewModel(testingRepository)

        assertThat(currentViewModelState.loadRestaurantsList is AsyncState.Loading).isTrue()

        coroutinesTestRule.resumeDispatcher()

        assertThat(currentViewModelState.loadRestaurantsList is AsyncState.Failed).isTrue()
        assertThat((currentViewModelState.loadRestaurantsList as AsyncState.Failed).failed)
            .isInstanceOf(NetworkErrorException::class.java)
    }

    @Test
    fun `filter restaurants list and return result`() = coroutinesTestRule.runBlockingTest {
        restaurantsListViewModel = RestaurantsListViewModel(testingRepository)
        restaurantsListViewModel.queriedFetchRestaurants("res 1")

        coroutinesTestRule.pauseDispatcher()
        delay(400)  //delay the same debounce timeOut

        assertThat((currentViewModelState.loadRestaurantsList as AsyncState.Loaded).result)
            .hasSize(1)
        assertThat((currentViewModelState.loadRestaurantsList as AsyncState.Loaded).result[0].restaurant.name)
            .isEqualTo("res 1")

        coroutinesTestRule.resumeDispatcher()
    }

    @Test
    fun `filter restaurants list and return empty`() = coroutinesTestRule.runBlockingTest {
        restaurantsListViewModel = RestaurantsListViewModel(testingRepository)
        restaurantsListViewModel.queriedFetchRestaurants("Lebanon")

        coroutinesTestRule.pauseDispatcher()
        delay(400)  //delay the same debounce timeOut

        assertThat((currentViewModelState.loadRestaurantsList as AsyncState.Loaded).result)
            .hasSize(0)
        coroutinesTestRule.resumeDispatcher()
    }
}
