package com.fjr619.studyfocus.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.dropUnlessResumed
import com.fjr619.studyfocus.domain.model.Task
import com.fjr619.studyfocus.presentation.util.Priority
import com.fjr619.studyfocus.presentation.util.changeMillisToDateString

@Composable
fun TaskCard(
    modifier: Modifier = Modifier,
    task: Task,
    onCheckBoxClick: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable(
            onClick = dropUnlessResumed {
                onClick()
            }
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TaskCheckBox(
                isComplete = task.isComplete,
                borderColor = Priority.fromInt(task.priority).color,
                onCheckBoxClick = onCheckBoxClick
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = task.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (task.isComplete) {
                        TextDecoration.LineThrough
                    } else TextDecoration.None
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = task.dueDate.changeMillisToDateString(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Preview
@Composable
private fun TaskCardPreview() {
    TaskCard(
        task = Task(
            taskId = 1,
            taskSubjectId = 1,
            relatedToSubject = "Mathematics",
            isComplete = true,
            description = "Task Card Desc",
            title = "Task Card Title",
            priority = Priority.HIGH.value,
            dueDate = 100
        ),
        onCheckBoxClick = { }
    ) {

    }
}