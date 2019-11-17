package com.moustafa.foodiemanual.base

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment

/**
 * @author moustafasamhoury
 * created on Sunday, 20 Oct, 2019
 */

abstract class BaseFragment(layout: Int) : Fragment(layout) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(view)
        ViewCompat.requestApplyInsets(view)
    }

    /**
     * This function is called at the appropriate time to initialise the views
     * i.e. set listeners...
     *
     * @param rootView – The fragment's root view
     */
    protected abstract fun setupViews(rootView: View)
}
