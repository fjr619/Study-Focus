package com.fjr619.studyfocus.presentation.session.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.dropUnlessResumed

@Composable
fun RelatedToSubjectSection(
    modifier: Modifier,
    relatedToSubject: String,
    selectSubjectButtonClick: () -> Unit
) {
    Column(modifier = modifier) {
        Text(
            text = "Related to subject",
            style = MaterialTheme.typography.bodySmall
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = relatedToSubject,
                style = MaterialTheme.typography.bodyLarge
            )
            IconButton(onClick = dropUnlessResumed {
                selectSubjectButtonClick()
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Select Subject"
                )
            }
        }
    }
}

@Preview
@Composable
private fun RelatedToSubjectSectionPreview() {
    Surface {
        RelatedToSubjectSection(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            relatedToSubject = "English",
            selectSubjectButtonClick = {  }
        )
    }
}