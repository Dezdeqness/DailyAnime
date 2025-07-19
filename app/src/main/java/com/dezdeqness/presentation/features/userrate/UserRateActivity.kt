package com.dezdeqness.presentation.features.userrate

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.transition.Slide
import android.view.Gravity
import android.view.Window
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dezdeqness.core.utils.collectEvents
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.di.subcomponents.UserRateModule
import com.dezdeqness.getComponent
import com.dezdeqness.presentation.event.EditUserRate
import kotlinx.parcelize.Parcelize
import javax.inject.Inject


class UserRateActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: UserRateViewModel by viewModels(
        factoryProducer = {
            viewModelFactory
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(window) {
            requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)

            val slideEnter = Slide(Gravity.BOTTOM).apply {
                excludeTarget(android.R.id.statusBarBackground, true)
                excludeTarget(android.R.id.navigationBarBackground, true)
            }

            enterTransition = slideEnter

            val slideExit = Slide(Gravity.TOP).apply {
                excludeTarget(android.R.id.statusBarBackground, true)
                excludeTarget(android.R.id.navigationBarBackground, true)
            }

            exitTransition = slideExit

        }

        val params = intent.getParcelableExtra<UserRateParams>(USER_RATE_PARAMS) ?: return

        application
            .getComponent()
            .editRateComponent()
            .userRateModule(
                UserRateModule(
                    rateId = params.userRateId,
                    title = params.title,
                )
            )
            .build()
            .inject(this)

        setContent {
            AppTheme {
                UserRatePage(
                    stateFlow = viewModel.userRateStateFlow,
                    actions = object : UserRateActions {
                        override fun onStatusChanged(id: String) {
                            viewModel.onStatusChanged(id)
                        }

                        override fun onScoreChanged(score: Long) {
                            viewModel.onScoreChanged(score)
                        }

                        override fun onSelectStatusClicked() {
                            viewModel.onSelectStatusClicked()
                        }

                        override fun onCloseSelectStatusClicked() {
                            viewModel.onCloseSelectStatusClicked()
                        }

                        override fun onResetClicked() {
                            viewModel.onResetButtonClicked()
                        }

                        override fun onChangeRateClicked() {
                            viewModel.onApplyButtonClicked()
                        }

                        override fun onBackPressed() {
                            this@UserRateActivity.onBackPressedDispatcher.onBackPressed()
                        }

                        override fun onIncrementEpisode() {
                            viewModel.onEpisodesPlusClicked()
                        }

                        override fun onDecrementEpisode() {
                            viewModel.onEpisodesMinusClicked()
                        }

                        override fun onEpisodesChanged(episodes: String) {
                            viewModel.onEpisodesChanged(episodes)
                        }

                        override fun onCommentChanged(comment: String) {
                            viewModel.onCommentChanged(comment)
                        }
                    }
                )
            }

            viewModel.events.collectEvents { event ->
                when (event) {
                    is EditUserRate -> {
                        val result = Intent().apply {
                            putExtra(USER_RATE_RESULT, event.userRateUiModel)
                        }
                        setResult(RESULT_OK, result)
                        finish()
                    }

                     else -> {}
                }
            }
        }
    }

    class UserRate : ActivityResultContract<UserRateParams, EditRateUiModel?>() {
        override fun createIntent(context: Context, input: UserRateParams) =
            Intent(context, UserRateActivity::class.java).apply {
                putExtra(USER_RATE_PARAMS, input)
            }

        override fun parseResult(resultCode: Int, intent: Intent?): EditRateUiModel? {
            if (resultCode != Activity.RESULT_OK) {
                return null
            }
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent?.getParcelableExtra(USER_RATE_RESULT, EditRateUiModel::class.java)
            } else {
                intent?.getParcelableExtra(USER_RATE_RESULT)
            }
        }
    }

    @Parcelize
    data class UserRateParams(
        val userRateId: Long,
        val title: String,
    ) : Parcelable

    companion object {
        private const val USER_RATE_PARAMS = "user_rate_params"
        private const val USER_RATE_RESULT = "user_rate_result"
    }

}
