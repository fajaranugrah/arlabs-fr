package com.example.baseproject.presenter.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.baseproject.domain.model.FaceGalleryId
import com.example.baseproject.domain.model.Tenant
import com.example.baseproject.presenter.MainApplication
import com.example.baseproject.presenter.login.TenantLoginActivity
import com.example.baseproject.presenter.main.MainActivity
import javax.inject.Inject

class SplashScreenActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: SplashScreenViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MainApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)

        initObserver()
        initData()
    }

    private fun initObserver() {
        viewModel.isTenantLoggedIn.observe(this@SplashScreenActivity, {
            when (it) {
                true -> {
                    openMainPage()
                }
                false -> {
                    viewModel.saveLoggedTenant(
                        Tenant(
                            id = "si_db",
                            accessToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjbGllbnRfaWQiOiJzaV9kYiIsImlhdCI6IjIwMjEtMDktMTNUMjA6NTA6MDguODM4NTQ1NzY5KzA3OjAwIiwiaXNzIjoicmlzZXQuYWkiLCJqdGkiOiJpNE4yUnlxRklEMllxQWFCOWZQZWFsTTJ6Zk1NdElzRiIsInNjb3BlIjoie1wiZmFjZV9yZWNvZ25pdGlvbl9zZXJ2aWNlXCI6IHtcIm1heF9uX2hpdFwiOiAwLCBcIm1heF9uX2ZhY2VcIjogMCwgXCJtYXhfbl9mYWNlZ2FsbGVyeVwiOiAwfSwgXCJmYWNlX3ZlcmlmaWNhdGlvbl9zZXJ2aWNlXCI6IHtcIm1heF9uX2hpdFwiOiAwLCBcIm1heF9uX2ZhY2VcIjogMCwgXCJtYXhfbl9mYWNlZ2FsbGVyeVwiOiAwfX0ifQ.Nq3Mt2B-IOpqEvNwajGNRP7JBJcp53BrfG2nozbOwb_qPZRlHcivTAyxnbYRznwVIMj3ZH6nBNvw6YHEoaYkJsr7SJWEzakDZPTYAWnmEQVLmLQjd3bU67T2TbSFBH_1jRMvpfiZEoqJ9l_jlAQVTetfSZLoc_AsrknLqfF5l06MqR20qtbX4dRAQy9atxO73f54-3fLqUR79PJnovVrj-hVxALxv7xARzof69o3M-6vm9f_V6685mU1oGXfn6oW5Qc7YELzxwrQl5arfe0gtwqtmrCR0bwe1DcpHMksG1DQD6tyDydYdAHpWK4SDmHGUCPNgtXTD80V5OFBj2JD1q3e0b1-9nAP_3bjZSjlqdM01EYgrt9l9gVR8Rob84P5F90v2AsuIaXzit6dYVxlZhcBk-5siISkLSi6dLEwVUg-odTT529oJWX4ONtu7TdF7BBiKH5CkUEdISCFNm13bj5UKPOjVLqaPUcySz1rRL3AkdCJjXbcgMpiDTNlCKLtmy9KIObVGOYsnd0B3_Lu9606F1ZVpWiFY5uonLQQgJLyHJay2SQCV70nAX41nFku2blRZrSnpfIauOvgus1p289y_5T5XvApBry7QfHNhFh2slwIirVgmCU9W9WmKVwfrqDINCVcQsslOR37bD_SPVs1LHeWABn4LAZhuIjjGbk",
                        )
                    )
                    openMainPage()
                }
            }

            finish()
        })
    }

    private fun initData() {
        viewModel.checkTenantHasLoggedIn()
        viewModel.createFaceGalleryId()
    }

    private fun openLoginTenantPage() {
        val intent = Intent(this, TenantLoginActivity::class.java)
        startActivity(intent)
    }

    private fun openMainPage() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}