package com.dezdeqness.presentation.features.searchfilter

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dezdeqness.presentation.features.searchfilter.composables.Section
import kotlinx.coroutines.flow.StateFlow

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
//            app:navigationIcon="@drawable/ic_close"
//            app:title="@string/search_filter_title"
//            app:titleTextColor="@color/text_primary">
//
//        </com.google.android.material.appbar.MaterialToolbar>
//    </com.google.android.material.appbar.AppBarLayout>
//
//    <androidx.core.widget.NestedScrollView
//        android:id="@+id/scroll_container"
//        android:layout_width="match_parent"
//        android:layout_height="0dp"
//        app:layout_constraintBottom_toTopOf="@id/buttons_container"
//        app:layout_constraintEnd_toEndOf="parent"
//        app:layout_constraintStart_toStartOf="parent"
//        app:layout_constraintTop_toBottomOf="@id/appbar_layout">
//
//        <androidx.appcompat.widget.LinearLayoutCompat
//            android:id="@+id/filter_container"
//            android:layout_width="match_parent"
//            android:layout_height="wrap_content"
//            android:orientation="vertical" />
//
//    </androidx.core.widget.NestedScrollView>
//
//    <androidx.appcompat.widget.LinearLayoutCompat
//        android:id="@+id/buttons_container"
//        android:layout_width="match_parent"
//        android:layout_height="wrap_content"
//        android:orientation="horizontal"
//        app:layout_constraintBottom_toBottomOf="parent"
//        app:layout_constraintEnd_toEndOf="parent"
//        app:layout_constraintStart_toStartOf="parent">
//
//        <com.google.android.material.button.MaterialButton
//            android:id="@+id/reset_filter"
//            android:layout_width="wrap_content"
//            android:layout_height="wrap_content"
//            android:layout_gravity="center"
//            android:layout_margin="8dp"
//            android:layout_weight="0.5"
//            android:text="@string/search_filter_reset" />
//
//        <com.google.android.material.button.MaterialButton
//            android:id="@+id/apply_filter"
//            android:layout_width="wrap_content"
//            android:layout_height="wrap_content"
//            android:layout_margin="8dp"
//            android:layout_weight="0.5"
//            android:text="@string/search_filter_apply" />
//
//    </androidx.appcompat.widget.LinearLayoutCompat>

