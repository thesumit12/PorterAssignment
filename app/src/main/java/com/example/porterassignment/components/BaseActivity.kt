package com.example.porterassignment.components

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

/**
 * Base activity to be extended by all activities of application
 * @author: Sumit Lakra
 * @date 09/02/19
 */
abstract class BaseActivity<T : ViewDataBinding, V : BaseViewModel> : AppCompatActivity(), KoinComponent {

    private lateinit var mViewDataBinding: T
    private var mViewModel: V? = null

    private val waitingDialogHelper = WaitingDialogHelper()

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun getViewModel(): V

    abstract fun getBindingVariable(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performDataBinding()
    }

    /**
     * Function to get ViewDataBinding
     * @return [T] ViewDataBinding
     * @author Sumit Lakra
     * @date 09/02/19
     */
    fun getViewDataBinding(): T = mViewDataBinding

    /**
     * Function to execute ViewDataBinding
     * @author Sumit Lakra
     * @date 04/25/2019
     */
    private fun performDataBinding() {
        mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId())
        this.mViewModel = if (mViewModel == null) getViewModel() else mViewModel
        mViewDataBinding.setVariable(getBindingVariable(), mViewModel)
        mViewDataBinding.executePendingBindings()
    }

    /**
     * Function to show waiting dialog
     * @param [msg] String
     * @author Sumit Lakra
     * @date 09/02/19
     */
    fun showWaitingDialog(msg: String) = waitingDialogHelper.show(this, msg)

    /**
     * Function to show waiting dialog
     * @param [msg] Int
     * @author Sumit Lakra
     * @date 09/02/19
     */
    fun showWaitingDialog(msg: Int) = waitingDialogHelper.show(this, msg)

    /**
     * Function to hide waiting dialog
     * @author Sumit Lakra
     * @date 09/02/19
     */
    fun hideWaitingDialog() = waitingDialogHelper.hideDialog()

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item?.itemId == android.R.id.home) {
            onBackPressed()
            true
        } else
            super.onOptionsItemSelected(item)
    }

    /**
     * Function to check internet Connection
     * @author slakra
     * @date 09/04/19
     */
    fun isNetworkAvailable() : Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo.isConnected
    }
}