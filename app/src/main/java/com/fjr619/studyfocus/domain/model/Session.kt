package com.fjr619.studyfocus.domain.model

data class Session(
    val sessionSubjectId: Int,
    val relatedToSubject: String,
    val date: Long,
    val duration: Long,
    val sessionId: Int?
)