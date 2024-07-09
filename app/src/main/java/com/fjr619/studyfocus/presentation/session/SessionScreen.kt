package com.fjr619.studyfocus.presentation.session

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.dropUnlessResumed
import com.fjr619.studyfocus.domain.Dummy
import com.fjr619.studyfocus.presentation.components.DeleteDialog
import com.fjr619.studyfocus.presentation.components.StudySessionsList
import com.fjr619.studyfocus.presentation.components.SubjectListBottomSheet
import com.fjr619.studyfocus.presentation.session.components.ButtonTimer
import com.fjr619.studyfocus.presentation.session.components.RelatedToSubjectSection
import com.fjr619.studyfocus.presentation.session.components.SessionScreenTopBar
import com.fjr619.studyfocus.presentation.session.components.TimerSection
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@RootNavGraph
@Destination
@Composable
fun SessionScreen(
    navigator: DestinationsNavigator
) {
    val viewModel: SessionViewModel = koinViewModel()

    SessionContent(
        onBackButtonClick = { navigator.popBackStack() }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionContent(
    onBackButtonClick: () -> Unit
) {

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    var isBottomSheetOpen by remember { mutableStateOf(false) }

    var isDeleteDialogOpen by rememberSaveable { mutableStateOf(false) }


    SubjectListBottomSheet(
        sheetState = sheetState,
        isOpen = isBottomSheetOpen,
        subjects = Dummy.subjects,
        onDismissRequest = { isBottomSheetOpen = false },
        onSubjectClicked = {
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                if (!sheetState.isVisible) isBottomSheetOpen = false
            }
        }
    )

    DeleteDialog(
        isOpen = isDeleteDialogOpen,
        title = "Delete Session?",
        bodyText = "Are you sure, you want to delete this session? " +
                "This action can not be undone.",
        onDismissRequest = { isDeleteDialogOpen = false },
        onConfirmButtonClick = {
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
                        .aspectRatio(1f)
                )
            }
            item {
                RelatedToSubjectSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    relatedToSubject = "English",
                    selectSubjectButtonClick = { isBottomSheetOpen = true }
                )
            }
            item {
                ButtonTimer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    startButtonClick = { /*TODO*/ },
                    cancelButtonClick = { /*TODO*/ },
                    finishButtonClick = { /*TODO*/ },
                )
            }
            StudySessionsList(
                sectionTitle = "STUDY SESSIONS HISTORY",
                emptyListText = "You don't have any recent study sessions.\n " +
                        "Start a study session to begin recording your progress.",
                sessions = Dummy.sessions,
                onDeleteIconClick = { isDeleteDialogOpen = true }
            )
        }
    }
}



