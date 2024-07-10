package com.fjr619.studyfocus.presentation.session

import android.content.Intent
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.dropUnlessResumed
import com.fjr619.studyfocus.domain.Dummy
import com.fjr619.studyfocus.presentation.components.DeleteDialog
import com.fjr619.studyfocus.presentation.components.StudySessionsList
import com.fjr619.studyfocus.presentation.components.SubjectListBottomSheet
import com.fjr619.studyfocus.presentation.session.components.ButtonTimer
import com.fjr619.studyfocus.presentation.session.components.RelatedToSubjectSection
import com.fjr619.studyfocus.presentation.session.components.SessionScreenTopBar
import com.fjr619.studyfocus.presentation.session.components.TimerSection
import com.fjr619.studyfocus.presentation.session.timer_service.ServiceHelper
import com.fjr619.studyfocus.presentation.session.timer_service.SessionTimerService
import com.fjr619.studyfocus.presentation.session.timer_service.TimerState
import com.fjr619.studyfocus.presentation.util.Constants
import com.fjr619.studyfocus.presentation.util.Constants.ACTION_SERVICE_CANCEL
import com.fjr619.studyfocus.presentation.util.Constants.ACTION_SERVICE_START
import com.fjr619.studyfocus.presentation.util.Constants.ACTION_SERVICE_STOP
import com.fjr619.studyfocus.presentation.util.Constants.MINIMUM_TIMER
import com.ramcosta.composedestinations.annotation.DeepLink
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import kotlin.time.DurationUnit

@RootNavGraph
@Destination(
    deepLinks = [
        DeepLink(
            action = Intent.ACTION_VIEW,
            uriPattern = "study_focus://dashboard/session"
        )
    ]
)
@Composable
fun SessionScreen(
    navigator: DestinationsNavigator,
    timerService: SessionTimerService,
) {

    val context = LocalContext.current
    val viewModel: SessionViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(
        key1 = timerService.currentTimerState.value,
        key2 = timerService.seconds.value
    ) {
        viewModel.updateTimer(
            hours = timerService.hours.value,
            minutes = timerService.minutes.value,
            seconds = timerService.seconds.value,
            currentTimerState = timerService.currentTimerState.value
        )
    }

    //update from timerService, contoh ketika open dari notifikasi atau ketika balik dari dasjboard dengan timer masih hidup
    LaunchedEffect(
        key1 = state.subjects
    ) {
        viewModel.onAction(SessionContract.Action.UpdateSubjectIdAndRelatedSubject(
            subjectId = timerService.subjectId.value,
            relatedToSubject = state.subjects.find { it.subjectId == timerService.subjectId.value }?.name
        ))
    }

    SessionContent(
        state = state,
        onAction = {

            when(it) {
                is SessionContract.Action.StartSession -> {
                    ServiceHelper.triggerForegroundService(
                        context = context,
                        action = if (state.currentTimerState == TimerState.STARTED) {
                            ACTION_SERVICE_STOP
                        } else {
                            ACTION_SERVICE_START
                        }
                    )
                    timerService.subjectId.value = state.subjectId
                }

                is SessionContract.Action.StopSession -> {
                    ServiceHelper.triggerForegroundService(
                        context = context,
                        action = ACTION_SERVICE_CANCEL
                    )
                }

                is SessionContract.Action.FinishSession -> {
                    val duration = timerService.duration.toLong(DurationUnit.SECONDS)
                    if (duration >= MINIMUM_TIMER ) {
                        ServiceHelper.triggerForegroundService(
                            context = context,
                            action = ACTION_SERVICE_CANCEL
                        )
                    }
                    viewModel.onAction(SessionContract.Action.SaveSession(duration))
                }

                else -> {
                    viewModel.onAction(it)
                }
            }
        },
        onBackButtonClick = { navigator.popBackStack() }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionContent(
    state: SessionContract.State,
    onAction: (SessionContract.Action) -> Unit,
    onBackButtonClick: () -> Unit
) {

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    var isBottomSheetOpen by remember { mutableStateOf(false) }
    var isDeleteDialogOpen by rememberSaveable { mutableStateOf(false) }

    SubjectListBottomSheet(
        sheetState = sheetState,
        isOpen = isBottomSheetOpen,
        subjects = state.subjects,
        onDismissRequest = { isBottomSheetOpen = false },
        onSubjectClicked = {
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                if (!sheetState.isVisible) isBottomSheetOpen = false
            }
            onAction(SessionContract.Action.OnRelatedSubjectChange(it))
        }
    )

    DeleteDialog(
        isOpen = isDeleteDialogOpen,
        title = "Delete Session?",
        bodyText = "Are you sure, you want to delete this session? " +
                "This action can not be undone.",
        onDismissRequest = { isDeleteDialogOpen = false },
        onConfirmButtonClick = {
            onAction(SessionContract.Action.DeleteSession)
            isDeleteDialogOpen = false
        }
    )

    Scaffold(
        topBar = {
            SessionScreenTopBar(onBackButtonClick = dropUnlessResumed {
                onBackButtonClick()
            })
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                TimerSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    hours = state.hours,
                    minutes = state.minutes,
                    seconds = state.seconds
                )
            }
            item {
                RelatedToSubjectSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    relatedToSubject = state.relatedToSubject ?: "",
                    selectSubjectButtonClick = { isBottomSheetOpen = true }
                )
            }
            item {
                ButtonTimer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    startButtonClick = {
                        if (state.subjectId != null && state.relatedToSubject != null) {
                            onAction(SessionContract.Action.StartSession)
                        } else {
                            onAction(SessionContract.Action.NotifyToUpdateSubject)
                        }
                    },
                    cancelButtonClick = {
                        onAction(SessionContract.Action.StopSession)
                    },
                    finishButtonClick = {
                        onAction(SessionContract.Action.FinishSession)
                    },
                    timerState = state.currentTimerState,
                    seconds = state.seconds
                )
            }
            StudySessionsList(
                sectionTitle = "STUDY SESSIONS HISTORY",
                emptyListText = "You don't have any recent study sessions.\n " +
                        "Start a study session to begin recording your progress.",
                sessions = state.sessions,
                onDeleteIconClick = {
                    isDeleteDialogOpen = true
                    onAction(SessionContract.Action.OnDeleteSessionButtonClick(it))
                }
            )
        }
    }
}



