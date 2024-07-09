package com.fjr619.studyfocus.presentation.session.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fjr619.studyfocus.presentation.util.Digit
import com.fjr619.studyfocus.presentation.util.compareTo

@Composable
fun TimerSection(
    modifier: Modifier,
    hours: String,
    minutes: String,
    seconds: String
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(250.dp)
                .border(5.dp, MaterialTheme.colorScheme.surfaceVariant, CircleShape)
        )

        Row {
            AnimatedTicker(value = hours, additional = ":")
            AnimatedTicker(value = minutes, additional = ":")
            AnimatedTicker(value = seconds)

            
        }
    }
}


@Composable
private fun AnimatedTicker(value: String, duration: Int = 600, additional: String = "") {
    Row {
        value.mapIndexed { index, c -> Digit(c, value.toInt(), index) }
            .forEach { digit ->
                AnimatedContent(
                    targetState = digit,
                    label = value,
                    transitionSpec = {
                        if (targetState > initialState) {
                            slideInVertically { it } + fadeIn(animationSpec = tween(duration)) togetherWith slideOutVertically { -it } + fadeOut(
                                animationSpec = tween(duration)
                            )
                        } else {
                            slideInVertically { -it } + fadeIn(animationSpec = tween(duration)) togetherWith slideOutVertically { it } + fadeOut(
                                animationSpec = tween(duration)
                            )
                        }
                    }) { data ->
                    Text(
                        text = "${data.digitChar}",
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 45.sp)
                    )
                }
            }
        Text(text = additional, style = MaterialTheme.typography.titleLarge.copy(fontSize = 45.sp))
    }
}

@Preview
@Composable
private fun TimerSectionPreview() {
    Surface {
        TimerSection(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            hours = "10",
            minutes = "04",
            seconds = "30"
        )
    }
}