package com.dezdeqness.core

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.dezdeqness.di.AppComponent
import com.dezdeqness.getComponent
import javax.inject.Inject

abstract class BaseFragment<V : ViewBinding> : Fragment() {

    private var _binding: V? = null
    protected val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getFragmentBinding(layoutInflater)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setupScreenComponent(
            requireActivity()
                .application
                .getComponent()
        )
    }

    abstract fun setupScreenComponent(component: AppComponent)

    abstract fun getFragmentBinding(layoutInflater: LayoutInflater): V

}
