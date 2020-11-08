package com.padc.grocery.mvp.presenters.impls

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.padc.grocery.analytics.PARAMETER_EMAIL
import com.padc.grocery.analytics.SCREEN_LOGIN
import com.padc.grocery.analytics.TAP_LOGIN
import com.padc.grocery.data.model.AuthenticationModel
import com.padc.grocery.data.model.AuthenticationModelImpl
import com.padc.grocery.data.model.GroceryModel
import com.padc.grocery.data.model.GroceryModelImpl
import com.padc.grocery.mvp.presenters.AbstractBasePresenter
import com.padc.grocery.mvp.presenters.LoginPresenter
import com.padc.grocery.mvp.views.LoginView

class LoginPresenterImpl : LoginPresenter,AbstractBasePresenter<LoginView>() {

    private val mAuthenticationModel : AuthenticationModel = AuthenticationModelImpl
    private val mGroceryModel : GroceryModel = GroceryModelImpl

    override fun onTapLogin(context: Context,email: String, password: String) {
        sendEventsToFirebaseAnalytics(context, TAP_LOGIN, PARAMETER_EMAIL,email)
        mAuthenticationModel.login(email,password,onSuccess = {
            mView.navigateToHomeScreen()
        }, onFailure = {
            mView.showError(it)
        })
    }

    override fun onTapRegister() {
        mView.navigateToRegisterScreen()
    }

    override fun onUiReady(context:Context,owner: LifecycleOwner) {
        sendEventsToFirebaseAnalytics(context, SCREEN_LOGIN)
        mGroceryModel.setUpRemoteConfigWithDefaultValues()
        mGroceryModel.fetchRemoteConfigs()
    }
}