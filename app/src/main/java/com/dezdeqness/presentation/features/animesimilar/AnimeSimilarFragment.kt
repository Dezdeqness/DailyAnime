package com.dezdeqness.presentation.features.animesimilar

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dezdeqness.R
import com.dezdeqness.data.analytics.AnalyticsManager
import com.dezdeqness.di.AppComponent
import com.dezdeqness.di.subcomponents.SimilarArgsModule
import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.event.AnimeDetails
import com.dezdeqness.presentation.event.Event
import com.dezdeqness.presentation.features.animesimilar.composable.SimilarItem
import com.dezdeqness.presentation.features.genericlistscreen.GenericListableFragment
import com.dezdeqness.presentation.features.genericlistscreen.GenericRenderer
import com.dezdeqness.presentation.models.AdapterItem
import com.dezdeqness.presentation.models.SimilarUiModel
import javax.inject.Inject

class AnimeSimilarFragment : GenericListableFragment() {

    private val args by navArgs<AnimeSimilarFragmentArgs>()

    @Inject
    lateinit var analyticsManager: AnalyticsManager

    override fun getTitleRes() = R.string.anime_similar_title

    override val renderer: GenericRenderer
        get() = object : GenericRenderer {
            @Composable
            override fun Render(modifier: Modifier, item: AdapterItem, onClick: (Action) -> Unit) {
                if (item !is SimilarUiModel) return

                SimilarItem(
                    modifier = modifier,
                    item = item,
                    onClick = onClick,
                )
            }
        }

    override fun setupScreenComponent(component: AppComponent) {
        component
            .animeSimilarComponent()
            .argsModule(SimilarArgsModule(args.animeId))
            .build()
            .inject(this)
    }

    override fun onEvent(event: Event) =
        when (event) {
            is AnimeDetails -> {
                analyticsManager.detailsTracked(id = event.animeId.toString(), title = event.title)
                findNavController().navigate(
                    AnimeSimilarFragmentDirections.animeDetailsFragment(event.animeId)
                )
                true
            }

            else -> {
                false
            }
        }

}

//            android:layout_width="wrap_content"
//            android:layout_height="wrap_content"
//            app:cardCornerRadius="8dp"
//            app:cardElevation="0dp"
//            app:cardPreventCornerOverlap="false"
//            app:layout_constraintStart_toStartOf="parent"
//            app:layout_constraintTop_toTopOf="parent">
//
//            <androidx.appcompat.widget.AppCompatImageView
//                android:id="@+id/anime_logo"
//                android:layout_width="96dp"
//                android:layout_height="120dp"
//                tools:src="@drawable/ic_placeholder" />
//
//            <androidx.appcompat.widget.AppCompatTextView
//                android:id="@+id/anime_score"
//                android:layout_width="wrap_content"
//                android:layout_height="wrap_content"
//                android:background="@color/background_shadow"
//                android:gravity="center"
//                android:padding="4dp"
//                android:textAllCaps="true"
//                android:textColor="@color/pure_white"
//                android:textSize="12sp"
//                app:layout_constraintStart_toStartOf="@+id/anime_logo"
//                app:layout_constraintTop_toTopOf="@+id/anime_logo"
//                tools:text="10" />
//
//        </com.google.android.material.card.MaterialCardView>
//
//        <TextView
//            android:id="@+id/anime_title"
//            android:layout_width="0dp"
//            android:layout_height="wrap_content"
//            android:layout_marginStart="12dp"
//            android:layout_marginLeft="12dp"
//            android:layout_marginTop="12dp"
//            android:layout_marginEnd="12dp"
//            android:layout_marginRight="12dp"
//            android:ellipsize="end"
//            android:maxLines="2"
//            android:textColor="@color/text_primary"
//            android:textSize="12sp"
//            android:textStyle="bold"
//            app:layout_constraintEnd_toEndOf="parent"
//            app:layout_constraintStart_toEndOf="@+id/anime_logo_container"
//            app:layout_constraintTop_toTopOf="parent"
//            tools:text="86 часть один" />
//
//        <androidx.appcompat.widget.AppCompatTextView
//            android:id="@+id/anime_brief_info"
//            android:layout_width="wrap_content"
//            android:layout_height="wrap_content"
//            android:layout_marginTop="4dp"
//            android:textSize="12sp"
//            app:layout_constraintStart_toStartOf="@+id/anime_title"
//            app:layout_constraintTop_toBottomOf="@+id/anime_title"
//            tools:text="Онгоинг • TV • 12 эп." />
//
//    </androidx.constraintlayout.widget.ConstraintLayout>
//
//</com.google.android.material.card.MaterialCardView>