package com.moustafa.foodiemanual.ui.restaurantlist

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.CompoundButton
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.moustafa.foodiemanual.R
import com.moustafa.foodiemanual.base.BaseFragment
import com.moustafa.foodiemanual.ui.misc.AsyncState
import com.moustafa.foodiemanual.utils.ItemDecorationCustomMargins
import com.moustafa.foodiemanual.utils.setExpansionAnimation
import kotlinx.android.synthetic.main.fragment_restaurants_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author moustafasamhoury
 * created on Sunday, 17 Nov, 2019
 */

class RestaurantsListFragment : BaseFragment(R.layout.fragment_restaurants_list) {

    private val restaurantsListViewModel: RestaurantsListViewModel by viewModel()
    private val restaurantsListAdapter: RestaurantsListAdapter =
        RestaurantsListAdapter(onFavoriteClicked = ::onFavoriteClicked)

    private fun onFavoriteClicked(view: View, position: Int) {
        restaurantsListViewModel.toggleFavoriteRestaurant(restaurantsListAdapter.currentList[position])
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        restaurantsListViewModel.stateLiveData.observe(viewLifecycleOwner, Observer {
            handleState(it)
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun setupViews(rootView: View) {
        recyclerViewRestaurantsList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = restaurantsListAdapter
            addItemDecoration(
                ItemDecorationCustomMargins(
                    top = 8, bottom = 8,
                    start = 8, end = 8
                )
            )
            (itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
        }

        chipBestMatch.setOnCheckedChangeListener { _: CompoundButton, _: Boolean ->
            restaurantsListViewModel.applySort(SortOption.BestMatch)
        }
        chipNewest.setOnCheckedChangeListener { _: CompoundButton, _: Boolean ->
            restaurantsListViewModel.applySort(SortOption.Newest)
        }
        chipRatingAverage.setOnCheckedChangeListener { _: CompoundButton, _: Boolean ->
            restaurantsListViewModel.applySort(SortOption.Rating)
        }
        chipDistance.setOnCheckedChangeListener { _: CompoundButton, _: Boolean ->
            restaurantsListViewModel.applySort(SortOption.Distance)
        }
        chipPopularity.setOnCheckedChangeListener { _: CompoundButton, _: Boolean ->
            restaurantsListViewModel.applySort(SortOption.Popularity)
        }
        chipAveragePrice.setOnCheckedChangeListener { _: CompoundButton, _: Boolean ->
            restaurantsListViewModel.applySort(SortOption.Price)
        }
        chipDeliveryCost.setOnCheckedChangeListener { _: CompoundButton, _: Boolean ->
            restaurantsListViewModel.applySort(SortOption.DeliveryCost)
        }
        chipMinimumCost.setOnCheckedChangeListener { _: CompoundButton, _: Boolean ->
            restaurantsListViewModel.applySort(SortOption.MinimumCharge)
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
                showError(loadingRestaurantsList.failed) {
                    restaurantsListViewModel.queriedFetchRestaurants("")
                }
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        val searchItem: MenuItem? = menu.findItem(R.id.action_search)
        setupSearchView(searchItem)
    }

    private fun setupSearchView(searchItem: MenuItem?) {
        val searchView = (searchItem?.actionView as? SearchView)

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                restaurantsListViewModel.queriedFetchRestaurants(query ?: "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                restaurantsListViewModel.queriedFetchRestaurants(newText ?: "")
                return true
            }
        })

        val searchBar = searchItem?.actionView
        if (searchBar != null) {
            searchItem.setExpansionAnimation(
                revealAnimation = {
                    val cx: Double = searchBar.measuredWidth.toDouble()
                    val cy: Double = searchBar.measuredHeight / 2.0

                    val finalRadius = Math.hypot(cx, cy).toFloat()

                    ViewAnimationUtils.createCircularReveal(
                        searchBar,
                        cx.toInt(),
                        cy.toInt(),
                        0f,
                        finalRadius
                    )
                },
                collapseAnimation = {
                    val cx: Double = searchBar.measuredWidth.toDouble()
                    val cy: Double = searchBar.measuredHeight / 2.0

                    val finalRadius = Math.hypot(cx, cy).toFloat()

                    ViewAnimationUtils.createCircularReveal(
                        searchBar,
                        cx.toInt(),
                        cy.toInt(),
                        finalRadius,
                        0f
                    )
                },
                searchBarBackground = R.drawable.rectangular_background_rounded_corners,
                searchCloseButtonColor = Color.BLACK
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> {
                item.expandActionView()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
