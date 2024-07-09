package com.fjr619.studyfocus.presentation.session.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.dropUnlessResumed
import com.fjr619.studyfocus.presentation.session.timer_service.TimerState
import com.fjr619.studyfocus.presentation.theme.Red

@Composable
fun ButtonTimer(
    modifier: Modifier,
    startButtonClick: () -> Unit,
    cancelButtonClick: () -> Unit,
    finishButtonClick: () -> Unit,
    timerState: TimerState,
    seconds: String
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(onClick = dropUnlessResumed {
            cancelButtonClick()
        }, enabled = seconds != "00" && timerState != TimerState.STARTED) {
            Text(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                text = "Cancel"
            )
        }
        Button(onClick = dropUnlessResumed {
            startButtonClick()
        }, colors = ButtonDefaults.buttonColors(
            containerColor = when (timerState) {
                TimerState.STARTED -> Red
                else -> MaterialTheme.colorScheme.primary
            },
            contentColor = when (timerState) {
                TimerState.STARTED -> Color.White
                else -> MaterialTheme.colorScheme.onPrimary
            }
        )) {
            Text(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                text = when (timerState) {
                    TimerState.STARTED -> "Pause"
                    TimerState.STOPPED -> "Resume"
                    else -> "Start"
                }
            )
        }
        Button(onClick = dropUnlessResumed {
            finishButtonClick()
        }, enabled = seconds != "00" && timerState != TimerState.STARTED) {
            Text(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                text = "Finish"
            )
        }
    }
}

@Preview
@Composable
private fun ButtonTimerPreview() {
    Surface {
        ButtonTimer(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            startButtonClick = { },
            cancelButtonClick = { },
            finishButtonClick = { },
            timerState = TimerState.IDLE,
            seconds = "00"
        )
    }
}
