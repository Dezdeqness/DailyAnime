package com.dezdeqness.core.ui.views.image

import androidx.annotation.DrawableRes
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter.State
import coil.request.ImageRequest

@Composable
fun AppImage(
    modifier: Modifier = Modifier,
    data: String,
    shape: Shape = RoundedCornerShape(8.dp),
    colorFilter: ColorFilter? = null,
    contentScale: ContentScale = ContentScale.Crop,
    contentDescription: String? = null,
    @DrawableRes placeholderId: Int = com.dezdeqness.core.ui.R.drawable.ic_placeholder,
    @DrawableRes errorId: Int = com.dezdeqness.core.ui.R.drawable.ic_placeholder,
) {
    val context = LocalContext.current

    val model = remember(data) {
        ImageRequest.Builder(context)
            .data(data)
            .crossfade(true)
            .build()
    }

    AsyncImage(
        model = model,
        contentScale = contentScale,
        contentDescription = contentDescription,
        colorFilter = colorFilter,
        placeholder = painterResource(id = placeholderId),
        error = painterResource(id = errorId),
        modifier = modifier.clip(shape),
    )

}

@Composable
fun AppImage(
    modifier: Modifier = Modifier,
    data: String,
    onLoading: ((State.Loading) -> Unit)? = null,
    onSuccess: ((State.Success) -> Unit)? = null,
    onError: ((State.Error) -> Unit)? = null,
    contentScale: ContentScale = ContentScale.Crop,
    contentDescription: String? = null,
    @DrawableRes placeholderId: Int = com.dezdeqness.core.ui.R.drawable.ic_placeholder,
    @DrawableRes errorId: Int = com.dezdeqness.core.ui.R.drawable.ic_placeholder,
) {
    val context = LocalContext.current

    val model = remember(data) {
        ImageRequest.Builder(context)
            .data(data)
            .crossfade(true)
            .build()
    }

    AsyncImage(
        model = model,
        contentScale = contentScale,
        contentDescription = contentDescription,
        onLoading = onLoading,
        onSuccess = onSuccess,
        onError = onError,
        placeholder = painterResource(id = placeholderId),
        error = painterResource(id = errorId),
        modifier = modifier
    )

}

