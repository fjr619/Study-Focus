package com.fjr619.studyfocus.data.mapper

import com.fjr619.studyfocus.data.local.database.entity.SessionEntity
import com.fjr619.studyfocus.domain.model.Session

fun Session.toSessionEntity(): SessionEntity = SessionEntity(
    relatedToSubject = relatedToSubject,
    date = date,
    duration = duration,
    sessionSubjectId = sessionSubjectId,
    sessionId = sessionId
)

fun SessionEntity.toSession(): Session = Session(
    relatedToSubject = relatedToSubject,
    date = date,
    duration = duration,
    sessionSubjectId = sessionSubjectId,
    sessionId = sessionId
)