//    <style name="SearchFilter.Title" parent="Widget.AppCompat.TextView">
//        <item name="android:textSize">20sp</item>
//        <item name="android:textColor">@color/text_primary</item>
//        <item name="android:textStyle">bold</item>
//    </style>
//
//    <style name="SearchFilter.Chip" parent="Base.Widget.MaterialComponents.Chip">
//        <item name="android:stateListAnimator">@null</item>
//        <item name="chipStrokeWidth">1dp</item>
//        <item name="rippleColor">@null</item>
//        <item name="android:textSize">14sp</item>
//    </style>
//
//    <androidx.appcompat.widget.AppCompatTextView
//        android:id="@+id/title"
//        style="@style/SearchFilter.Title"
//        android:layout_width="wrap_content"
//        android:layout_height="wrap_content"
//        android:paddingHorizontal="16dp"
//        android:paddingTop="8dp"
//        android:paddingBottom="8dp"
//        app:layout_constraintStart_toStartOf="parent"
//        app:layout_constraintTop_toTopOf="parent"
//        tools:text="Genre" />
//
//    <com.google.android.material.chip.ChipGroup
//        android:id="@+id/container"
//        android:layout_width="match_parent"
//        android:layout_height="wrap_content"
//        android:paddingHorizontal="16dp"
//        android:paddingBottom="8dp"
//        app:layout_constraintEnd_toEndOf="parent"
//        app:layout_constraintStart_toStartOf="parent"
//        app:layout_constraintTop_toBottomOf="@id/title" />
//
//    <com.google.android.material.divider.MaterialDivider
//        android:id="@+id/divider"
//        android:layout_width="match_parent"
//        android:layout_height="wrap_content"
//        app:dividerColor="@color/filter_divider"
//        app:dividerThickness="1dp"
//        app:layout_constraintEnd_toEndOf="parent"
//        app:layout_constraintStart_toStartOf="parent"
//        app:layout_constraintTop_toBottomOf="@id/container" />
//
//
//<androidx.appcompat.widget.AppCompatTextView
//android:id="@+id/title"
//style="@style/SearchFilter.Title"
//android:layout_width="wrap_content"
//android:layout_height="wrap_content"
//android:paddingHorizontal="16dp"
//android:paddingTop="8dp"
//android:paddingBottom="8dp"
//app:layout_constraintStart_toStartOf="parent"
//app:layout_constraintTop_toTopOf="parent"
//tools:text="Genre" />
//
//<com.google.android.material.chip.ChipGroup
//android:id="@+id/container"
//android:layout_width="match_parent"
//android:layout_height="wrap_content"
//android:paddingHorizontal="16dp"
//android:paddingBottom="8dp"
//app:layout_constraintEnd_toEndOf="parent"
//app:layout_constraintStart_toStartOf="parent"
//app:layout_constraintTop_toBottomOf="@id/title" />
//
//<com.google.android.material.divider.MaterialDivider
//android:id="@+id/divider"
//android:layout_width="match_parent"
//android:layout_height="wrap_content"
//app:dividerColor="@color/filter_divider"
//app:dividerThickness="1dp"
//app:layout_constraintEnd_toEndOf="parent"
//app:layout_constraintStart_toStartOf="parent"
//app:layout_constraintTop_toBottomOf="@id/container" />

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeSearchFilter(
    modifier: Modifier = Modifier,
    stateFlow: StateFlow<AnimeSearchFilterState>,
    actions: AnimeSearchFilterActions,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val state by stateFlow.collectAsState()

    if (state.isFilterVisible) {
        ModalBottomSheet(
            onDismissRequest = {
                actions.onDismissed()
            },
            sheetState = sheetState,
            modifier = modifier
                .fillMaxSize()
                .systemBarsPadding(),
            shape = RoundedCornerShape(0.dp),
            dragHandle = null
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
            ) {
                items(state.items.size) { index ->
                    Section(
                        sectionData = state.items[index],
                        onClick = { innerId, cellId ->
                            actions.onCellClicked(innerId, cellId)
                        }
                    )
                }
            }
        }
    }

//    private fun setChipState(chip: Chip, item: AnimeCell) {
//        when (item.state) {
//            CellState.INCLUDE -> {
//                chip.setTextColor(ContextCompat.getColor(context, R.color.pure_white))
//                chip.setChipBackgroundColorResource(R.color.purple_500)
//                chip.setCloseIconResource(R.drawable.ic_plus)
//                chip.setCloseIconTintResource(R.color.pure_white)
//                chip.isCloseIconVisible = true
//            }
//            CellState.EXCLUDE -> {
//                chip.setTextColor(ContextCompat.getColor(context, R.color.pure_white))
//                chip.setChipBackgroundColorResource(R.color.purple_500)
//                chip.setCloseIconResource(R.drawable.ic_minus)
//                chip.setCloseIconTintResource(R.color.pure_white)
//                chip.isCloseIconVisible = true
//            }
//            else -> {
//                chip.setTextColor(ContextCompat.getColor(context, R.color.text_primary))
//                chip.isCloseIconVisible = false
//            }
//        }
//    }
//
//    private fun setupObservers() {
//        viewLifecycleOwner.lifecycleScope.launch {
//            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.animeSearchFilterStateFlow.collect { state ->
//                    setInitialState(state)
//                    updateCell(state)
//                    val isSelected =
//                        state.items.flatMap { it.items }.any { it.state != CellState.NONE }
//
//                    binding.resetFilter.isVisible = isSelected
//
//                    viewModel.nullifyCurrentCell()
//                }
//            }
//        }
//    }
//
//    private fun updateCell(state: AnimeSearchFilterState) {
//        binding
//            .filterContainer
//            .children
//            .forEach { searchFilterView ->
//                if (searchFilterView.updateChip(state.currentCellUpdate)) {
//                    return
//                }
//            }
//    }

}
