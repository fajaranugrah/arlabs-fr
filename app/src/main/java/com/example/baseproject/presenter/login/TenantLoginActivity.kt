package com.example.baseproject.presenter.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import com.example.baseproject.R
import com.example.baseproject.data.Resource
import com.example.baseproject.databinding.ActivityTenantLoginBinding
import com.example.baseproject.presenter.MainApplication
import com.example.baseproject.presenter.dialog.ProgressDialogFragment
import com.example.baseproject.presenter.main.MainActivity
import com.example.baseproject.util.ext.showToast
import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil
import javax.inject.Inject

class TenantLoginActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: TenantLoginViewModel by viewModels { viewModelFactory }

    private lateinit var binding: ActivityTenantLoginBinding

    private val progressDialog by lazy { ProgressDialogFragment.newInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MainApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)

        binding = ActivityTenantLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewListener()
        initObserver()
    }

    private fun initObserver() {
        viewModel.loginTenantLiveData.observe(this, {
            when (it) {
                is Resource.Loading -> {
                    progressDialog.show(supportFragmentManager)
                }
                is Resource.Success -> {
                    progressDialog.hide()
                    it.data?.let { tenant ->
                        viewModel.saveLoggedTenant(tenant)
                        openMainPage()
                    }
                }
                is Resource.Error -> {
                    progressDialog.hide()
                    showToast(it.message)
                }
            }
        })
    }

    private fun initViewListener() {
        binding.etClientId.doAfterTextChanged {
            if (binding.tilClientId.isErrorEnabled) {
                binding.tilClientId.isErrorEnabled = false
            }
        }
        binding.etPassword.doAfterTextChanged {
            if (binding.tilPassword.isErrorEnabled) {
                binding.tilPassword.isErrorEnabled = false
            }
        }

        binding.btnSignIn.setOnClickListener {
            UIUtil.hideKeyboard(this)
            loginTenant()
        }
    }

    private fun loginTenant() {
        val clientId = binding.etClientId.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        var isDataValid = true

        if (clientId.isEmpty()) {
            binding.tilClientId.error = getString(R.string.error_client_id_required)
            isDataValid = false
        }
        if (password.isEmpty()) {
            binding.tilPassword.error = getString(R.string.error_password_required)
            isDataValid = false
        }

        if (!isDataValid) {
            return
        }

        viewModel.loginTenant(
            clientId = clientId,
            password = password,
        )
    }

    private fun openMainPage() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }
}