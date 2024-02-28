package com.example.weddingapp.ui

import android.R
import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.viewbinding.ViewBinding
import com.example.presenter.BasePresenter

abstract class BaseActivity<B: ViewBinding> :
    AppCompatActivity() {

    private lateinit var mPresenter: BasePresenter
    private lateinit var initialBackground: Drawable
    lateinit var binding: B

    abstract fun getViewBinding(): B

    protected fun bindPresenterToLifecycle(presenter: BasePresenter) {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED)) {
            throw UninitializedPropertyAccessException("presenter should be bound in the init block!")
        }

        mPresenter = presenter
    }

    private fun onSuperCreated() {
        lockPortraitOrientation()
        this.binding = getViewBinding()
        super.setContentView(this.binding.root)
        mPresenter.create()

        onCreate()
    }

    protected abstract fun onCreate()

    override fun onStart() {
        super.onStart()
        mPresenter.start()
    }

    override fun onStop() {
        mPresenter.stop()
        super.onStop()
    }

    override fun onDestroy() {
        mPresenter.destroy()
        super.onDestroy()
    }

    //region Helper functions
    @SuppressLint("SourceLockedOrientationActivity")
    private fun lockPortraitOrientation() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    fun setTransparentBackground() {
        initialBackground = window.decorView.background
        window.decorView.setBackgroundResource(R.color.transparent)
    }

    fun restoreBackground() {
        window.decorView.background = initialBackground
    }
    //endregion Helper functions

    //region Default function overriding prevention
    @Deprecated(
        "This function should not be overridden when extending BaseActivity.",
        ReplaceWith("onCreate()"),
        DeprecationLevel.ERROR
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onSuperCreated()
    }

    @Deprecated(
        "This function should not be overridden when extending BaseActivity.",
        ReplaceWith("onCreate()"),
        DeprecationLevel.ERROR
    )
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        onSuperCreated()
    }

    @Deprecated(
        "This function should not be overridden when extending BaseActivity. Pass the " +
                "layout resource id as a constructor param instead",
        ReplaceWith("BaseActivity(layoutResID"),
        DeprecationLevel.ERROR
    )
    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
    }
    //endregion
}