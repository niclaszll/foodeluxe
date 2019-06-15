package com.tuproject


interface BasePresenter<T> {

    fun takeView(view: T)

    fun dropView()

}