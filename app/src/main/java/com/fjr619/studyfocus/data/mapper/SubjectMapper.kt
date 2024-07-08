package com.fjr619.studyfocus.data.mapper

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.fjr619.studyfocus.data.local.database.entity.SubjectEntity
import com.fjr619.studyfocus.domain.model.Subject

fun Subject.toSubjectEntity(): SubjectEntity {
    return SubjectEntity(
        subjectId = subjectId,
        name = name,
        goalHours = goalHours,
        colors = colors.map {
            it.toArgb()
        }
    )
}

fun SubjectEntity.toSubject(): Subject {
    return Subject(
        subjectId = subjectId,
        name = name,
        goalHours = goalHours,
        colors = colors.map {
            Color(it)
        }
    )
}