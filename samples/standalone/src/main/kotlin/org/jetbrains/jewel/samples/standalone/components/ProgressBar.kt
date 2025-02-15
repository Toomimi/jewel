package org.jetbrains.jewel.samples.standalone.components

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.jetbrains.jewel.GroupHeader
import org.jetbrains.jewel.HorizontalProgressBar
import org.jetbrains.jewel.IndeterminateHorizontalProgressBar
import org.jetbrains.jewel.Text

@Composable
fun ProgressBar() {
    GroupHeader("Progress bars")
    val transition = rememberInfiniteTransition()
    val currentOffset by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 4000
                0f at 1000
                1f at 3000
            },
        ),
    )
    var intermittentProgress by remember { mutableStateOf(0f) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(800)
            if (intermittentProgress >= .9) {
                intermittentProgress = 0f
            } else {
                intermittentProgress += .25f
            }
        }
    }
    Column {
        Text("HorizontalProgressBar - linear progress")
        Row(Modifier.width(600.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            HorizontalProgressBar(modifier = Modifier.width(500.dp), progress = currentOffset)
            Text("${(currentOffset * 100).toInt()} %")
        }
    }
    Column {
        Text("HorizontalProgressBar - non linear progress")
        Row(Modifier.width(600.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            HorizontalProgressBar(modifier = Modifier.width(500.dp), progress = intermittentProgress)
            Text("${(intermittentProgress * 100).toInt()} %")
        }
    }
    Column {
        Text("HorizontalProgressBar - smoothed non linear progress")
        Row(Modifier.width(600.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            val smoothedProgress by androidx.compose.animation.core.animateFloatAsState(intermittentProgress)
            HorizontalProgressBar(modifier = Modifier.width(500.dp), progress = smoothedProgress)
            Text("${(intermittentProgress * 100).toInt()} %")
        }
    }
    Column {
        Text("IndeterminateHorizontalProgressBar")
        Row(Modifier.width(600.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            IndeterminateHorizontalProgressBar(modifier = Modifier.width(500.dp))
            Text("----")
        }
    }
}
