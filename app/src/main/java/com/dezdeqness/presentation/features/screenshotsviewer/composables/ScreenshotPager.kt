package com.dezdeqness.presentation.features.screenshotsviewer.composables

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dezdeqness.R
import kotlinx.coroutines.launch
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.toggleScale
import net.engawapg.lib.zoomable.zoomable

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScreenshotPager(
    items: List<String>,
    modifier: Modifier = Modifier,
    state: PagerState,
    onShow: () -> Unit,
    onHide: () -> Unit,
) {
    val scope = rememberCoroutineScope()

    var isUiVisible by remember { mutableStateOf(true) }

    HorizontalPager(
        state = state,
        modifier = modifier.fillMaxSize()
    ) { page ->

        var scale by remember {
            mutableStateOf<ContentScale>(ContentScale.None)
        }

        val zoomState = rememberZoomState()
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(items[page])
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = scale,
            onSuccess = { state ->
                zoomState.setContentSize(state.painter.intrinsicSize)
                scale = ContentScale.Fit
            },
            placeholder = painterResource(id = R.drawable.ic_placeholder),
            error = painterResource(id = R.drawable.ic_placeholder),
            modifier = Modifier
                .fillMaxSize()
                .zoomable(
                    zoomState,
                    onTap = {
                        if (isUiVisible) {
                            onHide()
                        } else {
                            onShow()
                        }

                        isUiVisible = !isUiVisible
                    },
                ),
        )

        BackHandler(zoomState.scale != 1f) {
            scope.launch {
                zoomState.toggleScale(1f, Offset(zoomState.offsetX, zoomState.offsetY))
            }
        }
    }


}
