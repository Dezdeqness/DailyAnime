package com.dezdeqness.presentation.features.genericlistscreen

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.dezdeqness.core.BaseComposeFragment
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.presentation.event.ConsumableEvent
import com.dezdeqness.presentation.event.Event
import com.dezdeqness.presentation.event.EventConsumer
import kotlinx.coroutines.launch

abstract class GenericListableFragment : BaseComposeFragment() {

    private var onBackPressedCallback: OnBackPressedCallback? = null

    private val eventConsumer: EventConsumer by lazy {
        EventConsumer(
            fragment = this,
            context = this.requireContext(),
        )
    }

    private val viewModel: GenericListableViewModel by viewModels(
        factoryProducer = { viewModelFactory },
    )

    @StringRes
    abstract fun getTitleRes(): Int

    open fun onEvent(event: Event) = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedCallback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().popBackStack()
        }
    }

    @Composable
    override fun FragmentContent() {
        AppTheme {
            GenericListPage()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { event ->
                    val isConsumed = onEvent(event)
                    if (isConsumed) {
                        return@collect
                    }

                    when (event) {
                        is ConsumableEvent -> {
                            eventConsumer.consume(event)
                        }

                        else -> {}
                    }
                }
            }
        }
    }

}

//<?xml version="1.0" encoding="utf-8"?>
//<androidx.appcompat.widget.LinearLayoutCompat xmlns:android="http://schemas.android.com/apk/res/android"
//    xmlns:app="http://schemas.android.com/apk/res-auto"
//    android:layout_width="match_parent"
//    android:layout_height="match_parent"
//    android:orientation="vertical">
//
//    <com.google.android.material.appbar.AppBarLayout
//        android:id="@+id/appbar_layout"
//        android:layout_width="match_parent"
//        android:layout_height="wrap_content"
//        app:layout_constraintEnd_toEndOf="parent"
//        app:layout_constraintStart_toStartOf="parent"
//        app:layout_constraintTop_toTopOf="parent">
//
//        <com.google.android.material.appbar.MaterialToolbar
//            android:id="@+id/toolbar"
//            android:layout_width="match_parent"
//            android:layout_height="wrap_content"
//            android:backgroundTint="@color/background_tint"
//            app:navigationIcon="@drawable/ic_back"
//            app:title="@string/toolbar_title_similar"
//            app:titleTextColor="@color/text_primary" />
//
//    </com.google.android.material.appbar.AppBarLayout>
//
//        <com.dezdeqness.ui.RecyclerViewWithState
//            android:id="@+id/recycler"
//            android:layout_width="match_parent"
//            android:layout_height="match_parent"
// />
//
//</androidx.appcompat.widget.LinearLayoutCompat>