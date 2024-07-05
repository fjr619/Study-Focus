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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fjr619.studyfocus.domain.Dummy
import com.fjr619.studyfocus.presentation.components.StudySessionsList
import com.fjr619.studyfocus.presentation.components.TasksList
import com.fjr619.studyfocus.presentation.dashboard.components.CountCardsSection
import com.fjr619.studyfocus.presentation.dashboard.components.DashboardTopBar
import com.fjr619.studyfocus.presentation.dashboard.components.SubjectCardsSection

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier
) {

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
                    onAddIconClicked = { /*TODO*/ }
                )
            }
            item {
                Button(
                    onClick = { /*TODO*/ },
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
                onTaskCardClick = { /*TODO*/ }
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

