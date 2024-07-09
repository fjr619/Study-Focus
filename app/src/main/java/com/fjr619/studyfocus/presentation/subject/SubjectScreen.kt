package com.fjr619.studyfocus.presentation.subject

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.dropUnlessResumed
import com.fjr619.studyfocus.presentation.components.AddSubjectDialog
import com.fjr619.studyfocus.presentation.components.DeleteDialog
import com.fjr619.studyfocus.presentation.components.StudySessionsList
import com.fjr619.studyfocus.presentation.components.TasksList
import com.fjr619.studyfocus.presentation.destinations.TaskScreenDestination
import com.fjr619.studyfocus.presentation.subject.components.SubjectOverviewSection
import com.fjr619.studyfocus.presentation.subject.components.SubjectScreenTopBar
import com.fjr619.studyfocus.presentation.task.TaskScreenNavArgs
import com.fjr619.studyfocus.presentation.util.ObserveAsEvents
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

data class SubjectScreenNavArgs(
    val subjectId: Int
)

@RootNavGraph
@Destination(
    navArgsDelegate = SubjectScreenNavArgs::class
)
@Composable
fun SubjectScreen(
    navigator: DestinationsNavigator
) {
    val viewModel: SubjectViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(flow = viewModel.events) { event ->
        when (event) {
            SubjectContract.Event.NavigateBack -> navigator.navigateUp()
        }
    }

    SubjectContent(
        state = state,
        onAction = viewModel::onAction,
        onBackButtonClick = { navigator.navigateUp() },
        onAddTaskButtonClick = {
            val navArg = TaskScreenNavArgs(taskId = null, subjectId = state.currentSubjectId)
            navigator.navigate(TaskScreenDestination(navArgs = navArg))
        },
        onTaskCardClick = { taskId ->
            val navArg = TaskScreenNavArgs(taskId = taskId, subjectId = null)
            navigator.navigate(TaskScreenDestination(navArgs = navArg))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectContent(
    state: SubjectContract.State,
    onAction: (SubjectContract.Action) -> Unit,
    onBackButtonClick: () -> Unit,
    onAddTaskButtonClick: () -> Unit,
    onTaskCardClick: (Int?) -> Unit
) {

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val listState = rememberLazyListState()
    val isFABExpanded by remember { derivedStateOf { listState.firstVisibleItemIndex == 0 } }

    var isEditSubjectDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isDeleteSubjectDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isDeleteSessionDialogOpen by rememberSaveable { mutableStateOf(false) }

    //TODO improvements
//    LaunchedEffect(key1 = state.studiedHours, key2 = state.goalStudyHours) {
//        onEvent(SubjectContract.Event.UpdateProgress)
//    }

    AddSubjectDialog(
        isOpen = isEditSubjectDialogOpen,
        subjectName = state.subjectName,
        goalHours = state.goalStudyHours,
        onSubjectNameChange = { onAction(SubjectContract.Action.OnSubjectNameChange(it)) },
        onGoalHoursChange = { onAction(SubjectContract.Action.OnGoalStudyHoursChange(it)) },
        selectedColors = state.subjectCardColors,
        onColorChange = { onAction(SubjectContract.Action.OnSubjectCardColorChange(it)) },
        onDismissRequest = {
            onAction(SubjectContract.Action.ResetSubject)
            isEditSubjectDialogOpen = false
        },
        onConfirmButtonClick = {
            onAction(SubjectContract.Action.UpdateSubject)
            isEditSubjectDialogOpen = false
        }
    )

    DeleteDialog(
        isOpen = isDeleteSubjectDialogOpen,
        title = "Delete Subject?",
        bodyText = "Are you sure, you want to delete this subject? All related " +
                "tasks and study sessions will be permanently removed. This action can not be undone",
        onDismissRequest = { isDeleteSubjectDialogOpen = false },
        onConfirmButtonClick = {
            onAction(SubjectContract.Action.DeleteSubject)
            isDeleteSubjectDialogOpen = false
        }
    )

    DeleteDialog(
        isOpen = isDeleteSessionDialogOpen,
        title = "Delete Session?",
        bodyText = "Are you sure, you want to delete this session? Your studied hours will be reduced " +
                "by this session time. This action can not be undone.",
        onDismissRequest = { isDeleteSessionDialogOpen = false },
        onConfirmButtonClick = { isDeleteSessionDialogOpen = false }
    )

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SubjectScreenTopBar(
                title = state.subjectName,
                onBackButtonClick = onBackButtonClick,
                onDeleteButtonClick = { isDeleteSubjectDialogOpen = true },
                onEditButtonClick = { isEditSubjectDialogOpen = true },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = dropUnlessResumed {
                    onAddTaskButtonClick()
                },
                icon = { Icon(imageVector = Icons.Default.Add, contentDescription = "Add") },
                text = { Text(text = "Add Task") },
                expanded = isFABExpanded
            )
        }
    ) { paddingValue ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
        ) {
            item {
                SubjectOverviewSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    studiedHours = state.studiedHours.toString(),
                    goalHours = state.goalStudyHours,
                    progress = state.progress
                )
            }
            TasksList(
                sectionTitle = "UPCOMING TASKS",
                emptyListText = "You don't have any upcoming tasks.\n " +
                        "Click the + button to add new task.",
                tasks = state.upcomingTasks,
                onCheckBoxClick = { onAction(SubjectContract.Action.OnTaskIsCompleteChange(it)) },
                onTaskCardClick = onTaskCardClick,
            )
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
            TasksList(
                sectionTitle = "COMPLETED TASKS",
                emptyListText = "You don't have any completed tasks.\n " +
                        "Click the check box on completion of task.",
                tasks = state.completedTasks,
                onCheckBoxClick = { onAction(SubjectContract.Action.OnTaskIsCompleteChange(it)) },
                onTaskCardClick = onTaskCardClick,
            )
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
            StudySessionsList(
                sectionTitle = "RECENT STUDY SESSIONS",
                emptyListText = "You don't have any recent study sessions.\n " +
                        "Start a study session to begin recording your progress.",
                sessions = state.recentSessions,
                onDeleteIconClick = {
                    isDeleteSessionDialogOpen = true
                    onAction(SubjectContract.Action.OnDeleteSessionButtonClick(it))
                }
            )
        }
    }
}



