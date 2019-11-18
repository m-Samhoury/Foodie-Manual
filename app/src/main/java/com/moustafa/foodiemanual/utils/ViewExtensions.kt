package com.moustafa.foodiemanual.utils

import android.animation.Animator
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.SearchView
import androidx.core.graphics.drawable.DrawableCompat
import com.moustafa.foodiemanual.R


/**
 * @author moustafasamhoury
 * created on Saturday, 21 Sep, 2019
 */

fun MenuItem.setExpansionAnimation(
    revealAnimation: () -> Animator,
    collapseAnimation: () -> Animator,
    @DrawableRes searchBarBackground: Int,
    @ColorInt searchCloseButtonColor: Int
) {

    val searchCloseButton =
        actionView?.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
    searchCloseButton?.imageTintList = ColorStateList.valueOf(searchCloseButtonColor)

    val searchBarLayoutTemp = actionView?.findViewById<LinearLayout>(R.id.search_bar)
        ?.findViewById<LinearLayout>(R.id.search_bar)

    searchBarLayoutTemp?.setBackgroundResource(searchBarBackground)
    searchBarLayoutTemp?.visibility = View.GONE
    setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
        var shouldCollapse: Boolean = true

        override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
            val searchBarLayout = actionView
            searchBarLayoutTemp?.visibility = View.GONE

            searchBarLayout?.post {
                val revealAnimation = revealAnimation()
                revealAnimation.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {
                    }

                    override fun onAnimationEnd(animation: Animator?) {

                    }

                    override fun onAnimationCancel(animation: Animator?) {
                    }

                    override fun onAnimationStart(animation: Animator?) {
                        searchBarLayoutTemp?.visibility = View.VISIBLE
                    }
                })
                revealAnimation.start()
                shouldCollapse = false
            }
            return true
        }

        override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
            if (shouldCollapse) {
                shouldCollapse = false
                return true
            }
            val searchBarLayout = (item?.actionView as SearchView?)
                ?.findViewById<LinearLayout>(R.id.search_bar)?.parent as View?

            searchBarLayout?.let {
                val collapseAnimation = collapseAnimation()

                collapseAnimation
                    .addListener(object : Animator.AnimatorListener {
                        override fun onAnimationRepeat(animation: Animator?) {

                        }

                        override fun onAnimationEnd(animation: Animator?) {
                            shouldCollapse = true
                            item?.collapseActionView()
                        }

                        override fun onAnimationCancel(animation: Animator?) {

                        }

                        override fun onAnimationStart(animation: Animator?) {

                        }

                    })
                collapseAnimation.start()
            }
            return false
        }

    })
}

fun Drawable.tintCompat(@ColorInt color: Int) {
    var wrappedDrawable = DrawableCompat.wrap(this)
    wrappedDrawable = wrappedDrawable.mutate()
    DrawableCompat.setTint(wrappedDrawable, color)
}

fun Drawable.toBitmap(): Bitmap {
    if (this is BitmapDrawable) {
        if (this.bitmap != null) {
            return this.bitmap
        }
    }

    val bitmap: Bitmap = if (intrinsicWidth <= 0 || intrinsicHeight <= 0) {
        Bitmap.createBitmap(
            1,
            1,
            Bitmap.Config.ARGB_8888
        ) // Single color bitmap will be created of 1x1 pixel
    } else {
        Bitmap.createBitmap(
            intrinsicWidth,
            intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
    }

    val canvas = Canvas(bitmap)
    setBounds(0, 0, canvas.width, canvas.height)
    draw(canvas)
    return bitmap
}