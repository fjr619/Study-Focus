package com.fjr619.studyfocus.data.repository

import com.fjr619.studyfocus.data.local.database.SessionDao
import com.fjr619.studyfocus.data.mapper.toSession
import com.fjr619.studyfocus.data.mapper.toSessionEntity
import com.fjr619.studyfocus.domain.model.Session
import com.fjr619.studyfocus.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take

class SessionRepositoryImpl(
    private val sessionDao: SessionDao
) : SessionRepository {

    override suspend fun insertSession(session: Session) {
        sessionDao.insertSession(session.toSessionEntity())
    }

    override suspend fun deleteSession(session: Session) {
        sessionDao.deleteSession(session.toSessionEntity())
    }

    override fun getAllSessions(): Flow<List<Session>> {
        return sessionDao.getAllSessions()
            .map { sessions -> sessions.sortedByDescending { it.date } }
            .map { it.map { sessionEntity -> sessionEntity.toSession() } }
    }

    override fun getRecentFiveSessions(): Flow<List<Session>> {
        return sessionDao.getAllSessions().take(count = 5)
            .map { sessions -> sessions.sortedByDescending { it.date } }
            .map { it.map { sessionEntity -> sessionEntity.toSession() } }
    }

    override fun getRecentTenSessionsForSubject(subjectId: Int): Flow<List<Session>> {
        return sessionDao.getRecentSessionsForSubject(subjectId).take(count = 10)
            .map { sessions -> sessions.sortedByDescending { it.date } }
            .map { it.map { sessionEntity -> sessionEntity.toSession() } }
    }

    override fun getTotalSessionsDuration(): Flow<Long> {
        return sessionDao.getTotalSessionsDuration()
    }

    override fun getTotalSessionsDurationBySubjectId(subjectId: Int): Flow<Long> {
       return  sessionDao.getTotalSessionsDurationBySubjectId(subjectId)
    }
}