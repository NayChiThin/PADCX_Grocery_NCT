package com.padc.grocery.mvp.presenters.impls

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import com.padc.grocery.analytics.SCREEN_HOME
import com.padc.grocery.data.model.AuthenticationModelImpl
import com.padc.grocery.data.model.GroceryModelImpl
import com.padc.grocery.data.vos.GroceryVO
import com.padc.grocery.mvp.presenters.AbstractBasePresenter
import com.padc.grocery.mvp.presenters.MainPresenter
import com.padc.grocery.mvp.views.MainView

class MainPresenterImpl : MainPresenter,AbstractBasePresenter<MainView>(){

    private val mGroceryModel = GroceryModelImpl
    private val mAuthenticationModel = AuthenticationModelImpl
    private var mChosenGroceryForFileUpload : GroceryVO? = null

    override fun onTapAddGrocery(name: String, description: String, amount: Int) {
        mGroceryModel.addGrocery(name,description, amount,"")
    }

    override fun onPhotoTaken(bitmap: Bitmap) {
        Log.d("Photo taken","Photo Taken")
        mChosenGroceryForFileUpload?.let {
            mGroceryModel.uploadImageAndUpdateGrocery(it,bitmap)
        }
    }

    override fun onTapAddGroceryWithImage(
        name: String,
        description: String,
        amount: Int,
        image: Bitmap
    ) {
        mGroceryModel.addGroceryWithImage(name, description, amount, image)
    }

    override fun onUiReady(context: Context,owner: LifecycleOwner) {
        sendEventsToFirebaseAnalytics(context, SCREEN_HOME)
        mView.displayRecyclerView(mGroceryModel.getLayoutType())
        val userName = mAuthenticationModel.getUserName()
        mView.showUserName(userName)
        mGroceryModel.getGroceries(
            onSuccess = {
                mView.showGroceryData(it)
            },
            onFailure = {
                mView.showErrorMessage(it)
            }
        )
        mView.displayToolbarTitle(mGroceryModel.getAppNameFromRemoteConfig()+"-"+mGroceryModel.getGrocery())
    }

    override fun onTapDeleteGrocery(name: String) {
        mGroceryModel.removeGrocery(name)
    }

    override fun onTapEditGrocery(name: String, description: String, amount: Int,image:String) {
        mView.showGroceryDialog(name,description,amount.toString(),image)
    }

    override fun onTapFileUpload(grocery: GroceryVO) {
        mChosenGroceryForFileUpload = grocery
        mView.openGallery()
    }

}