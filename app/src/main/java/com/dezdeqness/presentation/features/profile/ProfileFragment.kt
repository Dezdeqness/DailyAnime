package com.dezdeqness.presentation.features.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.dezdeqness.R
import com.dezdeqness.core.BaseFragment
import com.dezdeqness.databinding.FragmentProfileBinding
import com.dezdeqness.di.AppComponent
import kotlinx.coroutines.launch

class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    private val viewModel: ProfileViewModel by viewModels(
        factoryProducer = {
            viewModelFactory
        }
    )

    override fun getFragmentBinding(layoutInflater: LayoutInflater) =
        FragmentProfileBinding.inflate(layoutInflater)

    override fun setupScreenComponent(component: AppComponent) =
        component
            .profileComponent()
            .create()
            .inject(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupViews()
    }

    private fun setupViews() {
        with(binding) {
            historyLabel.setOnClickListener {
                // TODO: event
                this@ProfileFragment
                    .findNavController()
                    .navigate(
                        R.id.history,
                    )
            }
            statisticsLabel.setOnClickListener {

            }

            settingsLabel.setOnClickListener {
                // TODO: event
                this@ProfileFragment
                    .findNavController()
                    .navigate(
                        R.id.settings,
                    )
            }
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.profileStateFlow.collect { state ->

                    binding.nickname.text = state.nickname
                    Glide
                        .with(binding.avatar)
                        .load(state.avatar)
                        .centerCrop()
                        .placeholder(R.drawable.ic_launcher_background)
                        .into(binding.avatar)
                }
            }
        }
    }

}
