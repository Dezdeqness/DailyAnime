package com.dezdeqness.presentation.features.genericlistscreen.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.features.genericlistscreen.LocalAdapterItemRenderer
import com.dezdeqness.presentation.models.AdapterItem
import com.google.common.collect.ImmutableList

@Composable
fun GenericList(
    modifier: Modifier = Modifier,
    list: ImmutableList<AdapterItem>,
    onClick: (Action) -> Unit
) {
    val renderer = LocalAdapterItemRenderer.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.colors.onPrimary),
    ) {
        items(
            count = list.size,
            key = { index ->
                val item = list[index]
                item.id() + item.hashCode()
            }
        ) { index ->
            val item = list[index]

            renderer?.Render(
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp),
                item = item,
                onClick = onClick
            ) ?: Text(item.toString())
        }
    }
}
