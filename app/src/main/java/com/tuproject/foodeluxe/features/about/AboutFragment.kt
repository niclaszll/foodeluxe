package com.tuproject.foodeluxe.features.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tuproject.foodeluxe.R
import com.tuproject.foodeluxe.commons.extensions.inflate
import com.tuproject.foodeluxe.di.ActivityScoped
import dagger.android.support.DaggerFragment
import javax.inject.Inject

@ActivityScoped
class AboutFragment @Inject constructor(): DaggerFragment(), AboutContract.View {

    @Inject
    lateinit var presenter: AboutContract.Presenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_about)
    }

    override fun onResume() {
        super.onResume()
        presenter.takeView(this)
    }

    override fun onDestroy() {
        presenter.dropView()
        super.onDestroy()
    }


}
