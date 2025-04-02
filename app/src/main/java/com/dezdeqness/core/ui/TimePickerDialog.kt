package com.dezdeqness.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dezdeqness.R
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.core.ui.views.AppButton

private const val VISIBLE_ITEMS = 3

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    modifier: Modifier = Modifier,
    time: TimeData,
    sheetState: SheetState,
    onTimeSelected: (Int, Int) -> Unit,
    onDismissRequest: () -> Unit,
) {
    var selectedTime by remember { mutableStateOf(time) }

    val hours = mutableListOf<Int>().apply {
        for (hour in 0..23) {
            add(hour)
        }
    }

    val minutes = mutableListOf<Int>().apply {
        for (minute in 0..59) {
            add(minute)
        }
    }

    ModalBottomSheet(
        onDismissRequest = {
            onDismissRequest()
        },
        sheetState = sheetState,
        sheetGesturesEnabled = false,
        dragHandle = null,
        containerColor = colorResource(id = R.color.background_tint),
    ) {
        Column(
            modifier = modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val fontSize = 16

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max),
                contentAlignment = Alignment.Center
            ) {
                val height = (fontSize + 10).dp

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 100.dp, end = 100.dp)
                ) {
                    WheelView(
                        modifier = Modifier.weight(3f),
                        itemSize = DpSize(150.dp, height),
                        selectedValue = selectedTime.hours,
                        itemCount = hours.size,
                        rowOffset = VISIBLE_ITEMS,
                        onItemSelected = {
                            selectedTime = selectedTime.copy(hours = hours[it])
                        },
                        content = { index, alpha ->
                            Text(
                                text = hours[index].toString().padStart(2, '0'),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.width(50.dp),
                                fontSize = fontSize.sp,
                                color = AppTheme.colors.textPrimary.copy(alpha = alpha),
                            )
                        })

                    WheelView(
                        modifier = Modifier.weight(3f),
                        itemSize = DpSize(150.dp, height),
                        selectedValue = selectedTime.minutes,
                        itemCount = minutes.size,
                        rowOffset = VISIBLE_ITEMS,
                        onItemSelected = {
                            selectedTime = selectedTime.copy(minutes = minutes[it])
                        },
                        content = { index, alpha ->
                            Text(
                                text = minutes[index].toString().padStart(2, '0'),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.width(100.dp),
                                fontSize = fontSize.sp,
                                color = AppTheme.colors.textPrimary.copy(alpha = alpha),
                            )
                        })
                }

                SelectorView(visibleItems = VISIBLE_ITEMS)
            }

            AppButton(
                title = stringResource(R.string.timepicker_save_title),
                modifier = Modifier.fillMaxWidth(),
            ) {
                onTimeSelected(selectedTime.hours, selectedTime.minutes)
            }
        }
    }
}

data class TimeData(val hours: Int, val minutes: Int)

@Composable
private fun SelectorView(modifier: Modifier = Modifier, visibleItems: Int) {
    Column(
        modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.weight(visibleItems.toFloat()).fillMaxWidth())

        Column(
            modifier = Modifier
                .weight(1.13f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .height(0.5.dp)
                    .alpha(0.5f)
                    .background(AppTheme.colors.onSecondary)
                    .fillMaxWidth()
            )
            Box(
                modifier = Modifier
                    .height(0.5.dp)
                    .alpha(0.5f)
                    .background(AppTheme.colors.onSecondary)
                    .fillMaxWidth()
            )
        }

        Box(modifier = Modifier.weight(visibleItems.toFloat()).fillMaxWidth())
    }
}
