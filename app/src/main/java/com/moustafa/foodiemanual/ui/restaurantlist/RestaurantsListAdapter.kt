package com.moustafa.foodiemanual.ui.restaurantlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moustafa.foodiemanual.R
import com.moustafa.foodiemanual.models.RestaurantView
import com.moustafa.foodiemanual.utils.formatted
import kotlinx.android.synthetic.main.item_restaurant_list.view.*

/**
 * @author moustafasamhoury
 * created on Tuesday, 22 Oct, 2019
 */

class RestaurantsListAdapter(
    private val onRowClicked: ((View, Int) -> Any)? = null,
    private val onFavoriteClicked: ((View, Int) -> Any)? = null
) :
    ListAdapter<RestaurantView, RestaurantsListAdapter.ArticleViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object :
            DiffUtil.ItemCallback<RestaurantView>() {
            override fun areItemsTheSame(
                oldItem: RestaurantView,
                newItem: RestaurantView
            ): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: RestaurantView,
                newItem: RestaurantView
            ): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder =
        ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_restaurant_list, parent, false
            ), onRowClicked, onFavoriteClicked
        )

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) =
        holder.bind(getItem(position))

    class ArticleViewHolder(
        view: View,
        private val onRowClicked: ((View, Int) -> Any)? = null,
        private val onFavoriteClicked: ((View, Int) -> Any)? = null
    ) : RecyclerView.ViewHolder(view) {

        init {
            itemView.setOnClickListener {
                onRowClicked?.invoke(it, adapterPosition)
            }
            itemView.buttonFavoriteRestaurant.setOnClickListener {
                onFavoriteClicked?.invoke(it, adapterPosition)
            }
        }

        fun bind(item: RestaurantView) {
            itemView.textViewRestaurantName.text = item.restaurant.name
            itemView.textViewRestaurantClosed.text = item.restaurant.status
            itemView.textViewRestaurantSortValue.text = item.sortingValue.formatted()

            val headerColor = when {
                item.restaurant.isClosed -> {
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.restaurantClosed
                    )
                }
                item.restaurant.isOpened -> {
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.restaurantOpened
                    )
                }
                item.restaurant.isOrderAhead -> {
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.restaurantOrderAhead
                    )
                }
                else -> {
                    ContextCompat.getColor(itemView.context, R.color.restaurantOrderAhead)
                }
            }
            itemView.viewRestaurantHeaderColor.setBackgroundColor(headerColor)

        }
    }
}
