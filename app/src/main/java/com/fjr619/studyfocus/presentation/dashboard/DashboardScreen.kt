package com.fjr619.studyfocus.presentation.dashboard

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fjr619.studyfocus.domain.Dummy
import com.fjr619.studyfocus.domain.model.Subject
import com.fjr619.studyfocus.presentation.components.AddSubjectDialog
import com.fjr619.studyfocus.presentation.components.StudySessionsList
import com.fjr619.studyfocus.presentation.components.TasksList
import com.fjr619.studyfocus.presentation.dashboard.components.CountCardsSection
import com.fjr619.studyfocus.presentation.dashboard.components.DashboardTopBar
import com.fjr619.studyfocus.presentation.dashboard.components.SubjectCardsSection
import com.fjr619.studyfocus.presentation.destinations.SessionScreenDestination
import com.fjr619.studyfocus.presentation.destinations.SubjectScreenDestination
import com.fjr619.studyfocus.presentation.destinations.TaskScreenDestination
import com.fjr619.studyfocus.presentation.subject.SubjectScreenNavArgs
import com.fjr619.studyfocus.presentation.task.TaskScreenNavArgs
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RootNavGraph(start = true)
@Destination
@Composable
fun DashboardScreen(
    navigator: DestinationsNavigator
) {
    DashboardContent(
        onSubjectCardClick = { subjectId ->
            subjectId?.let {
                navigator.navigate(
                    SubjectScreenDestination(
                        SubjectScreenNavArgs(subjectId = it)
                    )
                )
            }
        },
        onTaskCardClick = { taskId ->
            navigator.navigate(
                TaskScreenDestination(
                    TaskScreenNavArgs(taskId = taskId, subjectId = null)
                )
            )
        },
        onStartSessionButtonClick = { navigator.navigate(SessionScreenDestination()) }
    )
}

@Composable
fun DashboardContent(
    modifier: Modifier = Modifier,
    onSubjectCardClick: (Int?) -> Unit,
    onTaskCardClick: (Int?) -> Unit,
    onStartSessionButtonClick: () -> Unit,
) {

    var isAddSubjectDialogOpen by rememberSaveable { mutableStateOf(false) }
    var subjectName by remember { mutableStateOf("") }
    var goalHours by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf(listOf<Color>()) }

    AddSubjectDialog(
        isOpen = isAddSubjectDialogOpen,
        selectedColors = selectedColor,
        subjectName = subjectName,
        goalHours = goalHours,
        onColorChange = { selectedColor = it },
        onSubjectNameChange = { subjectName = it },
        onGoalHoursChange = { goalHours = it },
        onDismissRequest = { isAddSubjectDialogOpen = false },
        onConfirmButtonClick = { isAddSubjectDialogOpen = false }
    )

    Scaffold(
        topBar = {
            DashboardTopBar()
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                CountCardsSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    subjectCount = 0,
                    studiedHours = "0",
                    goalHours = "0"
                )
            }
            item {
                SubjectCardsSection(
                    modifier = Modifier.fillMaxWidth(),
                    subjectList = Dummy.subjects,
                    onAddIconClicked = {
                        selectedColor = Subject.subjectCardColors.random()
                        isAddSubjectDialogOpen = true
                    },
                    onSubjectClicked = onSubjectCardClick
                )
            }
            item {
                Button(
                    onClick = onStartSessionButtonClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 48.dp, vertical = 20.dp)
                ) {
                    Text(text = "Start Study Session")
                }
            }

            TasksList(
                sectionTitle = "UPCOMING TASKS",
                emptyListText = "You don't have any upcoming tasks.\n " +
                        "Click the + button in subject screen to add new task.",
                tasks = listOf(),
                onCheckBoxClick = { /*TODO*/ },
                onTaskCardClick = onTaskCardClick
            )

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }

            StudySessionsList(
                sectionTitle = "RECENT STUDY SESSIONS",
                emptyListText = "You don't have any recent study sessions.\n " +
                        "Start a study session to begin recording your progress.",
                sessions = listOf(),
                onDeleteIconClick = {}
            )
        }
    }
}

