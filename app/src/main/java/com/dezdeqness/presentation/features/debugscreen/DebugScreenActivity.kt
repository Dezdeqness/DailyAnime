package com.dezdeqness.presentation.features.debugscreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.data.core.config.ConfigKeys
import com.dezdeqness.getComponent
import javax.inject.Inject
import kotlin.getValue

class DebugScreenActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: DebugScreenViewModel by viewModels(
        factoryProducer = {
            viewModelFactory
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        application
            .getComponent()
            .debugComponent()
            .create()
            .inject(this)

        setContent {
            AppTheme {
                DebugScreenPage(
                    uiState = viewModel.uiState,
                    actions = object : DebugScreenActions {
                        override fun onInitialLoading() {
                            viewModel.onInitialLoading()
                        }

                        override fun onOverrideConfigKeysClicked(value: Boolean) {
                            viewModel.onOverrideConfigKeysClicked(value)
                        }

                        override fun setValue(key: ConfigKeys, value: Any) {
                            viewModel.updateConfigValue(key, value)
                        }
                    }
                )
            }
        }
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, DebugScreenActivity::class.java)
    }
}
