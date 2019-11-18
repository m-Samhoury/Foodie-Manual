package com.moustafa.foodiemanual.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * @param start start padding value in dip
 * @param top top padding value in dip
 * @param end end padding value in dip
 * @param bottom bottom padding value in dip
 *
 *<p>
 * Created Padding around the Recyclerview row
 *
 * <b>Values passed will be considered as dip unit</b>
 * </p>
 */
class ItemDecorationCustomPaddings(
    private val start: Int = 0,
    private val top: Int = 0,
    private val end: Int = 0,
    private val bottom: Int = 0,
    private val predicateToApplyPaddings: (ItemDecorationCustomPaddings
    .(view: View, parent: RecyclerView, state: RecyclerView.State) -> Boolean) =
        { _, _, _ -> true }
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        if (predicateToApplyPaddings(view, parent, state)) {
            if (isLeftToRight()) {
                view.setPadding(
                    view.context.dip(start),
                    view.context.dip(top),
                    view.context.dip(end),
                    view.context.dip(bottom)
                )
            } else {
                view.setPadding(
                    view.context.dip(end),
                    view.context.dip(top),
                    view.context.dip(start),
                    view.context.dip(bottom)
                )
            }
        }
    }
}
