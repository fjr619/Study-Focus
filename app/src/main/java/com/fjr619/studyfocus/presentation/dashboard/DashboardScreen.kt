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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fjr619.studyfocus.domain.model.Session
import com.fjr619.studyfocus.domain.model.Task
import com.fjr619.studyfocus.presentation.components.AddSubjectDialog
import com.fjr619.studyfocus.presentation.components.DeleteDialog
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
import org.koin.androidx.compose.koinViewModel

@RootNavGraph(start = true)
@Destination
@Composable
fun DashboardScreen(
    navigator: DestinationsNavigator
) {

    val dashboardViewModel = koinViewModel<DashboardViewModel>()
    val state by dashboardViewModel.state.collectAsStateWithLifecycle()
    val tasks by dashboardViewModel.tasks.collectAsStateWithLifecycle()
    val recentSessions by dashboardViewModel.recentSessions.collectAsStateWithLifecycle()

    DashboardContent(
        state = state,
        tasks = tasks,
        recentSessions = recentSessions,
        onAction = dashboardViewModel::onAction,
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
    state: DashboardContract.State,
    tasks: List<Task>,
    recentSessions: List<Session>,
    onAction: (DashboardContract.Action) -> Unit,
    onSubjectCardClick: (Int?) -> Unit,
    onTaskCardClick: (Int?) -> Unit,
    onStartSessionButtonClick: () -> Unit,
) {

    var isAddSubjectDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isDeleteSessionDialogOpen by rememberSaveable { mutableStateOf(false) }

    AddSubjectDialog(
        isOpen = isAddSubjectDialogOpen,
        selectedColors = state.newSubjectCardColors,
        subjectName = state.newSubjectName,
        goalHours = state.newSubjectGoalStudyHours,
        onColorChange = { onAction(DashboardContract.Action.OnSubjectCardColorChange(it)) },
        onSubjectNameChange = { onAction(DashboardContract.Action.OnSubjectNameChange(it)) },
        onGoalHoursChange = { onAction(DashboardContract.Action.OnGoalStudyHoursChange(it)) },
        onDismissRequest = {
            onAction(DashboardContract.Action.ResetSubject)
            isAddSubjectDialogOpen = false
                           },
        onConfirmButtonClick = {
            onAction(DashboardContract.Action.SaveSubject)
            isAddSubjectDialogOpen = false
        }
    )

    DeleteDialog(
        isOpen = isDeleteSessionDialogOpen,
        title = "Delete Session?",
        bodyText = "Are you sure, you want to delete this session? Your studied hours will be reduced " +
                "by this session time. This action can not be undone.",
        onDismissRequest = { isDeleteSessionDialogOpen = false },
        onConfirmButtonClick = {
            onAction(DashboardContract.Action.DeleteSession)
            isDeleteSessionDialogOpen = false
        }
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
                    subjectCount = state.totalSubjectCount,
                    studiedHours = state.totalStudiedHours.toString(),
                    goalHours = state.totalGoalStudyHours.toString()
                )
            }
            item {
                SubjectCardsSection(
                    modifier = Modifier.fillMaxWidth(),
                    subjectList = state.subjects,
                    onAddIconClicked = {
                        onAction(DashboardContract.Action.ResetSubject)
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
                tasks = tasks,
                onCheckBoxClick = { onAction(DashboardContract.Action.OnTaskIsCompleteChange(it)) },
                onTaskCardClick = onTaskCardClick
            )

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }

            StudySessionsList(
                sectionTitle = "RECENT STUDY SESSIONS",
                emptyListText = "You don't have any recent study sessions.\n " +
                        "Start a study session to begin recording your progress.",
                sessions = recentSessions,
                onDeleteIconClick = {
                    isDeleteSessionDialogOpen = true
                    onAction(DashboardContract.Action.OnDeleteSessionButtonClick(it))
                }
            )
        }
    }
}

