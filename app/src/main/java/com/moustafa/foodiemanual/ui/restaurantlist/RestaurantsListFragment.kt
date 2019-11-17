package com.moustafa.foodiemanual.ui.restaurantlist

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.moustafa.foodiemanual.R
import com.moustafa.foodiemanual.base.BaseFragment
import com.moustafa.foodiemanual.ui.misc.AsyncState
import com.moustafa.foodiemanual.utils.ItemDecorationCustomMargins
import kotlinx.android.synthetic.main.fragment_restaurants_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author moustafasamhoury
 * created on Sunday, 17 Nov, 2019
 */

class RestaurantsListFragment : BaseFragment(R.layout.fragment_restaurants_list) {

    private val restaurantsListViewModel: RestaurantsListViewModel by viewModel()
    private val restaurantsListAdapter: RestaurantsListAdapter = RestaurantsListAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        restaurantsListViewModel.stateLiveData.observe(viewLifecycleOwner, Observer {
            handleState(it)
        })
    }

    override fun setupViews(rootView: View) {
        recyclerViewRestaurantsList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = restaurantsListAdapter
            addItemDecoration(
                ItemDecorationCustomMargins(
                    top = 8, bottom = 8,
                    start = 16, end = 16
                )
            )
            addItemDecoration(
                DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
            )
        }
    }

    private fun handleState(restaurantsListState: RestaurantsListState) {
        when (val loadingRestaurantsList = restaurantsListState.loadRestaurantsList) {
            AsyncState.Init -> {
                showLoading(shouldShow = false)
            }
            AsyncState.Loading -> {
                showLoading(shouldShow = true)
            }
            is AsyncState.Loaded -> {
                showLoading(shouldShow = false)
                restaurantsListAdapter.submitList(loadingRestaurantsList.result)
            }
            is AsyncState.Failed -> {
                showLoading(shouldShow = false)
            }
        }
    }

    private fun showLoading(shouldShow: Boolean) {
        if (shouldShow) {
            progressBarLoadingRestaurants.show()
        } else {
            progressBarLoadingRestaurants.hide()
        }
    }

    private fun showError(throwable: Throwable, action: (() -> Any)? = null) {
        val snackBar = Snackbar.make(
            constraintLayoutRoot,
            throwable.message ?: getString(R.string.generic_error_unknown),
            Snackbar.LENGTH_SHORT
        )
        if (action != null) {
            snackBar.setAction(R.string.action_retry) {
                action.invoke()
            }
            snackBar.duration = Snackbar.LENGTH_INDEFINITE
        }
        snackBar.show()
    }
}
