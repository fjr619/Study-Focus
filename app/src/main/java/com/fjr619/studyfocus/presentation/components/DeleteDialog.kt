package com.fjr619.studyfocus.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.dropUnlessResumed

@Composable
fun DeleteDialog(
    isOpen: Boolean,
    title: String,
    bodyText: String,
    onDismissRequest: () -> Unit,
    onConfirmButtonClick: () -> Unit
) {
    if (isOpen) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = { Text(text = title) },
            text = { Text(text = bodyText) },
            dismissButton = {
                TextButton(onClick = dropUnlessResumed {
                    onDismissRequest()
                }) {
                    Text(text = "Cancel")
                }
            },
            confirmButton = {
                TextButton(onClick = dropUnlessResumed {
                    onConfirmButtonClick()
                }) {
                    Text(text = "Delete")
                }
            }
        )
    }
}