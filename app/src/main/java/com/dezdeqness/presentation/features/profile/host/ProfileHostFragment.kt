package com.dezdeqness.presentation.features.profile.host

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isGone
import androidx.navigation.fragment.NavHostFragment
import com.dezdeqness.R
import com.dezdeqness.core.BaseFragment
import com.dezdeqness.databinding.FragmentProfileHostBinding
import com.dezdeqness.di.AppComponent

class ProfileHostFragment : BaseFragment<FragmentProfileHostBinding>() {
    override fun setupScreenComponent(component: AppComponent) = Unit

    override fun getFragmentBinding(layoutInflater: LayoutInflater) =
        FragmentProfileHostBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getNavController().addOnDestinationChangedListener { _, destination, _ ->
            requireActivity().findViewById<View>(R.id.navigation)?.let {
                it.isGone = destination.id == R.id.settings || destination.id == R.id.statistics
            }
        }
    }

    // TODO: extension
    private fun getNavController() =
        (childFragmentManager.findFragmentById(R.id.container) as NavHostFragment).navController

}
