package com.dezdeqness.presentation.features.profile

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.dezdeqness.R
import com.dezdeqness.core.BaseFragment
import com.dezdeqness.databinding.FragmentProfileBinding
import com.dezdeqness.getComponent
import com.dezdeqness.presentation.features.authorization.AuthorizationActivity
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileFragment : BaseFragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: ProfileViewModel by viewModels(
        factoryProducer = {
            viewModelFactory
        }
    )

    private val authorizationObserver =
        registerForActivityResult(StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val code = result.data?.getStringExtra(AuthorizationActivity.KEY_AUTHORIZATION_CODE)
                viewModel.onAuthorizationCodeReceived(code)
            }
        }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity()
            .application
            .getComponent()
            .profileComponent()
            .create()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupObservers()
    }

    private fun setupView() {
        binding.signIn.setOnClickListener {
            AuthorizationActivity.startActivity(authorizationObserver, requireContext())
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.profileStateFlow.collect { state ->
                    if (state.isAuthorized) {
                        binding.profileContainer.isVisible = true
                        binding.nickname.text = state.nickname
                        Glide
                            .with(binding.avatar)
                            .load(state.avatar)
                            .centerCrop()
                            .placeholder(R.drawable.ic_launcher_background)
                            .into(binding.avatar)

                        binding.authorization.isVisible = false
                    } else {
                        binding.profileContainer.isVisible = false
                        binding.authorization.isVisible = true
                    }
                }
            }
        }
    }

}
