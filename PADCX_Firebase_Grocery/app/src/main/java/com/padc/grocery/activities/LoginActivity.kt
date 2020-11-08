package com.padc.grocery.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.padc.grocery.R
import com.padc.grocery.mvp.presenters.LoginPresenter
import com.padc.grocery.mvp.presenters.impls.LoginPresenterImpl
import com.padc.grocery.mvp.views.LoginView
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity(),LoginView {

    companion object {
        fun newIntent(context: Context):Intent{
            return Intent(context,LoginActivity::class.java)
        }
    }

    private lateinit var mPresenter : LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(findViewById(R.id.toolbar))

        setUpPresenter()
        setUpActionListeners()

        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            Log.d("fbToken",it.token)
        }

        mPresenter.onUiReady(this,this)
    }

    private fun setUpPresenter() {
        mPresenter = getPresenter<LoginPresenterImpl,LoginView>()
    }
    private fun setUpActionListeners() {
        btnLogin.setOnClickListener {
            mPresenter.onTapLogin(this,etEmail.text.toString(),etPassword.text.toString())
        }
        btnRegister.setOnClickListener {
            mPresenter.onTapRegister()
        }
    }

    override fun navigateToHomeScreen() {
        startActivity(MainActivity.newIntent(this))
    }

    override fun navigateToRegisterScreen() {
        startActivity(RegisterActivity.newIntent(this))
    }
}