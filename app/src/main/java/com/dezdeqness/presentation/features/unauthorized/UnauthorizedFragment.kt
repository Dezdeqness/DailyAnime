package com.dezdeqness.presentation.features.unauthorized

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.dezdeqness.R
import com.dezdeqness.core.BaseFragment
import com.dezdeqness.databinding.FragmentUnauthorizedBinding
import com.dezdeqness.di.AppComponent
import com.dezdeqness.presentation.features.authorization.AuthorizationActivity

class UnauthorizedFragment : BaseFragment<FragmentUnauthorizedBinding>() {

    override fun setupScreenComponent(component: AppComponent) {
    }

    override fun getFragmentBinding(layoutInflater: LayoutInflater) =
        FragmentUnauthorizedBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupListeners()
    }

    private fun setupView() {
        arguments
            ?.getInt("titleResId", R.string.unauthorized_title_default)
            ?.let {
                binding.title.text = getString(it)
            }
    }

    private fun setupListeners() {
        binding.signIn.setOnClickListener {
            AuthorizationActivity.startActivity(
                requireContext(),
                isLogin = true
            )
        }

        binding.signUp.setOnClickListener {
            AuthorizationActivity.startActivity(
                requireContext(),
                isLogin = false
            )
        }
    }

}
